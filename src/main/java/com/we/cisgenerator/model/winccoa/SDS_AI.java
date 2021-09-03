package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.AI;
import com.we.cisgenerator.model.winccoa.ascii.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_AI extends AI implements Dp {

	public final static int FIRST_IEC_ADDRESS = 13888;
	private final static String MIN_VALUE = "-1.7976931348623e+308";
	private final static String MAX_VALUE = "1.7976931348623e+308";
	private final static String TYPE_NAME = "SDS_AI";

	private final Integer id;
	private String exportName = null;

//	public SDS_AI() {
//
//	}

	public SDS_AI(String dpName, Integer id) {
		super(dpName);
		this.id = id;
	}

	@Override
	public String getExportField(AsciiExportField field) {
		if (exportName == null) {
			exportName = "ASC (1)/0\t" + getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch (field) {
		case AlertValue:
			// TODO
			// Если пределы не заданы то снять бит активности, я так думаю
			// следующий столбец за значениями
			StringBuilder strAlertValue = new StringBuilder();
			strAlertValue.append(exportName + "\t13\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027 \"\"\t\\0\t\t\t1\t0\t\t\t\t0"
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\t\t\t\t\t\t\t\t\r\n");
			// Emergency low
			strAlertValue.append(exportName + "1\t4\t" + MIN_VALUE + "\t" + getEmergLow()
					+ "\t0\t1\t\t\t\t\talert.\tlt:1 LANG:10027 \"Нижний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
					+ MIN_VALUE + "\t" + getEmergLow()
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			// Warning low
			strAlertValue.append(exportName + "2\t4\t" + getEmergLow() + "\t" + getWarnLow()
					+ "\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Нижний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
					+ getEmergLow() + "\t" + getWarnLow()
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			// Emerg high
			String emergHighStr = exportName + "5\t4\t" + getEmergHigh() + "\t" + MAX_VALUE
					+ "\t0\t0\t\t\t\t\talert.\tlt:1 LANG:10027 \"Верхний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
					+ getEmergHigh() + "\t" + MAX_VALUE
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n";
			// Warning high
			String warnHighStr = exportName + "4\t4\t" + getWarnHigh() + "\t" + getEmergHigh()
					+ "\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Верхний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t"
					+ getWarnHigh() + "\t" + getEmergHigh()
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n";
			// Norm value
			strAlertValue.append(exportName + "3\t4\t" + getWarnLow() + "\t" + getWarnHigh()
					+ "\t0\t1\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t" + getWarnLow()
					+ "\t" + getWarnHigh() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n");
			strAlertValue.append(warnHighStr);
			strAlertValue.append(emergHighStr);
			// Quality
			strAlertValue.append("ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME
					+ "\t\t13\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027 \"\"\t\\0\t\t\t1\t1\t\t\t\t0\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\r\n"
					+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME
					+ "\t1\t5\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"Норма\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"*\"\t\r\n"
					+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME
					+ "\t2\t5\t\t\t\t\t\t\t\t\tquality.\tlt:1 LANG:10027 \"Неисправность\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"1\"\t\r\n"
					+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME
					+ "\t3\t5\t\t\t\t\t\t\t\t\tquality.\tlt:1 LANG:10027 \"Обрыв верхний\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"3\"\t\r\n"
					+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME
					+ "\t4\t5\t\t\t\t\t\t\t\t\tquality.\tlt:1 LANG:10027 \"Обрыв нижний\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t0\t\"\"\t\"9\"\t\r\n");
			return strAlertValue.toString();
		case Aliases:
			return getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%s@\"\r\n" + getIdent()
					+ ".val\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%" + getFormat() + "@" + (getUnits())
					+ "\"\r\n";
		case Datapoint:
			return getIdent() + "\t" + TYPE_NAME + "\t" + (id != null ? id : "") + "\r\n";
		case DbArchiveInfo:
			// TODO
			return exportName + "\t45\t1\r\n" + exportName
					+ "1\t3\t\t_ValueArchive_1\t0\t0\t5\t0\t01.01.1970 00:00:00.050\t0\t0\r\n";
		case DistributionInfo:
			ExportField<DistributionInfoE> distrInfoVal = new ExportField<>(DistributionInfoE.values().length);
			ExportField<DistributionInfoE> distrInfoQual = new ExportField<>(DistributionInfoE.values().length);
			distrInfoVal.setElement(DistributionInfoE.TypeName, TYPE_NAME).setElement(DistributionInfoE.ElementName,
					getIdent() + ".val");
			distrInfoQual.setElement(DistributionInfoE.TypeName, TYPE_NAME).setElement(DistributionInfoE.ElementName,
					getIdent() + ".qual");
			if (isFast()) {
				// IEC
				distrInfoVal.setElement(DistributionInfoE._distrib_type, "56")
						.setElement(DistributionInfoE._distrib_driver, "\\2");
				distrInfoQual.setElement(DistributionInfoE._distrib_type, "56")
						.setElement(DistributionInfoE._distrib_driver, "\\2");
			} else {
				// MODBUS
				distrInfoVal.setElement(DistributionInfoE._distrib_type, "56")
						.setElement(DistributionInfoE._distrib_driver, "\\1");
				distrInfoQual.setElement(DistributionInfoE._distrib_type, "56")
						.setElement(DistributionInfoE._distrib_driver, "\\1");
			}
			return distrInfoVal.toExportString() + distrInfoQual.toExportString();
		case DpDefaultValue:
			ExportField<DpDefaultValueE> defValue = new ExportField<>(DpDefaultValueE.values().length);
			defValue.setElement(DpDefaultValueE.TypeName, TYPE_NAME)
					.setElement(DpDefaultValueE.ElementName, getIdent() + ".val")
					.setElement(DpDefaultValueE._default_type, "3")
					.setElement(DpDefaultValueE._default_value, getDefaultValue().toString())
					.setElement(DpDefaultValueE._default_set_ibit, "0")
					.setElement(DpDefaultValueE._default_set_pvrange, "0");
			return defValue.toExportString();
		case DpFunction:
			return "";
		case DpType:
			return "SDS_AI.SDS_AI\t1#1\r\n" + "\tinfo\t1#2\r\n" + "\t\ttype\t25#3\r\n" + "\t\tsubtype\t25#4\r\n"
					+ "\t\tsystem\t25#11\r\n" + "\t\tremarks\t25#13\r\n" + "\thwinfo\t1#5\r\n" + "\t\tfreq\t19#6\r\n"
					+ "\t\tchan\t25#8\r\n" + "\t\tchas\t25#9\r\n" + "\t\tcab\t25#10\r\n" + "\t\tunit\t25#14\r\n"
					+ "\t\tpSppb\t25#90\r\n" + "\t\tpSignal\t25#91\r\n" + "\t\ttcomp\t25#93\r\n" + "\tval\t22#7\r\n"
					+ "\tlogcont\t25#12\r\n" + "\talarm\t1#16\r\n" + "\t\thAlarm\t1#17\r\n"
					+ "\t\t\tx_hAlarm\t14#29\r\n" + "\t\t\t\tmode1\t22#30\r\n" + "\t\t\t\tmode2\t22#31\r\n"
					+ "\t\t\t\tmode3\t22#32\r\n" + "\t\t\t\tmode4\t22#33\r\n" + "\t\t\t\tmode5\t22#34\r\n"
					+ "\t\t\t\tmode6\t22#35\r\n" + "\t\t\tz_hAlarm\t17#36\r\n" + "\t\t\t\tmode1\t25#37\r\n"
					+ "\t\t\t\tmode2\t25#38\r\n" + "\t\t\t\tmode3\t25#39\r\n" + "\t\t\t\tmode4\t25#40\r\n"
					+ "\t\t\t\tmode5\t25#41\r\n" + "\t\t\t\tmode6\t25#42\r\n" + "\t\thWarn\t1#20\r\n"
					+ "\t\t\tx_hWarn\t14#43\r\n" + "\t\t\t\tmode1\t22#44\r\n" + "\t\t\t\tmode2\t22#45\r\n"
					+ "\t\t\t\tmode3\t22#46\r\n" + "\t\t\t\tmode4\t22#47\r\n" + "\t\t\t\tmode5\t22#48\r\n"
					+ "\t\t\t\tmode6\t22#49\r\n" + "\t\t\tz_hWarn\t17#50\r\n" + "\t\t\t\tmode1\t25#51\r\n"
					+ "\t\t\t\tmode2\t25#52\r\n" + "\t\t\t\tmode3\t25#53\r\n" + "\t\t\t\tmode4\t25#54\r\n"
					+ "\t\t\t\tmode5\t25#55\r\n" + "\t\t\t\tmode6\t25#56\r\n" + "\t\tlWarn\t1#23\r\n"
					+ "\t\t\tx_lWarn\t14#57\r\n" + "\t\t\t\tmode1\t22#58\r\n" + "\t\t\t\tmode2\t22#59\r\n"
					+ "\t\t\t\tmode3\t22#60\r\n" + "\t\t\t\tmode4\t22#61\r\n" + "\t\t\t\tmode5\t22#62\r\n"
					+ "\t\t\t\tmode6\t22#63\r\n" + "\t\t\tz_lWarn\t17#64\r\n" + "\t\t\t\tmode1\t25#65\r\n"
					+ "\t\t\t\tmode2\t25#66\r\n" + "\t\t\t\tmode3\t25#67\r\n" + "\t\t\t\tmode4\t25#68\r\n"
					+ "\t\t\t\tmode5\t25#69\r\n" + "\t\t\t\tmode6\t25#70\r\n" + "\t\tlAlarm\t1#26\r\n"
					+ "\t\t\tx_lAlarm\t14#71\r\n" + "\t\t\t\tmode1\t22#72\r\n" + "\t\t\t\tmode2\t22#73\r\n"
					+ "\t\t\t\tmode3\t22#74\r\n" + "\t\t\t\tmode4\t22#75\r\n" + "\t\t\t\tmode5\t22#76\r\n"
					+ "\t\t\t\tmode6\t22#77\r\n" + "\t\t\tz_lAlarm\t17#78\r\n" + "\t\t\t\tmode1\t25#79\r\n"
					+ "\t\t\t\tmode2\t25#80\r\n" + "\t\t\t\tmode3\t25#81\r\n" + "\t\t\t\tmode4\t25#82\r\n"
					+ "\t\t\t\tmode5\t25#83\r\n" + "\t\t\t\tmode6\t25#84\r\n" + "\tqual\t21#92\r\n";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			return // Value
					// exportName + value + "\t0x8300000000000001\t"
					// + dateTimeStr + "\r\n" +
					// Cabinet
			"ASC (1)/0\t" + getIdent() + ".hwinfo.cab\t" + TYPE_NAME + "\t" + getPlc().getCabinet().getName()
					+ dateTimeStr
					// Number LCDM
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.chas\t" + TYPE_NAME + "\t" + getLcdmNum() + dateTimeStr
					// Number Channel
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + getChannelNum() + dateTimeStr
					// Unit
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.unit\t" + TYPE_NAME + "\t" + getType().getDmcType().getUnits() + dateTimeStr
					// Freq
					+ "ASC (1)/0\t" + getIdent() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + getPeriod().getName()
					+ dateTimeStr
					// Info System
					+ "ASC (1)/0\t" + getIdent() + ".info.system\t" + TYPE_NAME + "\t" + "ИВС" + dateTimeStr
					// Info Subtyp
					+ "ASC (1)/0\t" + getIdent() + ".info.subtype\t" + TYPE_NAME + "\t"
					+ getType().getDmcType().toString() + dateTimeStr
					// First place
					+ ((getInfoSystem() != null) && (!getInfoSystem().isEmpty()) ? "ASC (1)/0\t" + getIdent()
							+ ".hwinfo.pSignal\t" + TYPE_NAME + "\t" + getInfoSystem() + dateTimeStr : "")
					// Input point
					+ (getInputPoint() != null ? "ASC (1)/0\t" + getIdent() + ".hwinfo.pSppb\t" + TYPE_NAME + "\t"
							+ getInputPoint() + dateTimeStr : "")
					// T Compensation
					+ (getAdrCompensation() != 0 ? "ASC (1)/0\t" + getIdent() + ".hwinfo.tcomp\t" + TYPE_NAME + "\t"
							+ "M." + getPlc().getDriverModbus() + ".3." + getAdrCompensation() + dateTimeStr: "")
					// Alarms
					+ getAlarms(TYPE_NAME);
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			ExportField<PeriphAddrE> pAddrQ = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, getIdent() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "1")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000");
			pAddrQ.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, getIdent() + ".qual")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "1")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000");
			if (!isFast()) {
				int numDriver = getPlc().getDriverModbus();
				pAddr.setElement(PeriphAddrE._address_reference, "\"M." + numDriver + ".3." + getAddress() + "\"")
						.setElement(PeriphAddrE._address_poll_group, getPeriod().getNameGroup())
						.setElement(PeriphAddrE._address_direction, "\\4")
						.setElement(PeriphAddrE._address_datatype, "566")
						.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
				pAddrQ.setElement(PeriphAddrE._address_reference, "\"M." + numDriver + ".3." + (getAddress() + 2) + "\"")
						.setElement(PeriphAddrE._address_poll_group, getPeriod().getNameGroup())
						.setElement(PeriphAddrE._address_direction, "\\4")
						.setElement(PeriphAddrE._address_datatype, "561")
						.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
				
				/*return exportName + "16\t\"M." + numDriver + ".3." + getAddress() + "\"\t" + getPeriod().getNameGroup()
						+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t566\t\"MODBUS\"\r\n"
						+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t16\t\"M." + numDriver + ".3."
						+ (getAddress() + 2) + "\"\t" + getPeriod().getNameGroup()
						+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t561\t\"MODBUS\"\r\n";*/
				return pAddr.toExportString() + pAddrQ.toExportString();
			} else {

				pAddr.setElement(PeriphAddrE._address_reference, "\"iecPlc" + getPlc().getDriverIEC() + "-36.0." + (getPlc().getDriverIEC()) + ".0.0." + getIecAddress() + "\"")
						.setElement(PeriphAddrE._address_direction, "\\2")
						.setElement(PeriphAddrE._address_datatype, "526")
						.setElement(PeriphAddrE._address_drv_ident, "\"IEC\"");
				pAddrQ.setElement(PeriphAddrE._address_reference, "\"iecPlc" + getPlc().getDriverIEC() + "-36.0." + (getPlc().getDriverIEC()) + ".0.0." + getIecAddress() + "\"")
						.setElement(PeriphAddrE._address_direction, "\\2")
						.setElement(PeriphAddrE._address_datatype, "524")
						.setElement(PeriphAddrE._address_drv_ident, "\"IEC\"");
				
				/*return exportName + "16\t\"iecPlc" + getPlc().getDriverIEC() + "-36.0." + (getPlc().getDriverIEC())
						+ ".0.0." + getIecAddress() + "\"\t\t\t0\t0\t\\2\t0\t0" + "\t" + 1
						+ "\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t526\t\"IEC\"\r\n"
						+ "ASC (1)/0\t" + getIdent() + ".qual\t" + TYPE_NAME + "\t16\t\"iecPlc"
						+ getPlc().getDriverIEC() + "-10.0." + (getPlc().getDriverIEC()) + ".0.0." + getIecAddress()
						+ "\"\t\t\t0\t0\t\\2\t0\t0" + "\t" + 1
						+ "\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t524\t\"IEC\"\r\n";*/
				return pAddr.toExportString() + pAddrQ.toExportString();
			}
		case PvssRangeCheck:
			ExportField<RangeCheckE> rangeCheck = new ExportField<>(RangeCheckE.values().length);
			rangeCheck.setElement(RangeCheckE.TypeName, TYPE_NAME)
					.setElement(RangeCheckE.ElementName, getIdent() + ".val")
					.setElement(RangeCheckE._pv_range_type, "7").setElement(RangeCheckE._pv_range_ignor_inv, "0")
					.setElement(RangeCheckE._pv_range_neg, "0")
					.setElement(RangeCheckE._pv_range_min, Float.toString(getSensorMin()))
					.setElement(RangeCheckE._pv_range_max, Float.toString(getSensorMax()))
					.setElement(RangeCheckE._pv_range_incl_min, "1").setElement(RangeCheckE._pv_range_incl_max, "1");
			return rangeCheck.toExportString();
		case DpConvRawToIngMain:
			return "";
		default:
			break;
		}
		return null;
	}

	public String getDpName() {
		return getIdent();
	}

	private int getIecAddress() {
		return (getAddress() - FIRST_IEC_ADDRESS) / 4 + 1;
	}

	@Override
	public String getSDSType() {
		return TYPE_NAME;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return (getIdent() != null) && (getPlc() != null) && (getComNum() != null) && (getChannelNum() != null)
				&& (getAddress() != null) && (getPeriod() != null);
	}
}
