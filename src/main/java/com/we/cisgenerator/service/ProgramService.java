package com.we.cisgenerator.service;

import com.we.cisgenerator.model.*;
import com.we.cisgenerator.model.Module;
import com.we.cisgenerator.model.internal.ModbusJob;
import com.we.cisgenerator.model.internal.ModbusTag;
import com.we.cisgenerator.model.winccoa.SDS_AI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class ProgramService {

	private final static Logger logger = LoggerFactory.getLogger(ProgramService.class);
	
	private final static int NUMBER_IEC_VARS = 50;

	public static String createProgramReadDI(PLC plc) {
		if (!plc.isIecAddressCalculated()) {
			plc.calcIecAddress();
		}
		// Calc count of DI and DQ modules
		int iecInput = Integer.MAX_VALUE;
		int iecOutput = Integer.MAX_VALUE;
		int countDIModule = 0;
		int countDQModule = 0;
		for (Module module : plc.getModules()) {
			if ((module.getType() == Module.TYPE.DI) || (module.getType() == Module.TYPE.AI)
					|| (module.getType() == Module.TYPE.AI_PT100)) {
				if (iecInput > module.getIecIn()) {
					iecInput = module.getIecIn();
				}
				if ((module.getType() == Module.TYPE.AI) || (module.getType() == Module.TYPE.AI_PT100)) {
					countDIModule += module.getIecInSize() / 2;
				} else {
					countDIModule++;
				}
			}
			if (module.getType() == Module.TYPE.DO) {
				if (iecOutput > module.getIecOut()) {
					iecOutput = module.getIecOut();
				}
				countDQModule++;
			}
		}
		// Write program "ReadAIDI"
		StringBuilder str = new StringBuilder(getExportHeadBlock("", "PROGRAM ReadAIDI"));
		str.append("VAR\r\n");
		if ((iecInput == Integer.MAX_VALUE) && (iecOutput == Integer.MAX_VALUE)) {
			str.append("END_VAR\r\n(* @END_DECLARATION := '0' *)\r\n");
		} else {
			if (iecInput < Integer.MAX_VALUE) {
				str.append(
						"\tsaInputModule AT %IW" + (iecInput / 2) + " : ARRAY[1.." + countDIModule + "] OF WORD;\r\n");
			}
			if (iecOutput < Integer.MAX_VALUE) {
				str.append("\tsaOutputModule AT %QW" + (iecOutput / 2) + " : ARRAY[1.." + countDQModule
						+ "] OF WORD;\r\n");
			}
			str.append("\ti : INT;\r\nEND_VAR\r\n");
			str.append("VAR CONSTANT\r\n");
			if (iecInput < Integer.MAX_VALUE) {
				str.append("\tciCountInModule : INT := " + countDIModule + ";\r\n");
			}
			if (iecOutput < Integer.MAX_VALUE) {
				str.append("\tciCountOutModule : INT := " + countDQModule + ";\r\n");
			}
			str.append("END_VAR\r\n(* @END_DECLARATION := \'0\' *)\r\n");

			if (iecInput < Integer.MAX_VALUE) {
				str.append("FOR i := 1 TO ciCountInModule DO\r\n\tboolValueIn[i] := saInputModule[i];\r\nEND_FOR;\r\n");
			}
			if (iecOutput < Integer.MAX_VALUE) {
				str.append("\r\nIF (NOT PLC_PRG.stTimer.Q) THEN\r\n" + "\tFOR i := 1 TO ciCountOutModule DO\r\n"
						+ "\t\tsaOutputModule[i] := boolValueOut[i];\r\n\tEND_FOR;\r\n"
						+ "ELSE\r\n\tFOR i := 1 TO ciCountOutModule DO\r\n" + "\t\tsaOutputModule[i] := 0;\r\n"
						+ "\tEND_FOR;\r\nEND_IF;\r\n");
			}
		}
		// Add AI internal
		/*
		 * List<AI_Internal> listAIInternal = plc.getListAIInernal(); if
		 * (!listAIInternal.isEmpty()){ for (AI_Internal ai : listAIInternal){
		 * str.append("realValue[").append((ai.getAddress() - 12284) / 4);
		 * str.append("].value := "); str.append(ai.getIdent());
		 * str.append("/ 10.0;\r\n");
		 * str.append("realValue[").append((ai.getAddress() - 12284) / 4);
		 * str.append("].stat := "); str.append(0); str.append(";\r\n"); } }
		 */
		str.append(";\r\nEND_PROGRAM\r\n\r\n");
		// Create global variable for ai internal
		/*
		 * if (!listAIInternal.isEmpty()){
		 * str.append("(* @NESTEDCOMMENTS := 'Yes' *)\r\n" +
		 * "(* @GLOBAL_VARIABLE_LIST := 'Global_Variables_Internal' *)\r\n" +
		 * "(* @PATH := '' *)\r\n(* @OBJECTFLAGS := '0, 8' *)\r\n" +
		 * "(* @SYMFILEFLAGS := '2048' *)\r\nVAR_GLOBAL\r\n"); for (AI_Internal
		 * ai : listAIInternal){ str.append("\t"); str.append(ai.getIdent());
		 * str.append(" AT %"); if (ai.getPlc().isInterfaceModule()){
		 * str.append("QW"); int iecIn = 1200 +
		 * ai.getPlc().getModules().get(ai.getModuleNum() - 1).getIecIn();
		 * str.append(iecIn + ai.getChannelNum() - 1); } else {
		 * str.append("IW"); int iecIn =
		 * ai.getPlc().getModules().get(ai.getModuleNum() - 1).getIecIn();
		 * str.append(iecIn + ai.getChannelNum() - 1); }
		 * str.append(":\tINT;\t(* Temp in DAS *)\r\n"); } str.
		 * append("END_VAR\r\n(* @OBJECT_END := 'Global_Variables_Internal' *)\r\n"
		 * + "(* @CONNECTIONS := Global_Variables_Internal\r\n" +
		 * "FILENAME : ''\r\nFILETIME : 0\r\nEXPORT : 0\r\n" +
		 * "NUMOFCONNECTIONS : 0\r\n*)\r\n"); }
		 */
		return str.toString();
	}

	public static String createProgramModbusMaster(PLC plc) {
		// Write program "ModbusMaster"
		StringBuilder str = new StringBuilder(getExportHeadBlock("\\/Couplers", "PROGRAM ModbusMaster"));
		if (!plc.getCouplers().isEmpty()) {
			str.append("VAR CONSTANT\r\n\tciCountCouplers\t\t:\tINT := ");
			str.append(plc.getCouplers().size());
			str.append(
					";\r\nEND_VAR\r\nVAR\r\n\tlaCouplers\t:\tARRAY[1..ciCountCouplers] OF ModbusMasterTCP;\r\nEND_VAR\r\n");
		}
		str.append("(* @END_DECLARATION := '0' *)\r\n\r\n");
		if (!plc.getCouplers().isEmpty()) {
			int readOffset = 0;
			int writeOffset = 0;
			for (int i = 0; i < plc.getCouplers().size(); i++) {
				PLC coupler = plc.getCouplers().get(i);
				if (!coupler.isIecAddressCalculated()) {
					coupler.calcIecAddress();
				}
				int readQuantity = coupler.getIecInSize() / 2;
				int writeQuantity = coupler.getIecOutSize() / 2;
				str.append("laCouplers[").append(i + 1);
				str.append("](isIpAddress:=	\'").append(coupler.getIpAddress()).append("\',\r\n");
				str.append("\t\tiuPort := 502,\r\n\t\tibUnitID := 1,\r\n\t\titResponseTimeout := T#100ms,\r\n"
						+ "\t\titConnectTimeout := T#100ms,\r\n\t\titRequestDelay := T#100ms,\r\n"
						+ "\t\tiiReadAddress := 0,\r\n");
				str.append("\t\tiiReadQuantity := ").append(readQuantity).append(",\r\n");
				str.append("\t\tiiReadOffset := ").append(readOffset);
				str.append(",\r\n\t\tiiWriteAddress := 0,\r\n");
				str.append("\t\tiiWriteQuantity := ").append(writeQuantity).append(",\r\n");
				str.append("\t\tiiWriteOffset := ").append(writeOffset).append(");\r\n");
				readOffset += readQuantity;
				writeOffset += writeQuantity;
			}
		}
		str.append(";\r\nEND_PROGRAM\r\n\r\n");
		return str.toString();
	}

	public static String createProgramRWDAI(PLC plc) {
		StringBuilder modbusPRG = new StringBuilder();
		StringBuilder modbusConf = new StringBuilder(
				getExportHeadBlock("\\/Configuration", "PROGRAM MODBUS_CONFIGURATION"));
		modbusConf.append("VAR\r\nEND_VAR\r\n(* @END_DECLARATION := \'0\' *)\r\n"
				+ "(*\r\n\t<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"yes\"?>\r\n"
				+ "<network xml_structure_version=\"1\" xml_feature_version=\"1\">\r\n"
				+ "\t<generator_settings minRTUTaskCycleTimeMs=\"20\" minEthaskCycleTimeMs=\"20\" ethSlaveMultiplier=\"0.1\" "
				+ "rtuInterfaceMultiplier=\"0.1\" taskGeneration=\"true\"/>\r\n"
				+ "\t<master_interface type=\"eth\" name=\"\" expanded=\"true\">\r\n");
		StringBuilder moveValues = new StringBuilder(getExportHeadBlock("", "PROGRAM MOVE_VALUES"));
		moveValues.append("VAR\r\nEND_VAR\r\n(* @END_DECLARATION := \'0\' *)\r\n");
		StringBuilder moveValuesOut1 = new StringBuilder();
		StringBuilder moveValuesOut2 = new StringBuilder();
		Map<ExternalSystem, List<XR>> mapDAI = new HashMap<>();

		for (XR dai : plc.getListDAI()) {
			List<XR> setDAI = mapDAI.get(dai.getSystem());
			if (setDAI == null) {
				setDAI = new ArrayList<XR>();
				mapDAI.put(dai.getSystem(), setDAI);
			}
			setDAI.add(dai);
		}
		for (ExternalSystem system : mapDAI.keySet()) {

			StringBuilder listInTags = new StringBuilder();
			StringBuilder listOutTags = new StringBuilder();
			StringBuilder zz_listTags = new StringBuilder();
			StringBuilder zz_varList = new StringBuilder();

			modbusConf.append("\t\t<mb_slave comment=\"\" name=\"");
			modbusConf.append(system.getName());
			modbusConf.append("\" expanded=\"true\" type=\"generic\">\r\n");
			modbusConf.append("\t\t\t<mb_connection type=\"eth\" transport=\"TCP\" port=\"502\" ip=\"");
			modbusConf.append(system.getAddressMain());// TODO rezerv
														// address
			modbusConf.append("\" unitid=\"1\" request_delay_ms=\"200\" response_timeout_ms=\"2000\"/>\r\n");
			modbusConf.append("\t\t\t<generic_vars comment=\"\" expanded=\"true\">\r\n");

			modbusPRG.append(getExportHeadBlock("\\/Generated Code\\/Ethernet",
					"PROGRAM MBCFG_" + system.getName() + "(* generated by config one prg for each slave *)"));

			TreeMap<ModbusTag, ModbusTag> tags = new TreeMap<>();
			for (XR dai : mapDAI.get(system)) {
				ModbusTag tagValue = new ModbusTag(dai, dai.getIdent() + "_value", dai.getExternalAddrBit() != null ? PLC_TYPE.INT : dai.getType(), dai.getExternalAddr(),
						true, dai.isOut());
				if (tags.get(tagValue) != null) {
					tags.get(tagValue).addTag(tagValue);
				} else {
					tags.put(tagValue, tagValue);
				}
				logger.trace("Create {}", tagValue);
				if (dai.getExternalAddrStatus() != null) {
					ModbusTag tagStat = new ModbusTag(dai, dai.getIdent() + "_status", dai.getExternalAddrBitStatus() != null ? PLC_TYPE.INT : dai.getStatusType(),
							dai.getExternalAddrStatus(), false, dai.isOut());
					if (tags.get(tagStat) != null) {
						tags.get(tagStat).addTag(tagStat);
					} else {
						tags.put(tagStat, tagStat);
					}
					logger.trace("Create {}", tagStat);
				}
			}

			// PRG MOVE_VALUES
			moveValues.append("IF (MBCFG_").append(system.getName()).append(".MBCFG_Error = MBCFG_NO_ERROR) THEN\r\n");
			// int offsetAddr = 0;
			// Calculate modbus job
			List<ModbusJob> listJobs = new ArrayList<>();
			listJobs.add(new ModbusJob(tags.firstKey().getAddress(), 0, tags.firstKey().getFunction()));
			logger.trace("Create {}", listJobs.get(0));
			int jobIndex = 1;
			int varCount = 0;
			for (ModbusTag tag : tags.values()) {

				if (tag.getFunction() != listJobs.get(jobIndex - 1).getFunction()) {
					listJobs.add(new ModbusJob(tag.getAddress(), tag.getType().getSize(), tag.getFunction()));
					logger.trace("Create {}", listJobs.get(jobIndex));
					jobIndex++;
				}
				int quantity = tag.getAddress() + tag.getType().getSize()
						- listJobs.get(jobIndex - 1).getAddress();
				if (quantity > 125) {
					listJobs.add(new ModbusJob(tag.getAddress(), tag.getType().getSize(), tag.getFunction()));
					logger.trace("Create {}", listJobs.get(jobIndex));
					jobIndex++;
				} else {
					listJobs.get(jobIndex - 1).setQuantity(quantity);
				}

				// Append configutation for this tag
				modbusConf.append("\t\t\t\t<generic_var data_type=\"").append(tag.getType());
				modbusConf.append("\">\r\n");

				modbusConf.append("\t\t\t\t\t<generic_params name=\"").append(tag.getName());
				modbusConf.append("\" comment=\"").append("");
				modbusConf.append("\" expanded=\"false\" mb_iotype=\"unused\" ");
				modbusConf.append("mb_access=\"").append(tag.isOut() ? "wo" : "ro");
				modbusConf.append("\" var_byteorder=\"");
				modbusConf.append(tag.getDAI().getByteOrder());
				modbusConf.append("\"/>\r\n");

				modbusConf.append("\t\t\t\t\t<mb_address explicitAccess=\"false\" ");
				modbusConf.append(tag.isOut() ? "FCRead=\"0\" FCWrite=\"" + (tag.isBool() ? "15" : "16") + "\"" : "FCRead=\"" + (tag.isBool() ? "1" : "3") + "\" FCWrite=\"0\"");
				modbusConf.append("  ReadMBAddress=\"").append(tag.isOut() ? 0 : tag.getAddress());
				modbusConf.append("\" WriteMBAddress=\"").append(tag.isOut() ? tag.getAddress() : 0);
				modbusConf.append("\" ReadBitOffset=\"0\" WriteBitOffset=\"0\"/>\r\n");
				modbusConf.append("\t\t\t\t</generic_var>\r\n");

				// Append tag to program list
				if (tag.isOut()) {
					listOutTags.append("\t").append(tag.getName());
					listOutTags.append("\t:\t").append(tag.getType()).append(";\t(**)\r\n");
				} else {
					listInTags.append("\t").append(tag.getName());
					listInTags.append("\t:\t").append(tag.getType()).append(";\t(**)\r\n");
				}

				// Add tag to zz_list
				zz_listTags.append("\t(\tDataType\t\t:= MBCFG_TYPE_");
				zz_listTags.append(tag.getType());
				zz_listTags.append(",\r\n\t\tByteOrder\t\t:= ");
				zz_listTags.append(tag.getDAI().getByteOrder().getEnumName());
				zz_listTags.append(",\r\n\t\tBitSize\t\t\t:= ");
				zz_listTags.append(tag.isBool() ? 1 : 16 * tag.getType().getSize());
				zz_listTags.append(",\r\n\t\tptVar\t\t\t:= 0,\r\n");
				zz_listTags.append("\t\tReadJobIndex\t:= ").append(tag.isOut() ? 0 : jobIndex);
				int startBitNo = tag.isBool() ?  tag.getAddress() - listJobs.get(jobIndex - 1).getAddress() : 16 * (tag.getAddress() - listJobs.get(jobIndex - 1).getAddress());
				zz_listTags.append(",\r\n\t\tReadStartBitNo\t:= ").append(tag.isOut() ? 0 : startBitNo);
				zz_listTags.append(",\r\n\t\tWriteJobIndex\t:= ").append(tag.isOut() ? jobIndex : 0);
				zz_listTags.append(",\r\n\t\tWriteStartBitNo := ").append(tag.isOut() ? startBitNo : 0);
				zz_listTags.append("),\r\n");

				varCount++;
				zz_varList.append("\tzz_VariableList[");
				zz_varList.append(varCount);
				zz_varList.append("].ptVar := ADR(");
				zz_varList.append(tag.getName());
				zz_varList.append(");\r\n");

				// int sizePLCType = (tag.getType().getSize() + 15) / 16;
				// offsetAddr += sizePLCType;

				// Move values for WinCC OA
				if (!tag.isOut()) {
					moveValues.append(tag.toExportString());
					
				} else {
					moveValuesOut1.append(tag.toExportString());
				}
			}

			// PRG MOVE_VALUES
			moveValues.append("ELSE\r\n");
			boolean empty1 = true;
			for (XR dai : mapDAI.get(system)) {
				if (!dai.isOut()) {
					if ((dai.getType() != PLC_TYPE.BOOL)) {
						empty1 = false;
						int adr2 = (dai.getAddress() - 12284) / 4;
						moveValues.append("realValue[").append(adr2).append("].stat := 1;\r\n");
					}
				}/* else {
					moveValuesOut2.append("MBCFG_").append(dai.getSystem().getName()).append(".").append(dai.getIdent())
							.append("_value := 0;\r\n");
					moveValuesOut2.append("MBCFG_").append(dai.getSystem().getName()).append(".").append(dai.getIdent())
							.append("_status := 1;\r\n");
				}*/
			}
			for (ModbusTag tag : tags.values()){
				if (tag.isOut()){
					if (tag.isValue()){
						moveValuesOut2.append("MBCFG_").append(tag.getDAI().getSystem().getName()).append(".").append(tag.getDAI().getIdent())
							.append("_value := 0;\r\n");
					} else {
						moveValuesOut2.append("MBCFG_").append(tag.getDAI().getSystem().getName()).append(".").append(tag.getDAI().getIdent())
								.append("_status := 1;\r\n");
					}
					
				}
			}
			if (empty1) {
				moveValues.append(";\r\n");
			}
			moveValues.append("END_IF;\r\n");

			// Delete last ","
			zz_listTags.deleteCharAt(zz_listTags.length() - 3);
			zz_listTags.append(";\r\n");

			// **************************************
			// Write MCFG_PRG
			// **************************************

			// Append output tags
			modbusPRG.append("VAR_INPUT\r\n");
			modbusPRG.append(listOutTags);
			modbusPRG.append("\r\nEND_VAR\r\n");

			// Append input tags
			modbusPRG.append("\r\nVAR_OUTPUT\r\n");
			modbusPRG.append(listInTags);

			// Append system variables
			modbusPRG.append("\r\n\t(*--- system variables (read only) ----------------------------------------*)\r\n"
					+ "\tMBCFG_IpAddress     :   STRING(");
			modbusPRG.append(system.getAddressMain().length());
			modbusPRG.append(") := \'");
			modbusPRG.append(system.getAddressMain());
			modbusPRG.append("\';\t(* address of this slave *)\r\n"
					+ "\tMBCFG_Port\t\t\t:\tUINT := 502;\t\t\t\t(* IP - Port *)\r\n"
					+ "\tMBCFG_UnitID\t\t:\tBYTE := 1;\t\t\t(* MODBUS Unit-Id *)\r\n"
					+ "\tMBCFG_TimeOut\t\t:\tTIME := t#2000ms;\t\t(* Response timeout*)\r\n"
					+ "\tMBCFG_RequestDelay\t:\tTIME := t#200ms;\t\t(* 0 means no delay *)\r\n"
					+ "\tMBCFG_Error\t\t\t:\tMBCFG_eERROR := MBCFG_START_UP;\r\n"
					+ "\tMBCFG_LastJob\t\t:\tMBCFG_typCOM_JOB;\r\n"
					+ "\t(*-------------------------------------------------------------------------*)\r\n"
					+ "END_VAR\r\n");
			// Append var constant (deleted {library private})
			modbusPRG.append("\r\n\r\nVAR CONSTANT\r\n\tzz_VARIABLECOUNT:\tINT := ");
			modbusPRG.append(varCount);
			modbusPRG.append("; (* number of variables  *)\r\n" + "\tzz_JOBCOUNT\t\t:\tINT := ");
			modbusPRG.append(listJobs.size());
			modbusPRG.append("; (* number of jobs *)\r\nEND_VAR\r\n");
			// Append vars
			modbusPRG
					.append("VAR\r\n\r\n\t(*=== VARIABLE LIST ===============================================================================*)\r\n"
							+ "\tzz_VariableList :   ARRAY[1..zz_VARIABLECOUNT] OF MBCFG_typVARIABLE :=\r\n");
			modbusPRG.append(zz_listTags);
			modbusPRG.append(
					"\t(*=================================================================================================*)\r\n\r\n");
			// Append job list
			modbusPRG
					.append("\t(*=== JOB LIST ====================================================================================*)\r\n"
							+ "\tzz_JobList\t:\tARRAY[1..zz_JOBCOUNT] OF MBCFG_typCOM_JOB :=\r\n");
			for (ModbusJob job : listJobs) {
				modbusPRG.append(job.toExportString());
			}
			modbusPRG.deleteCharAt(modbusPRG.length() - 3);
			modbusPRG.append(
					";\r\n\t(*=================================================================================================*)\r\n\r\n");
			// Append data fields
			modbusPRG.append(
					"\t(*=== DATA FIELDS =================================================================================*)\r\n");
			jobIndex = 1;
			for (ModbusJob job : listJobs) {
				modbusPRG.append("\tzz_DataField_");
				modbusPRG.append(jobIndex);
				modbusPRG.append(job.isWrite() ? "_Write" : "_Read");
				modbusPRG.append("\t\t:\t\tARRAY[1..");
				modbusPRG.append(job.getQuantity());
				modbusPRG.append("] OF WORD;\r\n");
				jobIndex++;
			}
			modbusPRG.append(
					"\r\n(*=================================================================================================*)\r\n\r\n");
			// Append modbus master
			modbusPRG.append("\t(*=== MODBUS MASTER =====================================*)\r\n"
					+ "\tzz_MBCFG_MASTER_ETH :\tMBCFG_MASTER_TCP;\r\n"
					+ "\t(*=======================================================*)\r\n");
			// Append end declaration
			modbusPRG.append("\r\nEND_VAR\r\n(* @END_DECLARATION := '0' *)\r\n");

			// Body of program
			modbusPRG.append("(*--- for each variable -------------------------*)\r\n");
			modbusPRG.append(zz_varList);
			modbusPRG.append("\r\n(*-----------------------------------------------*)\r\n"
					+ "\r\n(*--- for each job -----------------------------------*)\r\n");
			for (int i = 1; i <= listJobs.size(); i++) {
				modbusPRG.append("\tzz_JobList[");
				modbusPRG.append(i);
				modbusPRG.append("].");
				modbusPRG.append(listJobs.get(i - 1).isWrite() ? "ptWriteData" : "ptReadData");
				modbusPRG.append("   := ADR(zz_DataField_");
				modbusPRG.append(i);
				modbusPRG.append(listJobs.get(i - 1).isWrite() ? "_Write" : "_Read");
				modbusPRG.append(");\r\n");
			}
			modbusPRG.append("\r\n(*----------------------------------------------------*)\r\n\r\n");
			// FIXED CODE
			modbusPRG.append("(*#### START OF FIXED CODE #####################################*)\r\n"
					+ "zz_MBCFG_MASTER_ETH(\tstrIpAddress\t:= MBCFG_IpAddress,\t\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\tuiPort\t\t\t:= MBCFG_Port,\t\t\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\tbUnitID\t\t\t:= MBCFG_UnitID,\t\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\ttTimeOut\t\t:= MBCFG_TimeOut,\t\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\tiVariableCount\t:= zz_VARIABLECOUNT,\r\n"
					+ "\t\t\t\t\t\tptVariableList  := ADR(zz_VariableList),\r\n"
					+ "\t\t\t\t\t\tiJobCount\t\t:= zz_JOBCOUNT,\r\n"
					+ "\t\t\t\t\t\tptJobList\t\t:= ADR(zz_JobList),\r\n"
					+ "\t\t\t\t\t\ttRequestDelay\t:= MBCFG_RequestDelay,\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\teError\t\t\t=> MBCFG_Error,\t\t\t\t\t(* fixed entry     *)\r\n"
					+ "\t\t\t\t\t\tLastJob\t\t\t=> MBCFG_LastJob\t\t\t\t(* fixed entry     *)\r\n" + "\t\t\t\t\t);\r\n"
					+ "(*##############################################################*)\r\n" + "END_PROGRAM\r\n");
			// **************************************
			// End write MCFG_PRG
			// **************************************

			// Close slave in configuration xml file
			modbusConf.append("\t\t\t</generic_vars>\r\n");
			modbusConf.append("\t\t</mb_slave>\r\n");
		}
		// Close modbus configuration xml file
		modbusConf.append("\t</master_interface>\r\n</network>\r\n\r\n*)\r\n;\r\nEND_PROGRAM\r\n\r\n");

		StringBuilder str = new StringBuilder();
		// Write Modbus Configuration in export file
		str.append(modbusConf);
		// Write Modbus PRG
		str.append(modbusPRG);
		// Write Move Values PRG
		str.append(moveValues);
		str.append("\r\nIF (NOT PLC_PRG.stTimer.Q) THEN\r\n");
		str.append(moveValuesOut1);
		str.append("ELSE\r\n");
		str.append(moveValuesOut2);
		str.append("END_IF;\r\n");
		str.append("END_PROGRAM\r\n\r\n");
		// Write Modbus TASK
		str.append("RESOURCE\r\n\r\nTASK MB_ETH_MASTER_TASK (PRIORITY := 26, INTERVAL := T#20ms);\r\n");
		for (ExternalSystem system : mapDAI.keySet()) {
			str.append("MBCFG_" + system.getName() + "();\r\n");
		}
		str.append("MOVE_VALUES();\r\n");
		str.append("{Additional_info : 1,0,0,0,1,4294967295}\r\nEND_TASK\r\nEND_RESOURCE\r\n\r\n");
		return str.toString();
	}

	public static String createInitProgram(PLC plc) {
		StringBuilder str = new StringBuilder(getExportHeadBlock("\\/LCDM", "PROGRAM CCDParamsInit"));
		str.append("VAR\r\nEND_VAR\r\n");
		str.append("(* @END_DECLARATION := '0' *)\r\n");
		for (CCDCom comParameter : plc.getComParameters().values()) {
			str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].countCCD := " + comParameter.getCountLCDM()
					+ ";\r\n");
			for (int i = 0; i < comParameter.getCountLCDM(); i++) {
				LCDM lcdm = comParameter.getLCDM(i + 1);
				str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].address := "
						+ lcdm.getAddress() + ";\r\n");
				str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1)
						+ "].channelMask := 16#" + Integer.toHexString(lcdm.getChannelMask() & 0xFFFF) + ";\r\n");
				str.append("comStatus[" + comParameter.getComNumber() + "].lcdm[" + (i + 1) + "].channelMask := 16#"
						+ Integer.toHexString(lcdm.getChannelMask() & 0xFFFF) + ";\r\n");

				str.append(
						"CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].init := TRUE;\r\n");
				for (int j = 1; j <= 16; j++) {
					DMC dmc = lcdm.getDMC(j);
					str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].channel[" + j
							+ "].regAddress := " + dmc.getRegAddress() + ";\r\n");
					str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].channel[" + j
							+ "].sensorMax := " + dmc.getSensorMax() + ";\r\n");
					str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].channel[" + j
							+ "].sensorMin := " + dmc.getSensorMin() + ";\r\n");
					str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].channel[" + j
							+ "].typeCh := " + dmc.getType() + ";\r\n");
					str.append("CCD_PARAMS[" + comParameter.getComNumber() + "].CCD_PARAM[" + (i + 1) + "].channel[" + j
							+ "].adrCompensation := " + dmc.getAdrCompensation() + ";\r\n");
				}
			}
		}
		str.append("GlobalInit := FALSE;\r\nEND_PROGRAM\r\n");
		return str.toString();
	}

	public static String createIECServerPRG(int driverIEC, List<AI> listAI) {
		StringBuilder str = new StringBuilder(getExportHeadBlock("", "PROGRAM IEC60870_ServerPRG_1_104"));
		str.append("VAR_INPUT\r\n" + "\txActiveClose            : BOOL;\r\n" + "\tdwGroupTrigger          : DWORD;\r\n"
				+ "\tfbOpenLocalSocket		: IEC870_OpenLocalSocketFB := (wPort := 2404);\r\n"
				+ "\t(* ClientConnection function-block(s) for data exchange with Telecontrol system *)\r\n"
				+ "\taSlaveConnection		: ARRAY[1..IEC870_MAX_CLIENTS] OF IEC870_104ClientConnection  := \r\n"
				+ "\t\t\t\t\t\t\t\t(byHerkAdr :=  1, byASDUByte01 := ")
				.append(driverIEC)
				.append(", byASDUByte02 := 0, SendBuffer := (tyDefaultCOT := (byARV := 48), byMaxTlgLen := 253, tonInhibitTime := (PT := t#0ms)),\r\n"
						+ "\t\t\t\t\t\t\t\ttySrvPar := (byCntMode := C870_COUNT_MODE_C, xSelBefExe := FALSE, tSelBefExeTout := t#5s),\r\n"
						+ "\t\t\t\t\t\t\t\tLinkFB := (tParameterT1 := T#15s, tParameterT2 := T#10s, tParameterT3 := T#20s, wParameterW := 8, wParameterK := 12, xSendTestframeAct := TRUE,\r\n"
						+ "\t\t\t\t\t\t\t\tCommunicationFB := (aAcceptedClients := '0.0.0.0', '0.0.0.0', '0.0.0.0'))),\r\n"
						+ "\t\t\t\t\t\t\t\t(byHerkAdr :=  1, byASDUByte01 := ")
				.append(driverIEC)
				.append(", byASDUByte02 := 0, SendBuffer := (tyDefaultCOT := (byARV := 48), byMaxTlgLen := 253, tonInhibitTime := (PT := t#0ms)),\r\n"
						+ "\t\t\t\t\t\t\t\ttySrvPar := (byCntMode := C870_COUNT_MODE_C, xSelBefExe := FALSE, tSelBefExeTout := t#5s),\r\n"
						+ "\t\t\t\t\t\t\t\tLinkFB := (tParameterT1 := T#15s, tParameterT2 := T#10s, tParameterT3 := T#20s, wParameterW := 8, wParameterK := 12, xSendTestframeAct := TRUE,\r\n"
						+ "\t\t\t\t\t\t\t\tCommunicationFB := (aAcceptedClients := '0.0.0.0', '0.0.0.0', '0.0.0.0')));\r\n\r\n")
				.append("\t(* Information-object function-block's *)\r\n");
		Map<Integer, AI> mapAI = new HashMap<>();
		for (AI ai : listAI) {
			if (ai.isFast()) {
				mapAI.put(ai.getAddress(), ai);
			}
		}
		// Float values
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			float hyst = 0.03f;
			int adr = SDS_AI.FIRST_IEC_ADDRESS + (i - 1) * 4;
			if (mapAI.containsKey(adr)) {
				hyst = mapAI.get(adr).getSensorMax() / 4000;
			}
			str.append("\tINFO_").append(String.format("%03d", i)).append("_36M_ME_TF")
					.append("\t\t: IEC870_36M_ME_TF\t\t:= (byAdr1 := ").append(i)
					.append(", byAdr2 := 0, byAdr3 := 0, tRepeatTime := t#10m, byHystMode := 1, rHysterese := ")
					.append(hyst).append(", rOffset := 0.0, rFaktor := 1.0);\r\n");
		}
		// Status values (int)
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			str.append("\tINFO_").append(String.format("%03d", i + 50)).append("_10M_ME_TA")
					.append("\t\t: IEC870_10M_ME_TA\t\t:= (byAdr1 := ").append(i)
					.append(", byAdr2 := 0, byAdr3 := 0, tRepeatTime := t#10m, diHysterese := 1, byHystMode := 1, diNormOffset := 0, diNormDivisor := 1);\r\n");
		}
		str.append(
				"\r\nEND_VAR\r\nVAR\r\n\txInitDone\t\t\t\t: BOOL; (* Var's to control the software initalisation after system startup *)\r\n"
						+ "\tbyVisuSelectedLog       : BYTE := 1; (* Var to control the Slaveconnection selektion in Visualisation *)\r\n"
						+ "\tInitFB\t\t\t\t\t: IEC870_InitInfObj;\r\n"
						+ "\tIEC870InfObj\t\t\t: IEC870_InfObjListe; (* List with all Info-Objecte FB's as input for the ClientConnection function-block *)\r\n"
						+ "\tdwSemHandle\t\t\t\t: DWORD := 16#FFFFFFFF; (* Handle of the semaphore *)\r\n"
						+ "\tn\t\t\t\t\t\t: INT; (* local counter var *)\r\n" + "\tdwGroupTrigger_         : DWORD;\r\n"
						+ "\tdwGroupToSend           : DWORD;\r\n" + "\ttonActiveClose          : TON;\r\n"
						+ "\tnCloseState             : INT;\r\n" + "END_VAR\r\n(* @END_DECLARATION := '0' *)\r\n"
						+ "IF NOT(xInitDone) THEN\r\n\tIF NOT(gxIEC870_Enable) THEN\t\tRETURN;\tEND_IF"
						+ "\tInitAction(); (* call the InitAction of this POU once after system startup *)\r\n"
						+ "\txInitDone := TRUE;\r\nEND_IF\r\n\r\nActiveExit(xActiveClose := );\r\n\r\n"
						+ "(* POU to init and open the local Socket *)\r\nfbOpenLocalSocket();\r\n"
						+ "IF NOT(fbOpenLocalSocket.xSocketOpen) THEN\r\n\tRETURN; (* return from this POU if socket is not open *)\r\nEND_IF\r\n\r\n"
						+ "(* call ClientConnection function-block(s) for data exchange with Telecontrol system *)\r\n"
						+ "FOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n\taSlaveConnection[n](nClientIx := n,\r\n"
						+ "\t\t\t\t\t\tdiLocalSocket := fbOpenLocalSocket.diLocalSocket,\r\n"
						+ "\t\t\t\t\t\tdwSemHandle := dwSemHandle,\r\n\t\t\t\t\t\tIEC870InfObj := IEC870InfObj,\r\n"
						+ "\t\t\t\t\t\txLinkOk => statusLinkLayer[n]);\r\nEND_FOR\r\n\r\n"
						+ "dwGroupToSend := (dwGroupTrigger XOR dwGroupTrigger_) AND dwGroupTrigger;\r\n"
						+ "dwGroupTrigger_ := dwGroupTrigger;\r\n\r\n");

		// Call every FB instant
		str.append("(* call every FB instanz for all used Inf.Object function-blocks *)\r\n");
		// Float values
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			str.append("INFO_").append(String.format("%03d", i)).append("_36M_ME_TF((*byAdr1 := ").append(i)
					.append(", byAdr2 := 0, byAdr3 := 0*)\r\n\t\t\t\t\trAnalogVal := realValue[")
					.append(String.format("%03d", 400 + i)).append("].value,\r\n")
					.append("\t\t\t\t\txQuOV := ,\r\n\t\t\t\t\txQuBL := ,\r\n\t\t\t\t\txQuSB := ,\r\n"
							+ "\t\t\t\t\txQuNT := ,\r\n\t\t\t\t\txQuIV := ,\r\n"
							+ "\t\t\t\t\tpFbArchivME := ,\r\n\t\t\t\t\txSendTrig := );\r\n\r\n");
		}
		// Status values (int)
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			str.append("INFO_").append(String.format("%03d", NUMBER_IEC_VARS + i)).append("_10M_ME_TA((*byAdr1 := ")
					.append(i).append(", byAdr2 := 0, byAdr3 := 0*)\r\n\t\t\t\t\tiAnalogVal := realValue[")
					.append(String.format("%03d", 400 + i)).append("].stat,\r\n")
					.append("\t\t\t\t\txQuOV := ,\r\n\t\t\t\t\txQuBL := ,\r\n\t\t\t\t\txQuSB := ,\r\n"
							+ "\t\t\t\t\txQuNT := ,\r\n\t\t\t\t\txQuIV := ,\r\n"
							+ "\t\t\t\t\tpFbArchivME := ,\r\n\t\t\t\t\txSendTrig := );\r\n\r\n");
		}
		str.append("END_PROGRAM\r\n");
		// ACTION ActiveExit
		str.append("ACTION	ActiveExit:\r\nCASE (nCloseState) OF\r\n"
				+ "\t0: (* Stop sending I-Frames for all conncetions *)\r\n" + "\t\tIF (xActiveClose) THEN\r\n"
				+ "\t\t\tFOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n"
				+ "\t\t\t\taSlaveConnection[n].LinkFB.xActiveClose := TRUE;\r\n"
				+ "\t\t\tEND_FOR\r\n\t\t\tnCloseState := 1;\r\n"
				+ "\t\t\ttonActiveClose(IN := FALSE);\r\n\t\tEND_IF\r\n\r\n"
				+ "\t1: (* Check all connections whether there is something to do for close *)\r\n"
				+ "\t\tnCloseState := 2;\r\n\t\ttonActiveClose(IN := TRUE, PT := t#15s);\r\n"
				+ "\t\tIF (tonActiveClose.Q = FALSE) THEN\r\n" + "\t\t\tFOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n"
				+ "\t\t\t(* Check all connections if an acknowledgment is necessary *)\r\n"
				+ "\t\t\t\tIF (aSlaveConnection[n].LinkFB.nCounterSendSFrame < aSlaveConnection[n].LinkFB.wParameterW) THEN\r\n"
				+ "\t\t\t\t\taSlaveConnection[n].LinkFB.Send_S_Frame(SendBuffer := aSlaveConnection[n].SendBuffer);\r\n"
				+ "\t\t\t\tEND_IF\r\n"
				+ "\t\t\t\t(* Check all connections if transmission and reception number are qual *)\r\n"
				+ "\t\t\t\tIF (aSlaveConnection[n].LinkFB.wClEmpfangsFolgeNr <> aSlaveConnection[n].LinkFB.wSvSendeTelegrammNr) THEN\r\n"
				+ "\t\t\t\t\tnCloseState := 1;\r\n\t\t\t\tEND_IF\r\n\t\t\tEND_FOR\r\n\t\tEND_IF\r\n\r\n"
				+ "\t2: (* Close all conncetions *)\r\n\t\tExitAction();\r\n"
				+ "\t\tfbOpenLocalSocket.xActiveClose := TRUE;\r\n"
				+ "\t\tnCloseState := 99;\r\n\r\n\tELSE\r\n\t\tIF (xActiveClose = FALSE) THEN\r\n"
				+ "\t\t\tFOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n"
				+ "\t\t\t\taSlaveConnection[n].LinkFB.xActiveClose := FALSE;\r\n\t\t\tEND_FOR\r\n"
				+ "\t\t\tfbOpenLocalSocket.xActiveClose := FALSE;\r\n\t\t\tnCloseState := 0;\r\n\t\tEND_IF\r\n"
				+ "END_CASE\r\nEND_ACTION\r\n\r\n");
		// ACTION ExitAction
		str.append("ACTION	ExitAction:\r\nFOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n"
				+ "\taSlaveConnection[n].LinkFB.CommunicationFB.Close();\r\nEND_FOR\r\n\r\n"
				+ "fbOpenLocalSocket.Close();\r\nEND_ACTION\r\n\r\n");
		// ACTION InitAction
		str.append("ACTION	InitAction:\r\n(* fist of all init the Overall exchange data-struct *)\r\n"
				+ "gxIEC870IniReturn := INI(IEC870InfObj, TRUE);\r\n\r\n"
				+ "(* write a part of the INPUT parameter for InitFB POU *)\r\n"
				+ "FOR n := 1 TO IEC870_MAX_CLIENTS DO\r\n"
				+ "\tInitFB.aClientTab[n] := ADR(aSlaveConnection[n].SendBuffer);\r\n"
				+ "END_FOR\r\nInitFB.tySrvPar := aSlaveConnection[1].tySrvPar;\r\n"
				+ "InitFB.pIEC870InfObj := ADR(IEC870InfObj);\r\n\r\n"
				+ "(* Init message buffer before using *)\r\nInitFB.InitTlgBuffer();\r\n\r\n"
				+ "dwSemHandle := SysSemCreate(TRUE);\r\n\r\n");
		// (* put the Pointer to every Inf.Object function-blocks in the
		// exchange data-struct *)
		str.append("(* put the Pointer to every Inf.Object function-blocks in the exchange data-struct *)\r\n");
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			str.append("InitFB(pInfObj := ADR(INFO_").append(String.format("%03d", i))
					.append("_36M_ME_TF), byTlgTyp := IEC870_TLG_TYP_HST);\r\n");
		}
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			str.append("InitFB(pInfObj := ADR(INFO_").append(String.format("%03d", NUMBER_IEC_VARS + i))
					.append("_10M_ME_TA), byTlgTyp := IEC870_TLG_TYP_HST);\r\n");
		}
		str.append("END_ACTION\r\n");
		return str.toString();
	}

	public static void writeIECGlobalVars(BufferedWriter bw, int driverIEC, List<AI> listAI) throws IOException {

		Map<Integer, AI> mapAI = new HashMap<>();
		for (AI ai : listAI) {
			if (ai.isFast()) {
				mapAI.put(ai.getAddress(), ai);
			}
		}
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			StringBuilder str = new StringBuilder();
			float hyst = 0.1f;
			int adr = SDS_AI.FIRST_IEC_ADDRESS + (i - 1) * 4;
			if (mapAI.containsKey(adr)) {
				hyst = mapAI.get(adr).getSensorMax() / 1000;
			}
			str.append("      <CObjectServer Name=\"IObj36M_ME_TF\">\r\n" + "\t\t<Text>IObj36M_ME_TF (0.0.").append(i)
					.append(" = ").append(i).append(")</Text>\r\n")
					.append("\t\t<Parameter Name=\"Comment\" Type=\"STRING\" Val=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"InfoObjAdr1\" Type=\"BYTE\" Val=\"")
					.append(i)
					.append("\" />\r\n" + "\t\t<Parameter Name=\"InfoObjAdr2\" Type=\"BYTE\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"InfoObjAdr3\" Type=\"BYTE\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"RepeatTime\" Type=\"TIME\" Val=\"t#10m\" />\r\n"
							+ "\t\t<Parameter Name=\"LinkFailure\" Type=\"BOOL\" Val=\"TRUE\" />\r\n"
							+ "\t\t<Parameter Name=\"GARequest\" Type=\"ENUM_GA\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"ObjectGroup\" Type=\"ENUM_GROUP\" Val=\"255\" />\r\n"
							+ "\t\t<Parameter Name=\"HystereseReal\" Type=\"REAL\" Val=\"")
					.append(hyst).append("\" />\r\n")
					.append("\t\t<Parameter Name=\"HystereseType\" Type=\"ENUM_HYSTERESE\" Val=\"1\" />\r\n"
							+ "\t\t<Parameter Name=\"RealOffset\" Type=\"REAL\" Val=\"0.0\" />\r\n"
							+ "\t\t<Parameter Name=\"RealFaktor\" Type=\"REAL\" Val=\"1.0\" />\r\n"
							+ "\t\t<Channel Name=\"IEE754\" Type=\"REAL\" Autoapply=\"False\" Assignment=\"realValue[")
					.append(400 + i).append("].value\" />\r\n")
					.append("\t\t<Channel Name=\"OV\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"BL\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"SB\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"NT\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"IV\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"ObjectTime_Select\" Type=\"ENUM_OBJECT_TIME\" Val=\"0\" />\r\n"
							+ "\t\t<Channel Name=\"ObjectTime_Var_CP56\" Type=\"IEC870_CP56\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_Choice\" Type=\"ENUM_SignalPreprocessArchive\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_Timebase\" Type=\"Time\" Val=\"t#15m\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_COT_Label\" Type=\"\" Val=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_FVA\" Type=\"ENUM_AlarmModem_FVA\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_Min_FVA\" Type=\"REAL\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_Max_FVA\" Type=\"REAL\" Val=\"0\" />\r\n"
							+ "      </CObjectServer>\r\n");
			bw.write(str.toString());
		}
		for (int i = 1; i <= NUMBER_IEC_VARS; i++) {
			StringBuilder str = new StringBuilder();
			str.append("      <CObjectServer Name=\"IObj10M_ME_TA\">\r\n" + "\t\t<Text>IObj10M_ME_TA (0.0.").append(i)
					.append(" = ").append(i).append(")</Text>\r\n")
					.append("\t\t<Parameter Name=\"Comment\" Type=\"STRING\" Val=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"InfoObjAdr1\" Type=\"BYTE\" Val=\"")
					.append(i)
					.append("\" />\r\n" + "\t\t<Parameter Name=\"InfoObjAdr2\" Type=\"BYTE\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"InfoObjAdr3\" Type=\"BYTE\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"RepeatTime\" Type=\"TIME\" Val=\"t#10m\" />\r\n"
							+ "\t\t<Parameter Name=\"LinkFailure\" Type=\"BOOL\" Val=\"TRUE\" />\r\n"
							+ "\t\t<Parameter Name=\"GARequest\" Type=\"ENUM_GA\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"ObjectGroup\" Type=\"ENUM_GROUP\" Val=\"255\" />\r\n"
							+ "\t\t<Parameter Name=\"Hysterese\" Type=\"DINT\" Val=\"1\" />\r\n"
							+ "\t\t<Parameter Name=\"HystereseType\" Type=\"ENUM_HYSTERESE\" Val=\"1\" />\r\n"
							+ "\t\t<Parameter Name=\"NormierungOffset\" Type=\"DINT\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"NormierungDivisor\" Type=\"DINT\" Val=\"1\" />\r\n"
							+ "\t\t<Channel Name=\"NVA_INT\" Type=\"INT\" Autoapply=\"False\" Assignment=\"realValue[")
					.append(400 + i).append("].stat\" />\r\n")
					.append("\t\t<Channel Name=\"OV\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"BL\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"SB\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"NT\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Channel Name=\"IV\" Type=\"BOOL\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"ObjectTime_Select\" Type=\"ENUM_OBJECT_TIME\" Val=\"0\" />\r\n"
							+ "\t\t<Channel Name=\"ObjectTime_Var_CP56\" Type=\"IEC870_CP56\" Autoapply=\"True\" Assignment=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_Choice\" Type=\"ENUM_SignalPreprocessArchive\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_Timebase\" Type=\"Time\" Val=\"t#15m\" />\r\n"
							+ "\t\t<Parameter Name=\"SignalPreprocessArchive_COT_Label\" Type=\"\" Val=\"\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_NVA\" Type=\"ENUM_AlarmModem_NVA\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_Min_NVA\" Type=\"INT\" Val=\"0\" />\r\n"
							+ "\t\t<Parameter Name=\"AlarmModem_Max_NVA\" Type=\"INT\" Val=\"0\" />\r\n"
							+ "      </CObjectServer>\r\n");
			bw.write(str.toString());
			bw.write("    </CServer104>\r\n  </CConfiguration>\r\n</IEC60870ProjectDescription>*)\r\n\r\n"
					+ "(* @OBJECT_END := 'IEC 60870 Generated Globale_Variablen' *)\r\n"
					+ "(* @CONNECTIONS := IEC 60870 Generated Globale_Variablen\r\n"
					+ "FILENAME : ''\r\nFILETIME : 0\r\nEXPORT : 0\r\nNUMOFCONNECTIONS : 0\r\n*)\r\n\r\n");
		}
	}

	public static String getExportHeadBlock(String path, String name) {
		StringBuilder str = new StringBuilder("\r\n(* @NESTEDCOMMENTS := 'Yes' *)\r\n(* @PATH := '");
		str.append(path);
		str.append("' *)\r\n(* @OBJECTFLAGS := '0, 8' *)\r\n(* @SYMFILEFLAGS := '2048' *)\r\n");
		str.append(name);
		str.append("\r\n\r\n");
		return str.toString();
	}
}
