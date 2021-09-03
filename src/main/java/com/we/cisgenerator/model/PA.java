package com.we.cisgenerator.model;

import com.we.cisgenerator.model.internal.AnalogSignal;
import com.we.cisgenerator.util.FormulaUtil;
import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.TableAccess;

import java.util.ArrayList;
import java.util.List;

@TableAccess(name = "Расчетные параметры PA")
public class PA extends AnalogSignal {

	public PA() {
		this.tags = new ArrayList<String>();
	}

	private String ident;
	private String type;
	private String name;
	private String formula;
	private final List<String> tags;

	public String getIdent() {
		return ident;
	}

	@ColumnAccess(columnName = "ИДЕНТИФИКАТОР")
	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getType() {
		return type;
	}

	@ColumnAccess(columnName = "ZPTSUBTYP")
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	@ColumnAccess(columnName = "НАИМЕНОВАНИЕ ПАРАМЕТРА")
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTags() {
		return tags;
	}

	@ColumnAccess(columnName = "ZINPNT", count = 20)
	public void addTag(String tag) {
		if (tag != null && !tag.isEmpty()) {
			tags.add(tag);
		}
	}

	public String getOriginalFormula() {
		return formula;
	}
	
	public String getFormula() {
		if (type == null) {
			return formula;
		}
		StringBuilder str = new StringBuilder(type);
		switch (type) {
		case "SDS_isBad":
			str.append("(\\\"" + ident + ".val\\\",p1)");
			return str.toString();
		case "BCD":
			str.append("(\\\"" + ident + ".val\\\",");
			return addStrTags(str, true);
		case "TACC":
		case "ROC":
			str.append("(\\\"" + ident + ".val\\\",p1");
			for (String tag : tags) {
				str.append(",\\\"").append(tag).append("\\\"");
			}
			str.append(")");
			return str.toString();
		case "DAF":
			str.append("(\\\"" + ident + ".val\\\",p1");
			for (String tag : tags) {
				if (!isTagConstant(tag)){
					str.append(",\\\"").append(tag).append("\\\"");
				} else {
					str.append(",").append(Constant.valueOf(tag).getValue());
				}
			}
			str.append(")");
			return str.toString();
		case "DAVE":
		case "DMAX":
		case "DMIN":
		case "ADD":
		case "LOG":
		case "MEAN":
		case "SINV":
		case "SUB":
			str.append("(\\\"" + ident + ".val\\\",");
			break;
		case "ASME":
			str = new StringBuilder("PAEX");
		case "PAEX":
			//str.append("(\\\"" + ident + ".val\\\",p1,\\\"");
			str.append("(\\\"" + ident + ".val\\\",");
			String newFormula = formula.toLowerCase();
			//newFormula = newFormula.replaceAll("p", "a");
			newFormula = FormulaUtil.changeOperatorToFunction(newFormula, "^", "pow");
			newFormula = FormulaUtil.changeParameters(newFormula, tags.size());
			str.append(newFormula);
			//str.append("\\\"");
			for (int i = 2; i <= 2 * tags.size(); i += 2) {
				str.append(",p").append(i);
			}
			/*for (int i = 1; i <= tags.size(); i++) {
				//str.append(",\\\"").append(tag).append("\\\"");
				str.append(",p").append(i * 2);
			}*/
			str.append(")");
			return str.toString();
		}

		return addStrTags(str, false);
	}

	@ColumnAccess(columnName = "ZPAEXPR")
	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Float[] getXLAlarm() {
		return xLAlarm;
	}

	private String addStrTags(StringBuilder str, boolean appendStatusConstant) {
		int i = 0;
		for (String tag : tags) {
			if (!isTagConstant(tag)) {
				str.append("p" + (i * 2 + 1));
				str.append(",p" + (i * 2 + 2));
				str.append(",");
				i++;
			} else {
				str.append(Constant.valueOf(tag).getValue());
				str.append(",");
				if (appendStatusConstant) {
					str.append(0);
					str.append(",");
				}
			}
		}
		str.deleteCharAt(str.length() - 1);
		str.append(")");
		return str.toString();
	}

	public String getExportTags() {
		StringBuilder str = new StringBuilder();
		boolean notFirst = false;
		switch (type) {
		case "SDS_isBad":
			for (String tag : getTags()) {
				if (notFirst) {
					str.append(", ");
				}
				str.append(tag + ".val:_original.._bad");
				notFirst = true;
			}
			return str.toString();
		case "ADD":
		case "LOG":
		case "SUB":
		case "SINV":
		case "BCD":
		case "MEAN":
			for (String tag : getTags()) {
				if (!isTagConstant(tag)) {
					if (notFirst) {
						str.append(", ");
					}
					str.append(tag + ".val:_online.._value,");
					str.append(tag + ".val:_original.._online_bad");
					notFirst = true;
				}
			}
			return str.toString();
		case "DAVE":
		case "DMAX":
		case "DMIN":
			for (String tag : getTags()) {
				if (!isTagConstant(tag)) {
					if (notFirst) {
						str.append(", ");
					}
					str.append(tag + ".val:_online.._value,");
					str.append(tag + ".val:_original.._status");
					notFirst = true;
				}
			}
			return str.toString();
		case "ROC":
		case "DAF":
			return "PA_Heartbeat.val:_original.._value";
		case "TACC":
			/*
			 * for (String tag : getTags()) { if (notFirst) { str.append(", ");
			 * } str.append(tag + ".val:_online.._value"); notFirst = true; }
			 * for (String tag : getTags()) { str.append(", "); str.append(tag +
			 * ".val:_original.._bad"); }
			 */
			// return str.toString();
			return "_Event.Heartbeat:_original.._value";
		case "PAEX":
		case "ASME":
			/*for (String tag : getTags()) {
				if (notFirst) {
					str.append(", ");
				}
				str.append(tag + ".val:_online.._value");
					notFirst = true;
			}
			for (String tag : getTags()) {
				str.append(", ");
				str.append(tag + ".val:_original.._bad");
			}*/
			for (String tag : getTags()) {
				if (!isTagConstant(tag)) {
					if (notFirst) {
						str.append(", ");
					}
					str.append(tag + ".val:_online.._value,");
					str.append(tag + ".val:_original.._online_bad");
					notFirst = true;
				}
			}
			return str.toString();
		}
		return str.toString();

	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("PA: ");
		str.append(ident);
		str.append(", type: " + type);
		str.append(", name: " + name);
		return str.toString();

	}

	public static boolean isTagConstant(String tag) {
		for (Constant constant : Constant.values()) {
			if (constant.name().equals(tag)) {
				return true;
			}
		}
		return false;
	}

	public enum Constant {
		XL4YES("1"), XL4NO("0"), XR4C1("1"), XR4C2("2"), XR4C3("3"), XR4C4("4"), XR4C6("6"), XR4FLT_C01(
				"10"), XRT4FLT_C02("60");
		// XR4CSF_QL - проверка достоверности
		// XR4FLT_C01 - константа фильтра 10.0
		// XR4FLT_C02 - константа фильтра 60.0
		// XR4ALARM - наличие сигнализации

		private Constant(String value) {
			this.value = value;
		}

		private String value;

		public String getValue() {
			return value;
		}

	}
}
