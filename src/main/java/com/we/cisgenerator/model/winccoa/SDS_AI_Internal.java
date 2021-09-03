package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.AI_Internal;
import com.we.cisgenerator.model.winccoa.ascii.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_AI_Internal extends AI_Internal implements Dp {

	private final static String TYPE_NAME = "SDS_intAI";
	
	//private final static int FIRST_IEC_ADDRESS = 13888;
	private final static String MIN_VALUE = "-1.7976931348623e+308";
	private final static String MAX_VALUE = "1.7976931348623e+308";
	
	private String exportName = null;
	
	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		if (exportName == null){
			exportName = "ASC (1)/0\t" + getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch(field){
		case AlertValue:
			// TODO
			// Если пределы не заданы то снять бит активности, я так думаю следующий столбец за значениями
			StringBuilder strAlertValue = new StringBuilder();
			strAlertValue.append(exportName + "\t13\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027 \"\"\t\\0\t\t\t1\t1\t\t\t\t0"
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\t\t\t\t\t\t\t\t\r\n");
			// Emergency low
			double emerglowLimit = getEmergLow();
			strAlertValue.append(exportName + "1\t4\t" + MIN_VALUE
						+ "\t" + emerglowLimit
						+ "\t0\t1\t\t\t\t\talert.\tlt:1 LANG:10027 \"Нижний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
						+ MIN_VALUE + "\t" + emerglowLimit
						+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			// Warning low
			double warnlowLimit = getWarnLow();
			strAlertValue.append(exportName + "2\t4\t" + emerglowLimit + "\t" + warnlowLimit
								+ "\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Нижний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
								+ emerglowLimit + "\t" + warnlowLimit
								+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			// Emerg high
			double emerghighLimit = getEmergHigh();
			String emergHighStr = exportName + "5\t4\t" + emerghighLimit + "\t"
						+ MAX_VALUE
						+ "\t0\t0\t\t\t\t\talert.\tlt:1 LANG:10027 \"Верхний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
						+ emerghighLimit + "\t" + MAX_VALUE
						+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n";
			// Warning high
			double warnhighLimit = getWarnHigh();
			String warnHighStr = exportName + "4\t4\t" + warnhighLimit + "\t"
						+ emerghighLimit
						+ "\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Верхний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
						+ warnhighLimit + "\t" + emerghighLimit
						+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n";
			// Norm value
			strAlertValue.append(exportName + "3\t4\t" + warnlowLimit + "\t"
					+ warnhighLimit + "\t0\t1\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t" + warnlowLimit
					+ "\t" + warnhighLimit + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			strAlertValue.append(warnHighStr);
			strAlertValue.append(emergHighStr);
			// Quality
			//strAlertValue.append("ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t\t13\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027 \"\"\t\\0\t\t\t1\t1\t\t\t\t0\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\r\n"
			//		+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t1\t5\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"Норма\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"*\"\t\r\n"
			//		+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t2\t5\t\t\t\t\t\t\t\t\tdanger.\tlt:1 LANG:10027 \"Неисправность\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"1\"\t\r\n"
			//		+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t3\t5\t\t\t\t\t\t\t\t\tdanger.\tlt:1 LANG:10027 \"Обрыв верхний\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"3\"\t\r\n"
			//		+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t4\t5\t\t\t\t\t\t\t\t\tdanger.\tlt:1 LANG:10027 \"Обрыв нижний\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"9\"\t\r\n");
						
			return strAlertValue.toString();
		case Aliases:
			return getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%s@\"\r\n" + getIdent()
				+ ".val\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%" + getFormat() + "@" + (getUnits()) + "\"\r\n";
		case Datapoint:
			return getIdent() + "\t" + TYPE_NAME + "\t" + ("") + "\r\n";
		case DbArchiveInfo:
			ExportField<DBArchiveInfoE> archiveInfo = new ExportField<>(DBArchiveInfoE.values().length);
			archiveInfo.setElement(DBArchiveInfoE.ElementName, getIdent() + ".val")
				.setElement(DBArchiveInfoE.TypeName, TYPE_NAME)
				.setElement(DBArchiveInfoE._archive_type, "45")
				.setElement(DBArchiveInfoE._archive_archive, "1");
			str.append(archiveInfo.toExportString());
			archiveInfo.setElement(DBArchiveInfoE.DetailNr, "1")
				.setElement(DBArchiveInfoE._archive_type, "15")
				.setElement(DBArchiveInfoE._archive_archive, "")
				.setElement(DBArchiveInfoE._archive_class, "_ValueArchive_1")
				.setElement(DBArchiveInfoE._archive_interv, "0")
				.setElement(DBArchiveInfoE._archive_interv_type, "0")
				.setElement(DBArchiveInfoE._archive_std_type, "0")
				.setElement(DBArchiveInfoE._archive_std_tol, "0")
				.setElement(DBArchiveInfoE._archive_std_time, "01.01.1970 00:00:00.000")
				.setElement(DBArchiveInfoE._archive_round_val, "0")
				.setElement(DBArchiveInfoE._archive_round_inv, "0");
			str.append(archiveInfo.toExportString());
			return str.toString();
			//return exportName + "\t45\t1\r\n"
			//		+ exportName + "1\t3\t\t_ValueArchive_1\t0\t0\t5\t0\t01.01.1970 00:00:00.050\t0\t0\r\n";
		case DistributionInfo:
			ExportField<DistributionInfoE> distrInfo = new ExportField<>(DistributionInfoE.values().length);
			distrInfo.setElement(DistributionInfoE.TypeName, TYPE_NAME)
				.setElement(DistributionInfoE.ElementName, getIdent() + ".val")
				.setElement(DistributionInfoE._distrib_type, "56")
				.setElement(DistributionInfoE._distrib_driver, "\\1");
			return distrInfo.toExportString();
		case DpDefaultValue:
			return "";
		case DpFunction:
			return "";
		case DpType:
			return "SDS_intAI.SDS_intAI\t1#1\r\n"
			+ "\tinfo\t1#2\r\n"
				+ "\t\ttype\t25#3\r\n"
				+ "\t\tsubtype\t25#4\r\n"
				+ "\t\tsystem\t25#11\r\n"
				+ "\t\tremarks\t25#13\r\n"
			+ "\thwinfo\t1#5\r\n"
				+ "\t\tfreq\t19#6\r\n"
				+ "\t\tchan\t25#8\r\n"
				+ "\t\tchas\t25#9\r\n"
				+ "\t\tcab\t25#10\r\n"
				+ "\t\tunit\t25#14\r\n"
			+ "\tval\t22#7\r\n";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			return // Value
					//exportName + 0
					//+ dateTimeStr  +
					// Cabinet
					"ASC (1)/0\t" + getIdent() + ".hwinfo.cab\t" + TYPE_NAME + "\t"
					+ getPlc().getCabinet().getName() + dateTimeStr
					// Number LCDM
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.chas\t" + TYPE_NAME + "\t" + getModuleNum() + dateTimeStr
					// Number Channel
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + getChannelNum() + dateTimeStr
					// Unit
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.unit\t" + TYPE_NAME + "\t" + getUnits() + dateTimeStr
					// Freq
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + getPeriod().getName() + dateTimeStr
					// Info System
					+ "ASC (1)/0\t" + getIdent() + ".info.system\t" + TYPE_NAME + "\t" + "ИВС" + dateTimeStr
					// Info Subtyp
					/*+ "ASC (1)/0\t" + getIdent() + ".info.subtype\tSDS_AI\t" + getType().getDmcType().getValue()
					+ "\t0x8300000000000001\t" + dateTimeStr + "\r\n"*/;
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, getIdent() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_reference, "\"M." + getPlc().getDriverModbus() + ".3." + getAddress() + "\"")
					.setElement(PeriphAddrE._address_poll_group, getPeriod().getNameGroup())
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_direction, "\\4")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "0")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_datatype, "561")
					.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			return pAddr.toExportString();
			//if (getAddress() < FIRST_IEC_ADDRESS){
			//int numDriver = getPlc().getDriverModbus();
			//return exportName + "16\t\"M." + numDriver + ".3." + getAddress() + "\"\t" + "_1sec"
			//		+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t566\t\"MODBUS\"\r\n"
			//		+ "ASC (1)/0\t" + getIdent() + ".qual\tSDS_AI\t16\t\"M." + numDriver + ".3." + (getAddress() + 2) + "\"\t" + "_1sec"
			//		+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t561\t\"MODBUS\"\r\n";
			//else {
			//	return exportName + "16\t\"IECTARGET-36.0." + getPlc().getDriverIEC() + ".0.0." + getIecAddress() + "\"\t\t\t0\t0\t\\2\t0\t0"
			//			+ "\t" + getIecAddress() + "\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t526\t\"IEC\"\r\n";
			//}
		case PvssRangeCheck:
			/*ExportField<RangeCheckE> rangeCheck = new ExportField<>(RangeCheckE.values().length);
			rangeCheck.setElement(RangeCheckE.TypeName, TYPE_NAME)
					.setElement(RangeCheckE.ElementName, getIdent() + ".val")
					.setElement(RangeCheckE._pv_range_type, "7")
					.setElement(RangeCheckE._pv_range_ignor_inv, "0")
					.setElement(RangeCheckE._pv_range_neg, "0")
					.setElement(RangeCheckE._pv_range_min, Float.toString(getSensorMin()))
					.setElement(RangeCheckE._pv_range_max, Float.toString(getSensorMax()))
					.setElement(RangeCheckE._pv_range_incl_min, "1")
					.setElement(RangeCheckE._pv_range_incl_max, "1");
			return rangeCheck.toExportString()*/
			return "";
		case DpConvRawToIngMain:
			ExportField<DpConvE> dpConv = new ExportField<>(DpConvE.values().length);
			dpConv.setElement(DpConvE.TypeName, TYPE_NAME)
				.setElement(DpConvE.ElementName, getIdent() + ".val")
				.setElement(DpConvE._msg_conv_type, "33");
			str.append(dpConv.toExportString());
			dpConv.setElement(DpConvE.DetailNr, "1")
				.setElement(DpConvE._msg_conv_type, "2")
				.setElement(DpConvE._msg_conv_poly_grade, "1")
				.setElement(DpConvE._msg_conv_poly_a, getPolinomA().toString())
				.setElement(DpConvE._msg_conv_poly_b, getPolinomB().toString())
				.setElement(DpConvE._msg_conv_poly_c, getPolinomC().toString())
				.setElement(DpConvE._msg_conv_poly_d, "0")
				.setElement(DpConvE._msg_conv_poly_e, "0");
			str.append(dpConv.toExportString());
			return str.toString();
		default:
			break;
		}
		return null;
	}

	@Override
	public String getDpName() {
		return getIdent();
	}
	
	@Override
	public String getSDSType(){
		return TYPE_NAME;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return getIdent() != null;
	}

}
