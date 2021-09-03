package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.QS;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue.AlertElement;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.model.winccoa.ascii.ExportField;
import com.we.cisgenerator.model.winccoa.ascii.RangeCheckE;

public class SDS_QS extends QS implements Dp {

	private final static String TYPE_NAME = "SDS_QS";
	
	public SDS_QS() {
	}

	@Override
	public String getExportField(AsciiExportField field) {
		switch(field){
		case AlertValue:
			StringBuilder strAlertValue = new StringBuilder();
			AlertValue alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.ElementName, getIdent() + ".val")
				.setElement(AlertElement.alert_hdl_type, "13")
				.setElement(AlertElement._alert_hdl_panel, "\"\"")
				.setElement(AlertElement._alert_hdl_panel_param, "\"\"")
				.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_min_prio, "\\0")
				.setElement(AlertElement._alert_hdl_active, "1")
				.setElement(AlertElement._alert_hdl_orig_hdl, "0")
				.setElement(AlertElement._alert_hdl_multi_instance, "0")
				.setElement(AlertElement._alert_hdl_impulse, "0");
			strAlertValue.append(alertValue.toExportString());
			// Message 1
			alertValue = new AlertValue();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.ElementName, getIdent() + ".val")
				.setElement(AlertElement.DetailNr, "1")
				.setElement(AlertElement.alert_hdl_type, "5")
				.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"" + getMessages().get(0) + "\"")
				.setElement(AlertElement._alert_hdl_hyst_type, "0")
				.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
				.setElement(AlertElement._alert_hdl_went_text, "lt:1 LANG:10027 \"\"")
				.setElement(AlertElement._alert_hdl_status64_pattern, "0x0")
				.setElement(AlertElement._alert_hdl_neg, "0")
				.setElement(AlertElement._alert_hdl_status64_match, "\"\"")
				.setElement(AlertElement._alert_hdl_match, "\"*\"");
			strAlertValue.append(alertValue.toExportString());
			// Message 2
			alertValue.setElement(AlertElement.DetailNr, "2")
				.setElement(AlertElement._alert_hdl_class, "information.")
				.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"" + getMessages().get(1) + "\"")
				.setElement(AlertElement._alert_hdl_match, "\"1\"");
			strAlertValue.append(alertValue.toExportString());
			// Message 3
			alertValue.setElement(AlertElement.DetailNr, "3")
			.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"" + getMessages().get(2) + "\"")
			.setElement(AlertElement._alert_hdl_match, "\"2\"");
		strAlertValue.append(alertValue.toExportString());
		// Message 4
		alertValue.setElement(AlertElement.DetailNr, "4")
			.setElement(AlertElement._alert_hdl_class, "warning.")
			.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"" + getMessages().get(3) + "\"")
			.setElement(AlertElement._alert_hdl_match, "\"3\"");
		strAlertValue.append(alertValue.toExportString());			
		return strAlertValue.toString(); 
		case Aliases:
			return getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + super.getName() + "@%s@\"\r\n";
		case DbArchiveInfo:
			return "ASC (1)/0\t" + getIdent() + ".val\tSDS_QS\t\t45\t1\t\t\t\t\t\t\t\t\r\n"
					+ "ASC (1)/0\t" + getIdent() + ".val\tSDS_QS\t1\t15\t\t_ValueArchive_3\t0\t0\t0\t0\t01.01.1970 00:00:00.000\t0\t0\r\n";
		case DistributionInfo:
		case DpDefaultValue:
		case PvssRangeCheck:
			ExportField<RangeCheckE> rangeCheck = new ExportField<>(RangeCheckE.values().length);
			rangeCheck.setElement(RangeCheckE.TypeName, TYPE_NAME)
				.setElement(RangeCheckE.ElementName, getIdent() + ".val")
				.setElement(RangeCheckE._pv_range_type, "8")
				.setElement(RangeCheckE._pv_range_ignor_inv, "0")
				.setElement(RangeCheckE._pv_range_neg, "1")
				.setElement(RangeCheckE._pv_range_set, "-9999");
			return rangeCheck.toExportString();
		case PeriphAddrMain:
		case DpConvRawToIngMain:
			return "";
		case Datapoint:
			return getIdent() + "\tSDS_QS\t" + "\r\n";
		case DpFunction:
			StringBuilder str = new StringBuilder();
			boolean notFirst = false;
			for (String tag : getParameters()){
				if (notFirst){
					str.append(", ");
				}
				str.append(tag + ".val:_online.._value,");
				str.append(tag + ".val:_original.._online_bad");
				notFirst = true;
			}
			return "ASC (1)/0\t" + getIdent() + ".val\tSDS_QS\t60\t" + str.toString() + "\t\"BCD(\\\"" + getIdent() + ".val\\\",p1,p2,p3,p4,0,0,0,0)\"\t\t0\r\n";
		case DpType:
			return "SDS_QS.SDS_QS\t1#1\r\n"
					+ "\tval\t21#10\r\n"
					+ "\tinfo\t1#11\r\n"
						+ "\t\ttype\t25#14\r\n"
						+ "\t\tsubtype\t25#15\r\n"
						+ "\t\tsystem\t25#16\r\n"
						+ "\t\tremarks\t25#17\r\n"
					+ "\tlogcont\t25#13\r\n";
		case DpValue:
			// TODO Auto-generated method stub
			return "";
		}
		return null;
	}

	@Override
	public String getDpName() {
		return getIdent();
	}

	@Override
	public String getSDSType(){
		return "SDS_QS";
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return getIdent() != null;
	}
}
