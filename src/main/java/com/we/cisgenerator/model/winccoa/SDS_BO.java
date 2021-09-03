package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.internal.DAlarms;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue.AlertElement;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.model.winccoa.ascii.DBArchiveInfoE;
import com.we.cisgenerator.model.winccoa.ascii.ExportField;
import com.we.cisgenerator.util.FormulaUtil;
import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.TableAccess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@TableAccess(name = "Расчетные параметры BO")
public class SDS_BO extends DAlarms implements Dp {

	private final static String TYPE_NAME = "SDS_BO";

	private String ident;
	private String name;
	private String subType;
	private String formula;
	private final List<String> tags;
	private String exportName = null;
	private String alarmClass;

	public SDS_BO() {
		this.tags = new ArrayList<String>();
	}

	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		if (exportName == null) {
			exportName = "ASC (1)/0\t" + getIdent() + ".val\t" + TYPE_NAME + "\t";
		}
		switch (field) {
		case AlertValue:
			AlertValue alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME);
			alertValue.setElement(AlertElement.ElementName, getIdent() + ".val");
			alertValue.setElement(AlertElement.alert_hdl_type, "12");
			alertValue.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"");
			alertValue.setElement(AlertElement._alert_hdl_min_prio, "\\0");
			alertValue.setElement(AlertElement._alert_hdl_class, getAlarmClass() + ".");
			alertValue.setElement(AlertElement._alert_hdl_active, "1");
			alertValue.setElement(AlertElement._alert_hdl_orig_hdl, "1");
			alertValue.setElement(AlertElement._alert_hdl_ok_range, "0");
			alertValue.setElement(AlertElement._alert_hdl_hyst_type, "0");
			alertValue.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000");
			alertValue.setElement(AlertElement._alert_hdl_multi_instance, "0");
			alertValue.setElement(AlertElement._alert_hdl_text1, "lt:1 LANG:10027 \"\"");
			alertValue.setElement(AlertElement._alert_hdl_text0, "lt:1 LANG:10027 \"\"");
			alertValue.setElement(AlertElement._alert_hdl_impulse, "0");
			return alertValue.toExportString();
		// return exportName
		// + "\t12\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027
		// \"\"\t\\0\talert.\t\t1\t1\t0\t0\t01.01.1970 00:00:00.000\t0\t\t\tlt:1
		// LANG:10027 \"\"\tlt:1 LANG:10027 \"\"\t\t\t\t\t\t\t\t\t\t\t\t0\r\n";
		case Aliases:
			String newName = name.replaceAll("\"", "\\\\\"");
			return ident + ".\t\"\"\tlt:1 LANG:10027 \"" + newName + "@%s@\"\r\n";
		case Datapoint:
			return ident + "\t" + TYPE_NAME + "\t" + "\r\n";
		case DbArchiveInfo:
			ExportField<DBArchiveInfoE> archiveInfo = new ExportField<>(DBArchiveInfoE.values().length);
			archiveInfo.setElement(DBArchiveInfoE.ElementName, getIdent() + ".val")
				.setElement(DBArchiveInfoE.TypeName, TYPE_NAME)
				.setElement(DBArchiveInfoE._archive_type, "45")
				.setElement(DBArchiveInfoE._archive_archive, "1");
			str.append(archiveInfo.toExportString());
			archiveInfo.setElement(DBArchiveInfoE.DetailNr, "1")
				.setElement(DBArchiveInfoE._archive_type, "3")
				.setElement(DBArchiveInfoE._archive_archive, "")
				.setElement(DBArchiveInfoE._archive_class, "_ValueArchive_5")
				.setElement(DBArchiveInfoE._archive_interv, "0")
				.setElement(DBArchiveInfoE._archive_interv_type, "0")
				.setElement(DBArchiveInfoE._archive_std_type, "6")
				.setElement(DBArchiveInfoE._archive_std_tol, "0")
				.setElement(DBArchiveInfoE._archive_std_time, "01.01.1970 00:00:01.000")
				.setElement(DBArchiveInfoE._archive_round_val, "0")
				.setElement(DBArchiveInfoE._archive_round_inv, "0");
			str.append(archiveInfo.toExportString());
			return str.toString();
			//return exportName + "\t45\t1\t\t\t\t\t\t\t\r\n" + exportName
			//		+ "1\t15\t\t_ValueArchive_5\t0\t0\t0\t0\t01.01.1970 00:00:00.000\t0\t0\r\n";
		case DistributionInfo:
			return "";
		case DpDefaultValue:
			return exportName + "3\t0\t0\t0\r\n";
		case DpFunction:
			boolean notFirst = false;
			//if (subType.equals("BOEX") || subType.equals("AND") || subType.equals("OR")) {
			//	for (String tag : tags) {
			//		if (notFirst) {
			//			str.append(", ");
			//		}
			//		str.append(tag + ".val:_online.._value");
			//		notFirst = true;
			//	}
			//}
			for (String tag : tags) {
				if (notFirst) {
					str.append(", ");
				}
				notFirst = true;
				if (subType.equals("BOEX") || subType.equals("AND") ||
				subType.equals("OR")){
					str.append(tag + ".val:_online.._value, ");
					str.append(tag + ".val:_original.._online_bad");
				} else {
					if (subType.equals("TOR")){
						str.append(tag + ".val:_original.._online_bad");
					} else {
						str.append(tag + ".val:_original.._status");
					}
				}
			}
			//if (subType.equals("BOEX") || subType.equals("AND") || subType.equals("OR")) {
			//	return exportName + "60\t" + "_Event.Heartbeat:_original.._value" + "\t\"" + getFormula()
			//			+ "\"\t\t1\r\n";
			//} else {
				return exportName + "60\t" + str.toString() + "\t\"" + getFormula() + "\"\t\t1\r\n";
			//}
		case DpType:
			return "SDS_BO.SDS_BO\t1#1\r\n" + "\tval\t23#2\r\n" + "\tinfo\t1#4\r\n" + "\t\ttype\t25#5\r\n"
					+ "\t\tsubtype\t25#6\r\n" + "\t\tsystem\t25#7\r\n" + "\t\tremarks\t25#8\r\n" + "\tlogcont\t25#9\r\n"
					+ "\talarm\t1#10\r\n" + "\t\tdAlarm\t13#11\r\n" + "\t\t\tmode1\t21#12\r\n"
					+ "\t\t\tmode2\t21#13\r\n" + "\t\t\tmode3\t21#14\r\n" + "\t\t\tmode4\t21#15\r\n"
					+ "\t\t\tmode5\t21#16\r\n" + "\t\t\tmode6\t21#17\r\n";
		case DpValue:
			String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"));
			for (int i = 0; i < getIdAlarms().length; i++) {
				Integer alarmValue = getIdAlarms()[i];
				if (alarmValue != null) {
					str.append("ASC (1)/0\t");
					str.append(ident);
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
		case PvssRangeCheck:
			return "";
		default:
			return "";
		}
	}

	@Override
	public String getDpName() {
		return ident;
	}

	@Override
	public String getSDSType() {
		return "SDS_BO";
	}

	public String getIdent() {
		return ident;
	}

	@ColumnAccess(columnName = "ZPID")
	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getName() {
		return name;
	}

	@ColumnAccess(columnName = "ZDESC")
	public void setName(String name) {
		this.name = name;
	}

	@ColumnAccess(columnName = "ZINPNT", count = 20)
	public void addTag(String tag) {
		if (tag != null && !tag.isEmpty()) {
			tags.add(tag);
		}
	}
	
	public List<String> getTags() {
		return tags;
	}

	public String getSubType() {
		return subType;
	}

	@ColumnAccess(columnName = "ZPTSUBTYP")
	public void setSubType(String subType) {
		this.subType = subType;
	}

	@ColumnAccess(columnName = "ZBOEXPR")
	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getAlarmClass() {
		return alarmClass;
	}

	@ColumnAccess(columnName = "AlarmClass")
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("SDS_BO: ");
		str.append(ident);
		str.append(", subType: " + subType);
		str.append(", name: " + name);
		str.append(", tags:");
		for (String tag : tags) {
			str.append(" ").append(tag);
		}
		return str.toString();

	}
	
	public String getOriginalFormula() {
		return formula;
	}

	public String getFormula() {
		if (subType == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		switch (subType) {
		case "AND":
			//str.append("BOEX(\\\"" + ident + ".val\\\",p1,\\\"");
			str.append("BOEX(\\\"" + ident + ".val\\\",");
			for (int i = 1; i <= tags.size(); i++) {
				if (i != 1) {
					str.append("&");
				}
				str.append("p").append(2 * i - 1);
			}
			//str.append("\\\"");
			//for (int i = tags.size() + 1; i <= 2 * tags.size(); i++) {
			for (int i = 1; i <= tags.size(); i++) {
				str.append(",p").append(2 * i );
				//str.append(",p").append(3 * i);
			}
			//for (String tag : tags) {
			//	str.append(",\\\"").append(tag).append("\\\"");
			//}
			str.append(")");
			return str.toString();
		case "OR":
			//str.append("BOEX(\\\"" + ident + ".val\\\",p1,\\\"");
			str.append("BOEX(\\\"" + ident + ".val\\\",");
			for (int i = 1; i <= tags.size(); i++) {
				if (i != 1) {
					str.append("|");
				}
				str.append("p").append(2 * i - 1);
			}
			//str.append("\\\"");
			//for (int i = tags.size() + 1; i <= 2 * tags.size(); i++) {
			for (int i = 1; i <= tags.size(); i++) {
				str.append(",p").append(2 * i);
			}
			//for (String tag : tags) {
			//	str.append(",\\\"").append(tag).append("\\\"");
			//}
			str.append(")");
			return str.toString();
		case "BOEX":
			//str.append("BOEX(\\\"" + ident + ".val\\\",p1,\\\"");
			str.append("BOEX(\\\"" + ident + ".val\\\",");
			String newFormula = formula.toLowerCase();
			//newFormula = newFormula.replaceAll("p", "a");
			newFormula = FormulaUtil.changeParameters(newFormula, tags.size());
			str.append(newFormula);
			//str.append("\\\"");
			//for (int i = tags.size() + 1; i <= 2 * tags.size(); i++) {
			//	str.append(",p").append(i);
			//}
			//for (String tag : tags) {
			//	str.append(",\\\"").append(tag).append("\\\"");
			//}
			//for (int i = 2; i <= 2 * tags.size(); i += 2) {
			//	str.append(",p").append(i);
			//}
			for (int i = 2; i <= 2 * tags.size(); i += 2) {
				str.append(",p").append(i);
			}
			str.append(")");
			return str.toString();
		case "TOR":
			return "!p1";
		case "HHA":
		case "HIA":
		case "LOA":
		case "LLA":
			if (tags.get(0) != null) {
				str.append("SDS_isAlert(\\\"").append(ident);
				str.append(".val\\\",\\\"").append(subType);
				str.append("\\\", p1, \\\"").append(tags.get(0));
				str.append(".val\\\")");
				return str.toString();
			}
		}
		return "";
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return (getIdent() != null) && (!getFormula().isEmpty());
	}
}
