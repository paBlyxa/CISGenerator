package com.we.cisgenerator.service;

import com.we.cisgenerator.model.AI_Internal;
import com.we.cisgenerator.model.PLC;
import com.we.cisgenerator.model.XR;
import com.we.cisgenerator.model.internal.DPFilter;
import com.we.cisgenerator.model.winccoa.Dp;
import com.we.cisgenerator.model.winccoa.SDS_PLC;
import com.we.jackcess.core.Criteria;
import com.we.jackcess.core.Factory;
import com.we.jackcess.core.exceptions.*;
import com.we.jackcess.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DBService {

	private final static Logger logger = LoggerFactory.getLogger(DBService.class);

	public static List<PLC> getListPLC(File file) throws IOException {
		List<PLC> listPLC = new ArrayList<>();
		String str = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			str = br.readLine();
		} catch (IOException e) {
			logger.error("An error occured while reading file", e);
		}
		if (str != null) {
			if (!str.contains("# ascii dump of database")) {
				Factory factory = new Factory(file);
				try {
					listPLC = factory.createCriteria(PLC.class).list();
					// Connect couplers
					for (PLC coupler : listPLC) {
						if (coupler.isInterfaceModule()) {
							if (coupler.getReservPLC() != null) {
								PLC plc = getPLC(listPLC, coupler.getReservPLC().getId());
								plc.addCoupler(coupler);
								if (coupler.isReserved()) {
									PLC reservPLC = plc.getReservPLC();
									reservPLC.addCoupler(coupler);
								}
							}
						}
					}
					if (logger.isDebugEnabled()){
						for (PLC plc : listPLC){
							logger.debug(plc.toString());
						}
					}
					listPLC.removeIf(plc -> (plc.isInterfaceModule() && (plc.getReservPLC() != null)));
					// Digital signals
					List<XR> listDAI = factory.createCriteria(XR.class).list();
					for (XR dai : listDAI) {
						dai.getPlc().addDAI(dai);
					}
					// Inernal AI
					List<AI_Internal> listAIInternal = factory.createCriteria(AI_Internal.class).list();
					for (AI_Internal ai : listAIInternal) {
						if (ai.getPlc().isInterfaceModule()) {
							if (ai.getPlc().getReservPLC() != null){
								ai.getPlc().getReservPLC().addAIInernal(ai);
								if (ai.getPlc().isReserved()) {
									ai.getPlc().getReservPLC().getReservPLC().addAIInernal(ai);
								}
							}
						} else {
							ai.getPlc().addAIInernal(ai);
						}
					}

				} catch (NotFoundException e) {
					logger.error("Not found: " + e.getCriteria(), e);
				} catch (InvalidCriteriaClassException e) {
					logger.error("Invalid criteria class - " + e.getCriteriaClass().getCanonicalName(), e);
				} catch (InvokeMethodException e) {
					logger.error("Illegal argument exception", e);
				}
			} else {
				Map<String, Dp> dpMap = DatapointService.importDp(file);
				for (Dp dp : dpMap.values()) {
					if (dp instanceof SDS_PLC) {
						listPLC.add((PLC) dp);
					}
				}
			}
		}
		return listPLC;

	}

	public static <T> List<T> list(File file, Class<T> criteriaClass, PLC plc) throws AccessException {
		List<PLC> listPLC = new ArrayList<>();
		listPLC.add(plc);
		if (plc.isReserved()) {
			listPLC.add(plc.getReservPLC());
		}
		return list(file, criteriaClass, listPLC, null);
	}

	public static <T> List<T> list(File file, Class<T> criteriaClass, List<PLC> plcList, List<DPFilter> dpFilters) throws AccessException {
		List<T> list = new ArrayList<>();
		String str = null;
		logger.debug("Try to read {} from file: {}", criteriaClass, file);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			str = br.readLine();
		} catch (IOException e) {
			logger.error("An error occured while reading file", e);
		}
		if (str != null) {
			if (!str.contains("# ascii dump of database")) {
				Factory factory = new Factory(file);
				Collection<Object> listCriteria = new ArrayList<>();
				if (plcList != null) {
					for (PLC plc : plcList) {
						listCriteria.add(plc.getId());
						if (plc.isReserved()) {
							listCriteria.add(plc.getReservPLC().getId());
						}
						if (!plc.getCouplers().isEmpty()) {
							for (PLC coupler : plc.getCouplers()) {
								listCriteria.add(coupler.getId());
							}
						}
					}
				}
				try {
					Criteria<T> criteria = factory.createCriteria(criteriaClass);
					if (listCriteria != null && !listCriteria.isEmpty()) {
						criteria.add(Restrictions.in("PLC", listCriteria));
					}
					if ((dpFilters != null) && (!dpFilters.isEmpty())){
						for (DPFilter dpFilter : dpFilters){
							criteria.add(dpFilter.getRestriction());
						}
					}
					list = criteria.list();
				} catch (NotFoundException e) {
					logger.error("Not found: " + e.getCriteria(), e);
					throw e;
				} catch (InvalidCriteriaClassException e) {
					logger.error("Invalid criteria class - " + e.getCriteriaClass().getCanonicalName(), e);
					throw e;
				} catch (InvokeMethodException e) {
					logger.error("Illegal argument exception", e);
					throw e;
				} catch (NullPointerAccessException e) {
					logger.error(e.getMessage(), e);
					throw e;
				}
			} else {
				// TODO read file from WINCC OA
			}
		}
		return list;
	}

	public static <T> List<T> list(File file, Class<T> criteriaClass, List<DPFilter> dpFilters) throws AccessException {
		return list(file, criteriaClass, new ArrayList<PLC>(), dpFilters);
	}
	
	public static <T> List<T> list(File file, Class<T> criteriaClass) throws AccessException {
		return list(file, criteriaClass, new ArrayList<PLC>(), null);
	}
	
	public static <T> List<String> getColumns(File file, Class<T> criteriaClass) throws InvalidCriteriaClassException, IOException{
		Factory factory = new Factory(file);
		Criteria<T> criteria = factory.createCriteria(criteriaClass);
		return criteria.getColumns();
	}
	
	private static PLC getPLC(List<PLC> listPLC, int id) {
		for (PLC plc : listPLC) {
			if (plc.getId() == id) {
				return plc;
			}
		}
		return null;
	}

	/*
	 * public static List<DAI> getDAI(File file, PLC plc){ List<DAI> listDAI =
	 * new ArrayList<>(); try (Database db = DatabaseBuilder.open(file)){ Table
	 * tableDAI = db.getTable(DAI.getTableName()); Map<Integer, DAI.Type>
	 * typeMap = getDAITypeMap(db); Map<String, ExternalSystem> extSysMap =
	 * getExtSysMap(db);
	 * 
	 * Cursor cursor = CursorBuilder.createCursor(tableDAI); Map<String,
	 * Integer> mapPLC = Collections.singletonMap(DAI.Fields.PLC.getName(),
	 * plc.getId()); while (cursor.findNextRow(mapPLC)){ Row row =
	 * cursor.getCurrentRow(); DAI dai = new DAI(row);
	 * dai.setType(typeMap.get(row.getInt(DAI.Fields.TYPE.getName())));
	 * dai.setSystem(extSysMap.get(row.getString(DAI.Fields.INFO_SYSTEM.getName(
	 * )))); listDAI.add(dai); } if (plc.isReserved()){ cursor =
	 * CursorBuilder.createCursor(tableDAI); mapPLC =
	 * Collections.singletonMap(DAI.Fields.PLC.getName(),
	 * plc.getReservPLC().getId()); while (cursor.findNextRow(mapPLC)){ Row row
	 * = cursor.getCurrentRow(); DAI dai = new DAI(row);
	 * dai.setType(typeMap.get(row.getInt(DAI.Fields.TYPE.getName())));
	 * listDAI.add(dai); } } } catch (IOException e) {
	 * logger.error("An error occured while reading from file", e); }
	 * logger.debug("Get {} DAI signals", listDAI.size()); return listDAI; }
	 * 
	 * public static Map<Integer, DMC_TYPE> getTypeMap(Database db) throws
	 * IOException{ Map<Integer, DMC_TYPE> typeMap = new HashMap<>(); Table
	 * tableTypeAI = db.getTable(AI.getTableTypeName()); for (Row row :
	 * tableTypeAI){ if (row.getString(TYPE_COLUMN_TYPE_PLC) != null){
	 * typeMap.put(row.getInt(TYPE_COLUMN_CODE),
	 * DMC.DMC_TYPE.valueOf(row.getString(TYPE_COLUMN_TYPE_PLC))); } else {
	 * typeMap.put(row.getInt(TYPE_COLUMN_CODE), DMC.DMC_TYPE.RAW_VALUE); } }
	 * return typeMap; }
	 * 
	 * public static Map<Integer, DAI.Type> getDAITypeMap(Database db) throws
	 * IOException{ Map<Integer, DAI.Type> typeMap = new HashMap<>(); Table
	 * tableTypeDAI = db.getTable(DAI.getTableTypeName()); for (Row row :
	 * tableTypeDAI){ int code = row.getInt(DAI.Type.Fields.CODE.getName());
	 * typeMap.put(code, new DAI.Type(row)); } return typeMap; }
	 * 
	 * public static Map<String, ExternalSystem> getExtSysMap(Database db)
	 * throws IOException{ Map<String, ExternalSystem> extSysMap = new
	 * HashMap<>(); Table tableExtSys =
	 * db.getTable(ExternalSystem.getTableName()); for (Row row : tableExtSys){
	 * ExternalSystem system = new ExternalSystem(row);
	 * extSysMap.put(system.getSystem(), system); } return extSysMap; }
	 */

}
