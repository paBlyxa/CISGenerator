package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.XR;
import com.we.cisgenerator.model.winccoa.ascii.*;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue.AlertElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_XR_DI implements Dp {

	private final static String TYPE_NAME = "SDS_XR_DI";
	private final XR xr;
	private String exportName = null;

	public SDS_XR_DI(XR xr) {
		this.xr = xr;
	}

	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		if (exportName == null) {
			exportName = "ASC (1)/0\t" + xr.getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch (field) {
		case AlertValue:
			if (xr.getAlarmClass() != null) {
				AlertValue alertValue = new AlertValue();
				alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
						.setElement(AlertElement.ElementName, xr.getIdent() + ".val")
						.setElement(AlertElement.alert_hdl_type, "12").setElement(AlertElement._alert_hdl_panel, "\"\"")
						.setElement(AlertElement._alert_hdl_panel_param, "\"\"")
						.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"")
						.setElement(AlertElement._alert_hdl_min_prio, "\\0")
						.setElement(AlertElement._alert_hdl_class, xr.getAlarmClass() + ".")
						.setElement(AlertElement._alert_hdl_active, "1")
						.setElement(AlertElement._alert_hdl_orig_hdl, "0")
						.setElement(AlertElement._alert_hdl_ok_range, "0")
						.setElement(AlertElement._alert_hdl_hyst_type, "0")
						.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
						.setElement(AlertElement._alert_hdl_multi_instance, "0")
						.setElement(AlertElement._alert_hdl_text1, "lt:1 LANG:10027 \"\"")
						.setElement(AlertElement._alert_hdl_text0, "lt:1 LANG:10027 \"\"")
						.setElement(AlertElement._alert_hdl_impulse, "0");
				 str.append(alertValue.toExportString());
			}
			AlertValue alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.ElementName, xr.getIdent() + ".qual")
				.setElement(AlertElement.alert_hdl_type, "13")
				.setElement(AlertElement._alert_hdl_panel, "\"\"")
				.setElement(AlertElement._alert_hdl_panel_param, "\"\"")
				.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_min_prio, "\\0")
				.setElement(AlertElement._alert_hdl_active, "1")
				.setElement(AlertElement._alert_hdl_orig_hdl, "1")
				.setElement(AlertElement._alert_hdl_multi_instance, "0")
				.setElement(AlertElement._alert_hdl_impulse, "0");
			str.append(alertValue.toExportString());
			alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.ElementName, xr.getIdent() + ".qual")
				.setElement(AlertElement.alert_hdl_type, "5")
				.setElement(AlertElement.DetailNr, "1")
				.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"Норма\"")
				.setElement(AlertElement._alert_hdl_hyst_type, "0")
				.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
				.setElement(AlertElement._alert_hdl_went_text, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_status64_pattern, "0x0")
				.setElement(AlertElement._alert_hdl_neg, "0")
				.setElement(AlertElement._alert_hdl_status64_match, "")
				.setElement(AlertElement._alert_hdl_match, "\"*\"");
			str.append(alertValue.toExportString());
			alertValue.setElement(AlertElement.DetailNr, "2")
				.setElement(AlertElement._alert_hdl_class, "quality.")
				.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"Неисправность\"")
				.setElement(AlertElement._alert_hdl_neg, "1")
				.setElement(AlertElement._alert_hdl_match, "\"0\"");
			str.append(alertValue.toExportString());
			return str.toString();
		case Aliases:
			return xr.getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + xr.getName() + "@%s@\"\r\n" + xr.getIdent()
					+ ".val\t\"\"\tlt:1 LANG:10027 \"" + xr.getName() + "@%"
					+ (xr.getFormat() != null ? xr.getFormat() : "") + "@"
					+ (xr.getUnits() != null ? xr.getUnits() : "") + "\"\r\n";
		case Datapoint:
			return xr.getIdent() + "\t" + TYPE_NAME + "\t" + "" + "\r\n";
		case DbArchiveInfo:
			ExportField<DBArchiveInfoE> archiveInfo = new ExportField<>(DBArchiveInfoE.values().length);
			archiveInfo.setElement(DBArchiveInfoE.ElementName, xr.getIdent() + ".val")
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
			return exportName + "56\t\\" + "1" + "\r\n";
		case DpDefaultValue:
			return exportName + "3\t" + "0" + "\t0\t0\r\n";
		case DpFunction:
			return "";
		case DpType:
			return TYPE_NAME + "." + TYPE_NAME + "\t1#1\r\n"
					+ "\thwinfo\t1#12\r\n"
					+ "\t\tfreq\t19#17\r\n"
					+ "\t\tchan\t25#18\r\n"
					+ "\t\tchas\t25#19\r\n"
					+ "\t\tcab\t25#20\r\n"
					+ "\t\tinver\t23#21\r\n"
				+ "\tval\t23#26\r\n"
				+ "\tinfo\t1#34\r\n"
					+ "\t\ttype\t25#35\r\n"
					+ "\t\tsubtype\t25#36\r\n"
					+ "\t\tsystem\t25#37\r\n"
					+ "\t\tremarks\t25#38\r\n"
				+ "\tqual\t21#39";
		case DpValue:
			String dateTimeStr = "\t0x8300000000000001\t"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
			return 
					// Cabinet
					"ASC (1)/0\t" + xr.getIdent() + ".hwinfo.cab\t" + TYPE_NAME + "\t" + xr.getPlc().getCabinet().getName()
					+ dateTimeStr
					// Freq
					+ "ASC (1)/0\t" + xr.getIdent() + ".hwinfo.freq\t" + TYPE_NAME + "\t" + xr.getPeriod().getName() + dateTimeStr
					// Number Channel (External addr for XR)
					+ "ASC (1)/0\t" + xr.getIdent() + ".hwinfo.chan\t" + TYPE_NAME + "\t" + xr.getExternalAddr() / 16 + "." + xr.getExternalAddr() % 16 + dateTimeStr
					// Info System
					+ "ASC (1)/0\t" + xr.getIdent() + ".info.system\t" + TYPE_NAME + "\t"
					+ xr.getSystem().getSystem() + dateTimeStr
					;
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
					.setElement(PeriphAddrE.ElementName, xr.getIdent() + ".val")
					.setElement(PeriphAddrE._address_type, "16")
					.setElement(PeriphAddrE._address_reference, "\"M." + xr.getPlc().getDriverModbus() + ".2." + xr.getAddress() + "\"")
					.setElement(PeriphAddrE._address_poll_group, xr.getPeriod().getNameGroup())
					.setElement(PeriphAddrE._address_offset, "0")
					.setElement(PeriphAddrE._address_subindex, "0")
					.setElement(PeriphAddrE._address_direction, "\\4")
					.setElement(PeriphAddrE._address_internal, "0")
					.setElement(PeriphAddrE._address_lowlevel, "1")
					.setElement(PeriphAddrE._address_active, "1")
					.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
					.setElement(PeriphAddrE._address_datatype, "566")
					.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			return pAddr.toExportString();
					//+ "ASC (1)/0\t" + xr.getIdent() + ".qual\t" + TYPE_NAME + "\t16\t\"M."
					//+ xr.getPlc().getDriverModbus() + ".3." + (xr.getAddress() + 2) + "\"\t"
					//+ xr.getPeriod().getNameGroup()
					//+ "\t\t0\t0\t\\4\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t561\t\"MODBUS\"\r\n";
		case PvssRangeCheck:
			return "";
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
	public String getSDSType() {
		return TYPE_NAME;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return xr.getIdent() != null;
	}
	
	@Override
	public String toString(){
		return "SDS_XR_DI: " + xr.toString(); 
	}
}
