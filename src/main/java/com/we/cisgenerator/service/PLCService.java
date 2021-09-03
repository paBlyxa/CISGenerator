package com.we.cisgenerator.service;

import com.we.cisgenerator.model.*;
import com.we.cisgenerator.model.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PLCService {

	private final static Logger logger = LoggerFactory.getLogger(PLCService.class);

	private final static String PROGRAM_FILE = "PROGRAM.EXP";
	private final static String IEC60870_PROGRAM_FILE = "IEC60870.EXP";;
	private final static String IEC60870_GV_FILE = "IEC60870_GV.exp";
	//private final static String IEC60870_MAIN_PRG = "IEC60870_MainPrg.exp";
	private final static String TASK_FILE = "TASKCONF.EXP";
	private final static String LIBRARY_FILE = "LIBRARY.EXP";
	private final static String DEF_CHARSET = "windows-1252";

	public static void writeConfig(File file, PLC plc, boolean append) {
		if (!plc.isIecAddressCalculated()) {
			plc.calcIecAddress();
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
			bw.newLine();
			bw.write(
					"PLC_CONFIGURATION\r\n_GLOBAL\r\n_VERSION: 3\r\n_AUTOADR: 1\r\n_CHECKADR: 0\r\n_SAVECONFIGFILESINPROJECT: 0\r\n_END_GLOBAL\r\n");
			bw.newLine();
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'Root\'\r\n_INDEX_IN_PARENT: \'-1\'\r\n_MODULE_NAME: \'PLC Configuration\'\r\n_NODE_ID: -1\r\n_IECIN: %IB0\r\n_IECOUT: %QB0\r\n_IECDIAG: %MB0\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n");
			bw.newLine();
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'K_Bus\'\r\n_INDEX_IN_PARENT: \'1\'\r\n_MODULE_NAME: \'K-Bus\'\r\n_NODE_ID: 0\r\n_IECIN: %IB0\r\n_IECOUT: %QB0\r\n_IECDIAG: %MB0\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'PFC200 Telecontrol (FW03-...)\'\r\n");
			bw.newLine();
			bw.write(
					"_PARAMETER\r\n_PARAM 201019000: 0, \'1\'\r\n_PARAM 201019001: 0, \'10000\'\r\n_PARAM 201019002: 0, \'0\'\r\n_PARAM 201019003: 0, \'1\'\r\n_END_PARAMETER\r\n");

			// Модули контроллера
			int count = 1;
			for (Module m : plc.getModules()) {
				if (m.getType() != Module.TYPE._750_602) {
					// Для модулей типа 750-602 не нужно создавать запись
					bw.write(getStringRepresentation(m, count));
					count++;
				}
			}
			bw.write("_END_MODULE\r\n");

			// Программные модули
			// IEC60870
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'IEC60870_Konfig\'\r\n_INDEX_IN_PARENT: \'2\'\r\n_MODULE_NAME: \'IEC60870-5-Konfig\'\r\n_NODE_ID: 1\r\n_IECIN: %IB10000\r\n"
							+ "_IECOUT: %QB10000\r\n_IECDIAG: %MB10000\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n\r\n");
			// IEC60870 parameters
			bw.write(
					"_PARAMETER\r\n_PARAM 1: 0, \'t#500ms\'\r\n_PARAM 2: 0, \'t#5s\'\r\n_PARAM 3: 0, \'t#2s\'\r\n_PARAM 4: 0, \'32\'\r\n_PARAM 5: 0, \'1\'\r\n"
							+ "_PARAM 6: 0, \'21\'\r\n_PARAM 7: 0, \'t#20ms\'\r\n_PARAM 8: 0, \'22\'\r\n_PARAM 9: 0, \'t#20ms\'\r\n_PARAM 10: 0, \'23\'\r\n_PARAM 11: 0, \'t#20ms\'\r\n"
							+ "_PARAM 12: 0, \'24\'\r\n_PARAM 13: 0, \'t#20ms\'\r\n_PARAM 14: 0, \'1\'\r\n_PARAM 15: 0, \'DEUTSCH\'\r\n_PARAM 16: 0, \'758-8xx\'\r\n_PARAM 17: 0, \'Standard.LIB\'\r\n"
							+ "_PARAM 18: 0, \'SysLibCallback.LIB\'\r\n_PARAM 19: 0, \'SysLibRtc.LIB\'\r\n_PARAM 20: 0, \'SysLibSockets.LIB\'\r\n_PARAM 21: 0, \'SysLibMem.LIB\'\r\n"
							+ "_PARAM 22: 0, \'SysLibTime.LIB\'\r\n_PARAM 23: 0, \'SysLibSem.LIB\'\r\n_PARAM 24: 0, \'SysLibCom.LIB\'\r\n_PARAM 25: 0, \'\'\r\n_PARAM 26: 0, \'\'\r\n"
							+ "_PARAM 27: 0, \'%IB0\'\r\n_PARAM 28: 0, \'%QB0\'\r\n_PARAM 29: 0, \'TRUE\'\r\n_PARAM 30: 0, \'192.53.103.108\'\r\n_PARAM 31: 0, \'t#50ms\'\r\n_PARAM 32: 0, \'60\'\r\n"
							+ "_PARAM 33: 0, \'10\'\r\n_PARAM 34: 0, \'SerComm.LIB\'\r\n_PARAM 35: 0, \'WagoLibEthernet_01.lib\'\r\n_PARAM 36: 0, \'WagoLibSntp.LIB\'\r\n_PARAM 37: 0, \'WagoLibNTP.LIB\'\r\n"
							+ "_PARAM 38: 0, \'WagoLibPing.lib\'\r\n_PARAM 39: 0, \'\'\r\n_PARAM 40: 0, \'\'\r\n_PARAM 41: 0, \'WagoLib60870Base.lib\'\r\n_PARAM 42: 0, \'WagoLib60870Slave.lib\'\r\n"
							+ "_PARAM 43: 0, \'WagoLib60870Master.lib\'\r\n_PARAM 44: 0, \'0\'\r\n_PARAM 45: 0, \'0\'\r\n_PARAM 46: 0, \'0\'\r\n_PARAM 47: 0, \'1000\'\r\n_PARAM 48: 0, \'16\'\r\n"
							+ "_PARAM 49: 0, \'1\'\r\n_PARAM 50: 0, \'0\'\r\n_PARAM 51: 0, \'0\'\r\n_PARAM 52: 0, \'0\'\r\n_PARAM 53: 0, \'0\'\r\n_PARAM 54: 0, \'0\'\r\n_PARAM 55: 0, \'0\'\r\n"
							+ "_PARAM 56: 0, \'0\'\r\n_PARAM 57: 0, \'0\'\r\n_PARAM 58: 0, \'0\'\r\n_PARAM 59: 0, \'0\'\r\n_END_PARAMETER\r\n");
			bw.write("_END_MODULE\r\n\r\n");
			// PFC Modbus
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'PFC200MODBUS\'\r\n_INDEX_IN_PARENT: \'3\'\r\n_MODULE_NAME: \'Modbus variables\'\r\n_NODE_ID: 2\r\n_IECIN: %IB0\r\n_IECOUT: %QB0"
							+ "\r\n_IECDIAG: %MB0\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n\r\n");
			// PFC Modbus parameters
			bw.write(
					"_PARAMETER\r\n_PARAM 2010330101: 0, \'1\'\r\n_PARAM 2010330102: 0, \'1\'\r\n_PARAM 2010330201: 0, \'1\'\r\n_PARAM 2010330202: 0, \'502\'\r\n"
							+ "_PARAM 2010330203: 0, \'600\'\r\n_PARAM 2010330301: 0, \'1\'\r\n_PARAM 2010330302: 0, \'502\'\r\n_PARAM 2010330401: 0, \'0\'\r\n_PARAM 2010330402: 0, \'1\'\r\n"
							+ "_PARAM 2010330403: 0, \'5000\'\r\n_PARAM 2010330404: 0, \'0\'\r\n_PARAM 2010330405: 0, \'115200\'\r\n_PARAM 2010330406: 0, \'1\'\r\n_PARAM 2010330407: 0, \'1\'\r\n"
							+ "_PARAM 2010330408: 0, \'0\'\r\n_PARAM 2010330409: 0, \'0\'\r\n_END_PARAMETER\r\n");
			bw.write("_END_MODULE\r\n\r\n");
			// IEC61058
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'IEC61850_Konfig\'\r\n_INDEX_IN_PARENT: \'4\'\r\n_MODULE_NAME: \'IEC61850-Konfig\'\r\n_NODE_ID: 3\r\n_IECIN: %IB10000\r\n"
							+ "_IECOUT: %QB10000\r\n_IECDIAG: %MB10000\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n\r\n");
			// IEC61058 parameters
			bw.write("_PARAMETER\r\n_PARAM 1: 0, \'DEUTSCH\'\r\n_PARAM 2: 0, \'758-8xx\'\r\n_END_PARAMETER\r\n");
			bw.write("_END_MODULE\r\n\r\n");
			// DNP3
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'DNP3_Konfig\'\r\n_INDEX_IN_PARENT: \'5\'\r\n_MODULE_NAME: \'DNP3-Konfig\'\r\n_NODE_ID: 4\r\n_IECIN: %IB10000"
							+ "_IECOUT: %QB10000\r\n_IECDIAG: %MB10000\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n\r\n");
			// DNP3 parameters
			bw.write("_PARAMETER\r\n_PARAM 1: 0, \'DEUTSCH\'\r\n_PARAM 2: 0, \'758-8xx\'\r\n_END_PARAMETER\r\n");
			bw.write("_END_MODULE\r\n\r\n");
			// MB Master
			bw.write(
					"_MODULE: \'3S\'\r\n_SECTION_NAME: \'MB_MASTER\'\r\n_INDEX_IN_PARENT: \'6\'\r\n_MODULE_NAME: \'Modbus-Master\'\r\n_NODE_ID: 5\r\n_IECIN: %IB0\r\n"
							+ "_IECOUT: %QB0\r\n_IECDIAG: %MB0\r\n_DOWNLOAD: 1\r\n_EXCLUDEFROMAUTOADR: 0\r\n_COMMENT: \'\'\r\n");
			bw.write("_END_MODULE\r\n");

			bw.write("_END_MODULE\r\n");
			bw.write("PLC_END\r\n");
			logger.info("WriteConfig to file \"{}\" is done", file);
		} catch (IOException e) {
			logger.error("An error occured while writing to file", e);
		}
	}

	private static String getStringRepresentation(Module m, int index) {
		StringBuilder str = new StringBuilder("");
		str.append("\r\n_MODULE: \'3S\'\r\n");
		str.append("_SECTION_NAME: \'").append(m.getSectionName()).append("\'\r\n");
		str.append("_INDEX_IN_PARENT: \'").append(index).append("\'\r\n");
		str.append("_MODULE_NAME: \'").append(m.getName()).append("\'\r\n");
		str.append("_NODE_ID: ").append(index - 1).append("\r\n");
		str.append("_IECIN: %IB").append(m.getIecIn()).append("\r\n");
		str.append("_IECOUT: %QB").append(m.getIecOut()).append("\r\n");
		str.append("_IECDIAG: %MB0\r\n");
		str.append("_DOWNLOAD: 1\r\n");
		str.append("_EXCLUDEFROMAUTOADR: 0\r\n");
		str.append("_COMMENT: \'\'\r\n");

		for (int i = 1; i <= m.getChannels(); i++) {
			StringBuilder strCh = new StringBuilder("\r\n_CHANNEL\r\n");
			switch (m.getType()) {
			case DI: // Type_1 - DI
				strCh.append("_SECTION_NAME: \'BOOLOnX_I\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'Ch_").append(i).append(" Digital input\'\r\n");
				strCh.append("_CHANNEL_MODE: \'I\'\r\n");
				strCh.append("_IECADR: %IX").append((m.getIecIn() / 2) + "." + (m.getIecIn() % 2 * 8 + i - 1) + "\r\n");
				break;
			case DO: // Type_10 - DO
				strCh.append("_SECTION_NAME: \'BOOLOnX_Q\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'Ch_").append(i).append(" Digital output\'\r\n");
				strCh.append("_CHANNEL_MODE: \'Q\'\r\n");
				strCh.append("_IECADR: %QX")
						.append((m.getIecOut() / 2) + "." + (m.getIecOut() % 2 * 8 + i - 1) + "\r\n");
				break;
			case AI: // Type_8 - AI
			case AI_PT100: // Type_44 - AI PT100
				strCh.append("_SECTION_NAME: \'WORDOnW_I\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'Ch_").append(i).append(" Input word\'\r\n");
				strCh.append("_CHANNEL_MODE: \'I\'\r\n");
				strCh.append("_IECADR: %IW").append((m.getIecIn() / 2 + i - 1) + "\r\n");

				break;
			case AO: // Type_14 - AO
				strCh.append("_SECTION_NAME: \'WORDOnW_Q\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'Ch_").append(i).append(" Analog output\'\r\n");
				strCh.append("_CHANNEL_MODE: \'Q\'\r\n");
				strCh.append("_IECADR: %QW").append((m.getIecOut() / 2 + i - 1) + "\r\n");
				break;
			case _750_652_8:
			case _750_652_24:
			case _750_652_48: // Type_30 - 750_652
				strCh.append("_SECTION_NAME: \'BYTEOnB_I\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'First Byte of XX\'\r\n");
				strCh.append("_CHANNEL_MODE: \'I\'\r\n");
				strCh.append("_IECADR: %IB").append(m.getIecIn() + "\r\n");
				strCh.append("_END_CHANNEL\r\n\r\n");

				strCh.append("_CHANNEL\r\n");
				strCh.append("_SECTION_NAME: \'BYTEOnB_Q\'\r\n");
				strCh.append("_INDEX_IN_PARENT: \'").append(i + 1).append("\'\r\n");
				strCh.append("_SYMBOLIC_NAME: \'\'\r\n");
				strCh.append("_COMMENT: \'First Byte of XX\'\r\n");
				strCh.append("_CHANNEL_MODE: \'Q\'\r\n");
				strCh.append("_IECADR: %QB").append(m.getIecOut() + "\r\n");
				break;
			case _750_602:
				break;
			default:
				break;
			}
			strCh.append("_END_CHANNEL\r\n");
			str.append(strCh);
		}
		str.append("_END_MODULE\r\n");

		return str.toString();
	}

	public static void writeProgram(PLC plc, File file, List<AI> listAI, boolean append) {
		if (!plc.isIecAddressCalculated()) {
			plc.calcIecAddress();
		}

		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, append), DEF_CHARSET))) {

			// Write projtam ReadDI
			bw.write(ProgramService.createProgramReadDI(plc));

			// Write program "ModbusMaster"
			bw.write(ProgramService.createProgramModbusMaster(plc));

			// Write program for read and write digital signals (Gateway)
			if (plc.getListDAI() != null && !plc.getListDAI().isEmpty()) {
				logger.debug("Write program for read/write digital signals");
				bw.write(ProgramService.createProgramRWDAI(plc));
			}

			// Write all another program
			if (plc.getComParameters().size() > 0) {
				logger.debug("Write program CCD, comParams.size = {}", plc.getComParameters().size());
				writeFromFile(bw, PROGRAM_FILE);
				
				// Write IEC60870 program
				bw.write(ProgramService.createIECServerPRG(plc.getDriverIEC(), listAI));
				writeFromFile(bw, IEC60870_PROGRAM_FILE);
				//Write Global IEC vars
				Map<String, String> changes = new HashMap<>();
				//changes.put("byASDUByte01 := 1", "byASDUByte01 := " + plc.getDriverIEC());
				changes.put("<Parameter Name=\"ASDUAdr1\" Type=\"BYTE\" Val=\"1", "<Parameter Name=\"ASDUAdr1\" Type=\"BYTE\" Val=\"" + plc.getDriverIEC());
				writeFromFileWithChanges(bw, IEC60870_GV_FILE, changes);
				ProgramService.writeIECGlobalVars(bw, plc.getDriverIEC(), listAI);
			}

			// Write libraryes
			writeFromFile(bw, LIBRARY_FILE);
			// Write tasks
			Map<String, String> changes = new HashMap<>();
			boolean mainPlc = (plc.getPosition().charAt(plc.getPosition().length() - 1) - '0') % 2 != 0;
			changes.put("C_MAIN_REZERV_PLC	:	BOOL := TRUE;", "C_MAIN_REZERV_PLC	:	BOOL := " + Boolean.toString(mainPlc).toUpperCase() + ";");
			writeFromFileWithChanges(bw, TASK_FILE, changes);
		} catch (IOException e) {
			logger.error("An error occured while writing to file", e);
		}
	}

	public static void calcComParameters(List<AI> listAI, PLC plc) {
		logger.debug("Calculate com parameters for PLC - {}", plc.getPosition());
		Map<Integer, CCDCom> comParameters = plc.getComParameters();
		for (AI ai : listAI) {
			if ((ai.getComNum() != null) && (ai.getLcdmNum() != null)) {
				int comNum = (int) ai.getComNum();
				CCDCom comParameter = comParameters.get(comNum);
				if (comParameter == null) {
					comParameter = new CCDCom(comNum);
					comParameters.put(comNum, comParameter);
					logger.debug("Put new comParameter in PLC - {}, comNum - {}", plc.getPosition(), comNum);
				}
				int lcdmNum = ai.getLcdmNum();
				LCDM lcdm = comParameter.getLCDMbyAddr(lcdmNum);
				if (lcdm == null) {
					lcdm = comParameter.addnewLCDM(lcdmNum);
				}

				int channelNum = ai.getChannelNum();
				lcdm.setChannelMask((short) (lcdm.getChannelMask() | (1 << (channelNum - 1))));
				DMC channel = lcdm.getDMC(channelNum);
				channel.setType(ai.getType().getDmcType());
				if (ai.getType().isInvers()){
					channel.setSensorMax(ai.getSensorMin());
					channel.setSensorMin(ai.getSensorMax());
				} else {
					channel.setSensorMax(ai.getSensorMax());
					channel.setSensorMin(ai.getSensorMin());
				}
				channel.setRegAddress(ai.getAddress());
				channel.setAdrCompensation(ai.getAdrCompensation());
			}
		}

	}

	public static void writeInitProgram(PLC plc, File file, boolean append) {
		if (plc.getComParameters().size() > 0) {
			try (BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, append), DEF_CHARSET))) {
				bw.write(ProgramService.createInitProgram(plc));
			} catch (IOException e) {
				logger.error("An error occured while writing to file", e);
			}
		}
	}

	public static ObservableList<CCDParameter> getParameterList(PLC plc, Map<Integer, CCDCom> onlineParams) {
		ObservableList<CCDParameter> listParameter = FXCollections.observableArrayList();
		for (int i = 2; i <= 6; i++) {
			CCDCom comParameterOffline = plc.getComParameters().get(i);
			CCDCom comParameterOnline = onlineParams.get(i);
			if (comParameterOffline == null && comParameterOnline == null) {
				continue;
			}
			int offlineCountLCDM = comParameterOffline == null ? 0 : comParameterOffline.getCountLCDM();
			int onlineCountLCDM = comParameterOnline == null ? 0 : comParameterOnline.getCountLCDM();
			int comNumber = comParameterOffline == null ? comParameterOnline.getComNumber()
					: comParameterOffline.getComNumber();
			listParameter.add(new CCDParameter("COM" + comNumber + ".countLCDM", offlineCountLCDM, onlineCountLCDM));
			for (int j = 1; (j <= offlineCountLCDM) || (j <= onlineCountLCDM); j++) {
				LCDM lcdmOffline = (comParameterOffline == null) || (j > offlineCountLCDM) ? null
						: comParameterOffline.getLCDM(j);
				LCDM lcdmOnline = (comParameterOnline == null) || (j > onlineCountLCDM) ? null
						: comParameterOnline.getLCDM(j);
				String str = "COM" + comNumber + ".LCDM" + j;
				int addressOffline = lcdmOffline == null ? 0 : lcdmOffline.getAddress();
				int addressOnline = lcdmOnline == null ? 0 : lcdmOnline.getAddress();
				listParameter.add(new CCDParameter(str + ".address", addressOffline, addressOnline));
				short maskOffline = lcdmOffline == null ? 0 : lcdmOffline.getChannelMask();
				short maskOnline = lcdmOnline == null ? 0 : lcdmOnline.getChannelMask();
				listParameter.add(new CCDParameter(str + ".channelMask", maskOffline & 0xFFFF, maskOnline & 0xFFFF));
				for (int k = 1; k <= 16; k++) {
					DMC dmcOffline = lcdmOffline == null ? null : lcdmOffline.getDMC(k);
					DMC dmcOnline = lcdmOnline == null ? null : lcdmOnline.getDMC(k);
					String str2 = "COM" + comNumber + ".LCDM" + j + ".channel" + k;

					listParameter.add(
							new CCDParameter(str2 + ".regAddress", dmcOffline == null ? 0 : dmcOffline.getRegAddress(),
									dmcOnline == null ? 0 : dmcOnline.getRegAddress()));
					listParameter.add(
							new CCDParameter(str2 + ".sensorMax", dmcOffline == null ? 0 : dmcOffline.getSensorMax(),
									dmcOnline == null ? 0 : dmcOnline.getSensorMax()));
					listParameter.add(
							new CCDParameter(str2 + ".sensorMin", dmcOffline == null ? 0 : dmcOffline.getSensorMin(),
									dmcOnline == null ? 0 : dmcOnline.getSensorMin()));
					listParameter.add(new CCDParameter(str2 + ".type",
							dmcOffline == null ? "RAW_VALUE" : dmcOffline.getType().toString(),
							dmcOnline == null ? "RAW_VALUE" : dmcOnline.getType().toString()));
				}
			}
		}
		return listParameter;
	}
	
	private static void writeFromFile(BufferedWriter bw, String file) throws IOException{
		try  {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new PLCService().getClass().getResourceAsStream("/" + file), DEF_CHARSET));
			String str;
			while ((str = br.readLine()) != null) {
				bw.write(str);
				bw.newLine();
			}
			br.close();
		} catch(NullPointerException e) {
			logger.error("NullPointerException for file {}, ", file);
			throw e;
		}
	}
	
	public static void writeFromFileWithChanges(BufferedWriter bw, String file, Map<String, String> changes) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new PLCService().getClass().getResourceAsStream("/" + file), DEF_CHARSET));
		String str;
		while ((str = br.readLine()) != null) {
			for (Map.Entry<String, String> change : changes.entrySet()){
				if (str.contains(change.getKey())){
					str = str.replaceAll(change.getKey(), change.getValue());
				}
			}
			bw.write(str);
			bw.newLine();
		}
		br.close();
	}

}
