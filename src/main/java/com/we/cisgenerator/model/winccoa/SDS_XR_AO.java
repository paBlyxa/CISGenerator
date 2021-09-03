package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.PLC_TYPE;
import com.we.cisgenerator.model.XR;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.model.winccoa.ascii.ExportField;
import com.we.cisgenerator.model.winccoa.ascii.PeriphAddrE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_XR_AO implements Dp {

	private final static String TYPE_NAME = "SDS_XR_AO";
	private final XR xr;
	private String exportName = null;
	
	public SDS_XR_AO(XR xr){
		this.xr = xr;
	}
	
	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		if (exportName == null){
			exportName = "ASC (1)/0\t" + xr.getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch(field){
		case AlertValue:
			return "";
		case Aliases:
			return xr.getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + xr.getName() + "@%s@\"\r\n" + xr.getIdent()
				+ ".val\t\"\"\tlt:1 LANG:10027 \"" + xr.getName() + "@%" + (xr.getFormat() != null ? xr.getFormat() : "") + "@" + (xr.getUnits() != null ? xr.getUnits() : "") + "\"\r\n";
		case Datapoint:
			return xr.getIdent() + "\t" + TYPE_NAME + "\t" + "" + "\r\n";
		case DbArchiveInfo:
			return "";
		case DistributionInfo:
			return exportName + "56\t\\" + "1" + "\r\n";
		case DpDefaultValue:
			String defaultValue = "0";
			if (xr.getType().compareTo(PLC_TYPE.REAL) == 0){
				defaultValue = "0.0";
			}
			return exportName + "3\t" + defaultValue + "\t0\t0\r\n";
		case DpFunction:
			return (xr.getTag() != null ? exportName + "60\t" + xr.getTag() + ".val:_original.._value\t\"p1\"\t\t1\r\n" : "");
		case DpType:
			return TYPE_NAME + "." + TYPE_NAME + "\t1#1\r\n"
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
						+ "\t\tpSppb\t25#90\r\n"
						+ "\t\tpSignal\t25#91\r\n"
					+ "\tval\t22#7\r\n"
					+ "\tlogcont\t25#12\r\n"
					+ "\talarm\t1#16\r\n"
					+ "\t\thAlarm\t1#17\r\n"
						+ "\t\t\tx_hAlarm\t14#29\r\n"
							+ "\t\t\t\tmode1\t22#30\r\n"
							+ "\t\t\t\tmode2\t22#31\r\n"
							+ "\t\t\t\tmode3\t22#32\r\n"
							+ "\t\t\t\tmode4\t22#33\r\n"
							+ "\t\t\t\tmode5\t22#34\r\n"
							+ "\t\t\t\tmode6\t22#35\r\n"
						+ "\t\t\tz_hAlarm\t17#36\r\n"
							+ "\t\t\t\tmode1\t25#37\r\n"
							+ "\t\t\t\tmode2\t25#38\r\n"
							+ "\t\t\t\tmode3\t25#39\r\n"
							+ "\t\t\t\tmode4\t25#40\r\n"
							+ "\t\t\t\tmode5\t25#41\r\n"
							+ "\t\t\t\tmode6\t25#42\r\n"
					+ "\t\thWarn\t1#20\r\n"
						+ "\t\t\tx_hWarn\t14#43\r\n"
							+ "\t\t\t\tmode1\t22#44\r\n"
							+ "\t\t\t\tmode2\t22#45\r\n"
							+ "\t\t\t\tmode3\t22#46\r\n"
							+ "\t\t\t\tmode4\t22#47\r\n"
							+ "\t\t\t\tmode5\t22#48\r\n"
							+ "\t\t\t\tmode6\t22#49\r\n"
						+ "\t\t\tz_hWarn\t17#50\r\n"
							+ "\t\t\t\tmode1\t25#51\r\n"
							+ "\t\t\t\tmode2\t25#52\r\n"
							+ "\t\t\t\tmode3\t25#53\r\n"
							+ "\t\t\t\tmode4\t25#54\r\n"
							+ "\t\t\t\tmode5\t25#55\r\n"
							+ "\t\t\t\tmode6\t25#56\r\n"
					+ "\t\tlWarn\t1#23\r\n"
						+ "\t\t\tx_lWarn\t14#57\r\n"
							+ "\t\t\t\tmode1\t22#58\r\n"
							+ "\t\t\t\tmode2\t22#59\r\n"
							+ "\t\t\t\tmode3\t22#60\r\n"
							+ "\t\t\t\tmode4\t22#61\r\n"
							+ "\t\t\t\tmode5\t22#62\r\n"
							+ "\t\t\t\tmode6\t22#63\r\n"
						+ "\t\t\tz_lWarn\t17#64\r\n"
							+ "\t\t\t\tmode1\t25#65\r\n"
							+ "\t\t\t\tmode2\t25#66\r\n"
							+ "\t\t\t\tmode3\t25#67\r\n"
							+ "\t\t\t\tmode4\t25#68\r\n"
							+ "\t\t\t\tmode5\t25#69\r\n"
							+ "\t\t\t\tmode6\t25#70\r\n"
					+ "\t\tlAlarm\t1#26\r\n"
						+ "\t\t\tx_lAlarm\t14#71\r\n"
							+ "\t\t\t\tmode1\t22#72\r\n"
							+ "\t\t\t\tmode2\t22#73\r\n"
							+ "\t\t\t\tmode3\t22#74\r\n"
							+ "\t\t\t\tmode4\t22#75\r\n"
							+ "\t\t\t\tmode5\t22#76\r\n"
							+ "\t\t\t\tmode6\t22#77\r\n"
						+ "\t\t\tz_lAlarm\t17#78\r\n"
							+ "\t\t\t\tmode1\t25#79\r\n"
							+ "\t\t\t\tmode2\t25#80\r\n"
							+ "\t\t\t\tmode3\t25#81\r\n"
							+ "\t\t\t\tmode4\t25#82\r\n"
							+ "\t\t\t\tmode5\t25#83\r\n"
							+ "\t\t\t\tmode6\t25#84\r\n"
				+ "\thLimit\t22#88\r\n"
				+ "\tlLimit\t22#89\r\n"
				+ "\tqual\t21#92\r\n";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			return 
					// Cabinet
					"ASC (1)/0\t" + xr.getIdent() + ".hwinfo.cab\t" + TYPE_NAME + "\t"
					+ xr.getPlc().getCabinet().getName() + dateTimeStr
					// Number LCDM
					//+ "ASC (1)/0\t" + getIdent() + ".hwinfo.chas\t" + TYPE_NAME + "\t" + getLcdmNum()
					//+ dateTimeStr
					// Number Channel (External addr for XR)
					+ "ASC (1)/0\t" + xr.getIdent() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + xr.getExternalAddr()
					+ dateTimeStr
					// Unit
					+ xr.getUnits() != null ? "ASC (1)/0\t" + xr.getIdent() + ".hwinfo.unit\t" + TYPE_NAME + "\t" + xr.getUnits() + dateTimeStr : ""
					// Freq
					+ "ASC (1)/0\t" + xr.getIdent() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + xr.getPeriod().getName()
					+ dateTimeStr
					// Info System
					+ "ASC (1)/0\t" + xr.getIdent() + ".info.system\t" + TYPE_NAME + "\t" + xr.getSystem().getSystem()
					+ dateTimeStr
					// Info Subtyp
					+ "ASC (1)/0\t" + xr.getIdent() + ".info.subtype\t" + TYPE_NAME + "\t" + xr.getType() + ":" + xr.getByteOrder()
					+ dateTimeStr
					// Alarms
					+ xr.getAlarms(TYPE_NAME);
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, xr.getIdent() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_reference, "\"M." + xr.getPlc().getDriverModbus() + ".16." + xr.getAddress() + "\"")
					.setElement(PeriphAddrE._address_poll_group, xr.getPeriod().getNameGroup())
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_direction, "\\1")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "0")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_datatype, "566")
					.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			str.append(pAddr.toExportString());
			pAddr.setElement(PeriphAddrE.ElementName, xr.getIdent() + ".qual")
				.setElement(PeriphAddrE._address_reference, "\"M." + xr.getPlc().getDriverModbus() + ".16." + (xr.getAddress() + 2) + "\"")
				.setElement(PeriphAddrE._address_datatype, "561");
			str.append(pAddr.toExportString());
			return str.toString();	
				//return exportName + "16\t\"M." + xr.getPlc().getDriverModbus() + ".16." + xr.getAddress() + "\"\t" + xr.getPeriod().getNameGroup()
				//		+ "\t\t0\t0\t\\1\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t566\t\"MODBUS\"\r\n"
				//		+ "ASC (1)/0\t" + xr.getIdent() + ".qual\t" + TYPE_NAME + "\t16\t\"M." + xr.getPlc().getDriverModbus() + ".16." + (xr.getAddress() + 2) + "\"\t" + xr.getPeriod().getNameGroup()
				//				+ "\t\t0\t0\t\\1\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t561\t\"MODBUS\"\r\n";
			
		case PvssRangeCheck:
			return exportName + "7\t0\t0\t" + xr.getSensorMin() + "\t" + xr.getSensorMax() + "\t1\t1\t\t\r\n";
		case DpConvRawToIngMain:
			return "";
		}
		return null;
	}

	@Override
	public String getDpName() {
		return xr.getIdent();
	}

	@Override
	public String getSDSType(){
		return TYPE_NAME;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return xr.getIdent() != null;
	}
	
	@Override
	public String toString(){
		return "SDS_XR: " + xr.toString(); 
	}
}
