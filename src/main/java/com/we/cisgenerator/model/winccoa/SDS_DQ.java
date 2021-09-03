package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.DQ;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.model.winccoa.ascii.DBArchiveInfoE;
import com.we.cisgenerator.model.winccoa.ascii.ExportField;
import com.we.cisgenerator.model.winccoa.ascii.PeriphAddrE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_DQ extends DQ implements Dp {

	private final static String TYPE_NAME = "SDS_DO";
	
	private boolean defaultValue;
	private String exportName;
	private Integer id;
	
	public SDS_DQ(String dpName, Integer id){
		super(dpName);
		this.id = id;
	}
	
	public SDS_DQ(){
	}
	
	@Override
	public String getName(){
		return getIdent();
	}
	
	@Override
	public String getExportField(AsciiExportField field){
		StringBuilder str = new StringBuilder();
		if (exportName == null){
			exportName = "ASC (1)/0\t" + getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch(field){
		case AlertValue:
			return exportName + "\t12\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\\0\t" + getAlarmClass() + ".\t\t0\t0\t0\t0\t01.01.1970 00:00:00.000\t0"
					+ "\t\t\tlt:1 LANG:10027 \"\"\tlt:1 LANG:10027 \"\"\t\t\t\t\t\t\t\t\t\t\t\t0\t\t\t\t\t\t\t\t\r\n";
		case Aliases:
			return getName() + ".\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%s@\"\r\n" + getName()
			+ ".val\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@@\"\r\n";
		case Datapoint:
			return getIdent() + "\t" + TYPE_NAME + "\t" + (id != null ? id : "") + "\r\n";
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
				.setElement(DBArchiveInfoE._archive_class, "_ValueArchive_2")
				.setElement(DBArchiveInfoE._archive_interv, "0")
				.setElement(DBArchiveInfoE._archive_interv_type, "0")
				.setElement(DBArchiveInfoE._archive_std_type, "0")
				.setElement(DBArchiveInfoE._archive_std_tol, "0")
				.setElement(DBArchiveInfoE._archive_std_time, "01.01.1970 00:00:00.000")
				.setElement(DBArchiveInfoE._archive_round_val, "0")
				.setElement(DBArchiveInfoE._archive_round_inv, "0");
			str.append(archiveInfo.toExportString());
			return str.toString();
		case DistributionInfo:
			return exportName + "56\t\\" + 1 + "\r\n";
		case DpDefaultValue:
			return exportName + "3\t" + Boolean.toString(defaultValue) + "\t0\t0\r\n";
		case DpType:
			return TYPE_NAME + "." + TYPE_NAME + "\t1#1\r\n\thwinfo\t1#12\r\n\t\tfreq\t19#17\r\n\t\tchan\t25#18\r\n"
					+ "\t\tchas\t25#19\r\n\t\tcab\t25#20\r\n\t\tinver\t23#21\r\n\t\tpSppb\t25#46\r\n"
					+ "\t\tpSignal\t25#47\r\n\t\tcoil\t25#49\r\n\tlogcont\t25#24\r\n\tval\t23#26\r\n\talarm\t1#27\r\n"
					+ "\t\tdAlarm\t13#39\r\n\t\t\tmode1\t21#40\r\n\t\t\tmode2\t21#41\r\n\t\t\tmode3\t21#42\r\n"
					+ "\t\t\tmode4\t21#43\r\n\t\t\tmode5\t21#44\r\n\t\t\tmode6\t21#45\r\n\tinfo\t1#34\r\n"
					+ "\t\ttype\t25#35\r\n\t\tsubtype\t25#36\r\n\t\tsystem\t25#37\r\n\t\tremarks\t25#38\r\n";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			return
					// Cabinet
					"ASC (1)/0\t" + getName() + ".hwinfo.cab\t" + TYPE_NAME + "\t"
					+ getPlc().getCabinet().getName() + dateTimeStr
					// Number module
					+ "ASC (1)/0\t" + getName() + ".hwinfo.chas\t" + TYPE_NAME + "\t" + getModuleNum()
					+ dateTimeStr
					// Number Channel
					+ "ASC (1)/0\t" + getName() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + getChannelNum()
					+ dateTimeStr
					// Freq
					+ "ASC (1)/0\t" + getName() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + getPeriod().getName()
					+ dateTimeStr
					// First place
					+ ((getInfoSystem() != null) && (!getInfoSystem().isEmpty()) ? "ASC (1)/0\t" + getName() + ".hwinfo.pSignal\t" + TYPE_NAME + "\t" + getInfoSystem() + dateTimeStr
					: "")
					// Coil
					+ (getCoil() != null ? "ASC (1)/0\t" + getName() + ".hwinfo.coil\t" + TYPE_NAME + "\t" + getCoil() + dateTimeStr
					: "")
					// Input point
					+ (getInputPoint() != null ? "ASC (1)/0\t" + getName() + ".hwinfo.pSppb\t" + TYPE_NAME + "\t" + getInputPoint() + dateTimeStr : "");
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, getIdent() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_reference, "\"M." + getPlc().getDriverModbus() + ".15." + getAddress() + "\"")
					.setElement(PeriphAddrE._address_poll_group, getPeriod().getNameGroup())
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_direction, "\\1")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "0")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_datatype, "567")
					.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			return pAddr.toExportString();
			//return exportName + "16\tM." + (getPlc().isInterfaceModule() ? getPlc().getReservPLC().getDriverModbus() : getPlc().getDriverModbus()) 
			//		+ ".5." + (getAddress()) + "\t" + getPeriod().getName()
			//		+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t567\tMODBUS\r\n";
		case DpFunction:
			return (getTag() != null ? exportName + "60\t" + getTag() + ".val:_online.._value\t\"p1\"\t\t1\r\n" : "");
		case PvssRangeCheck:
		case DpConvRawToIngMain:
			return "";
		default:
			break;
		}
		return null;
	}

	@Override
	public String getDpName() {
		return getName();
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
