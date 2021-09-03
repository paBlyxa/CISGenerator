package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.DI;
import com.we.cisgenerator.model.winccoa.ascii.*;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue.AlertElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_DI implements Dp {
	
	private final static String TYPE_NAME = "SDS_DI";
	
	private boolean defaultValue;
	private String exportName = null;
	private DI di;
	
	public SDS_DI(String dpName){
		this.di = new DI(dpName);
	}
	
	public SDS_DI(DI di) {
		this.di = di;
	}
	
	public String getName() {
		return di.getIdent();
	}
	
	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		if (exportName == null){
			this.exportName = "ASC (1)/0\t" + getName() + ".val\t" + TYPE_NAME + "\t";
		}
		switch(field){
		case AlertValue:
			AlertValue alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.ElementName, getName() + ".val")
				.setElement(AlertElement.alert_hdl_type, "12")
				.setElement(AlertElement._alert_hdl_panel, "\"\"")
				.setElement(AlertElement._alert_hdl_panel_param, "\"\"")
				.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_min_prio, "\\0")
				.setElement(AlertElement._alert_hdl_class, di.getAlarmClass() + ".")
				.setElement(AlertElement._alert_hdl_active, "1")
				.setElement(AlertElement._alert_hdl_orig_hdl, "0")
				.setElement(AlertElement._alert_hdl_ok_range, "0")
				.setElement(AlertElement._alert_hdl_hyst_type, "0")
				.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
				.setElement(AlertElement._alert_hdl_multi_instance, "0")
				.setElement(AlertElement._alert_hdl_text1, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_text0, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_impulse, "0");
			return alertValue.toExportString();
			//return exportName + "\t12\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\\0\tdisturbance.\t\t0\t0\t0\t0\t01.01.1970 00:00:00.000\t0"
			//		+ "\t\t\tlt:1 LANG:10027 \"\"\tlt:1 LANG:10027 \"\"\t\t\t\t\t\t\t\t\t\t\t\t0\t\t\t\t\t\t\t\t\r\n";
		case Aliases:
			return getName() + ".\t\"\"\tlt:1 LANG:10027 \"" + di.getName() + "@%s@\"\r\n" + getName()
			+ ".val\t\"\"\tlt:1 LANG:10027 \"" + di.getName() + "@@\"\r\n";
		case Datapoint:
			return getName() + "\t" + TYPE_NAME + "\t" + "\r\n";
		case DbArchiveInfo:
			ExportField<DBArchiveInfoE> archiveInfo = new ExportField<>(DBArchiveInfoE.values().length);
			archiveInfo.setElement(DBArchiveInfoE.ElementName, getName() + ".val")
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
			return TYPE_NAME + "." + TYPE_NAME + "\t1#1\r\n"
					+ "\thwinfo\t1#12\r\n"
						+ "\t\tfreq\t19#17\r\n"
						+ "\t\tchan\t25#18\r\n"
						+ "\t\tchas\t25#19\r\n"
						+ "\t\tcab\t25#20\r\n"
						+ "\t\tinver\t23#21\r\n"
						+ "\t\tpSppb\t25#46\r\n"
						+ "\t\tpSignal\t25#47\r\n"
						+ "\t\tcoil\t25#49\r\n"
					+ "\tlogcont\t25#24\r\n"
					+ "\tval\t23#26\r\n"
					+ "\talarm\t1#27\r\n"
						+ "\t\tdAlarm\t13#39\r\n"
							+ "\t\t\tmode1\t21#40\r\n"
							+ "\t\t\tmode2\t21#41\r\n"
							+ "\t\t\tmode3\t21#42\r\n"
							+ "\t\t\tmode4\t21#43\r\n"
							+ "\t\t\tmode5\t21#44\r\n"
							+ "\t\t\tmode6\t21#45\r\n"
					+ "\tinfo\t1#34\r\n"
						+ "\t\ttype\t25#35\r\n"
						+ "\t\tsubtype\t25#36\r\n"
						+ "\t\tsystem\t25#37\r\n"
						+ "\t\tremarks\t25#38\r\n"
					+ "\tqual\t21#48\r\n";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			str.append(
					// Cabinet
					"ASC (1)/0\t" + getName() + ".hwinfo.cab\t" + TYPE_NAME + "\t" + di.getPlc().getCabinet().getName() + dateTimeStr
					// Number module
					+ "ASC (1)/0\t" + getName() + ".hwinfo.chas\t" + TYPE_NAME + "\t" + di.getModuleNum() + dateTimeStr
					// Number Channel
					+ "ASC (1)/0\t" + getName() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + di.getChannelNum() + dateTimeStr
					// Freq
					+ "ASC (1)/0\t" + getName() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + di.getPeriod().getName() + dateTimeStr
					// First place
					+ ((di.getInfoSystem() != null) && (!di.getInfoSystem().isEmpty()) ? "ASC (1)/0\t" + getName() + ".hwinfo.pSignal\t" + TYPE_NAME + "\t" + di.getInfoSystem() + dateTimeStr
					: "")
					// Coil
					+ (di.getCoil() != null ? "ASC (1)/0\t" + getName() + ".hwinfo.coil\t" + TYPE_NAME + "\t" + di.getCoil() + dateTimeStr
					: "")
					// Input point
					+ (di.getInputPoint() != null ? "ASC (1)/0\t" + getName() + ".hwinfo.pSppb\t" + TYPE_NAME + "\t" + di.getInputPoint() + dateTimeStr : ""));
			for (int i = 0; i < di.getIdAlarms().length; i++) {
				Integer alarmValue = di.getIdAlarms()[i];
				if (alarmValue != null) {
					str.append("ASC (1)/0\t");
					str.append(getName());
					str.append(".alarm.dAlarm.mode");
					str.append(i + 1);
					str.append("\t" + TYPE_NAME + "\t");
					str.append(alarmValue);
					str.append("\t0x8300000000000001\t");
					str.append(dateTimeStr);
					str.append("\r\n");
				}
			}
			return str.toString();
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, getName() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_reference, "\"M." + di.getPlc().getDriverModbus() + ".2." + di.getAddress() + "\"")
					.setElement(PeriphAddrE._address_poll_group, di.getPeriod().getNameGroup())
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_direction, "\\4")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "1")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_datatype, "567")
					.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			return pAddr.toExportString();
			//return exportName + "16\tM." + di.getPlc().getDriverModbus()
			//		+ ".2." + di.getAddress() + "\t" + di.getPeriod().getNameGroup()
			//		+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t567\tMODBUS\r\n";
		case DpFunction:
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
	public String toString(){
		return di.toString();
	}

	@Override
	public String getSDSType(){
		return "SDS_DI";
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return di.getIdent() != null;
	}
}
