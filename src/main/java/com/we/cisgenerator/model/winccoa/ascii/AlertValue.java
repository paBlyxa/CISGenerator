package com.we.cisgenerator.model.winccoa.ascii;


public class AlertValue extends ExportField<AlertValue.AlertElement> {

	private final static int COUNT_ELEMENTS = 45;

	public AlertValue() {
		super(COUNT_ELEMENTS);
	}

	public AlertValue setElement(AlertElement element, String value){
		setElement(element.ordinal(), value);
		return this;
	}
	
	public static AlertValue createAlertValueType13(){
		AlertValue alertValue = new AlertValue();
		alertValue.setElement(AlertElement.alert_hdl_type, "13")
			.setElement(AlertElement._alert_hdl_panel, "\"\"")
			.setElement(AlertElement._alert_hdl_panel_param, "\"\"")
			.setElement(AlertElement._alert_hdl_help, "lt:1 LANG:10027 \"\"")
			.setElement(AlertElement._alert_hdl_min_prio, "\\0")
			.setElement(AlertElement._alert_hdl_active, "1")
			.setElement(AlertElement._alert_hdl_orig_hdl, "1")
			.setElement(AlertElement._alert_hdl_multi_instance, "0")
			.setElement(AlertElement._alert_hdl_impulse, "0");
		return alertValue;
	}
	
	public static AlertValue createAlertValueType5(){
		AlertValue alertValue = new AlertValue();
		alertValue.setElement(AlertElement.alert_hdl_type, "5")
			.setElement(AlertElement._alert_hdl_hyst_type, "0")
			.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
			.setElement(AlertElement._alert_hdl_went_text, "lt:1 LANG:10027 \"\"")
			.setElement(AlertElement._alert_hdl_status64_pattern, "0x0")
			.setElement(AlertElement._alert_hdl_status64_match, "\"\"");
		return alertValue;
	}

	/*
	 * public AlertValue setElementName(String elementName){ setElement(1,
	 * elementName); return this; }
	 * 
	 * public AlertValue setTypeName(String typeName){ setElement(2, typeName);
	 * return this; }
	 * 
	 * public AlertValue setDetailNr(String detailNr){ setElement(3, detailNr);
	 * return this; }
	 * 
	 * public AlertValue setAlertHdlType(String alertHdlType){ setElement(4,
	 * alertHdlType); return this; }
	 * 
	 * public AlertValue setAlertHdlLLimit(String alertHdlLLimit){ setElement(5,
	 * alertHdlLLimit); return this; }
	 * 
	 * public AlertValue setAlertHdlHLimit(String alertHdlHLimit){ setElement(6,
	 * alertHdlHLimit); return this; }
	 * 
	 * public AlertValue setAlertHdlLIncl(Boolean include){ setElement(7,
	 * include ? "1" : "0"); return this; }
	 * 
	 * public AlertValue setAlertHdlHIncl(Boolean include){ setElement(8,
	 * include ? "1" : "0"); return this; }
	 * 
	 * public AlertValue setAlertHdlPanel(String alertHdlPanel){ setElement(9,
	 * alertHdlPanel); return this; }
	 * 
	 * public AlertValue setAlertHdlPanelParam(String alertHdlPanelParam){
	 * setElement(10, alertHdlPanelParam); return this; }
	 * 
	 * public AlertValue setAlertHdlHelp(String alertHdlHelp){ setElement(11,
	 * alertHdlHelp); return this; }
	 * 
	 * public AlertValue setAlertHdlMinPrio(String alertHdlMinPrio){
	 * setElement(12, alertHdlMinPrio); return this; }
	 * 
	 * public AlertValue setAlertHdlClass(String alertHdlClass){ setElement(13,
	 * alertHdlClass); return this; }
	 * 
	 * public AlertValue setAlertHdlText(String alertHdlText){ setElement(14,
	 * alertHdlText); return this; }
	 * 
	 * public AlertValue setAlertHdlActive(String alertHdlActive){
	 * setElement(15, alertHdlActive); return this; }
	 * 
	 * public AlertValue setAlertHdlOrigHdl(String alertHdlOrigHdl){
	 * setElement(16, alertHdlOrigHdl); return this; }
	 * 
	 * public AlertValue setAlertHdlOkRange(String alertHdlOkRange){
	 * setElement(16, alertHdlOkRange); return this; }
	 */

	public enum AlertElement {

		ManagerUser, ElementName, TypeName, DetailNr, alert_hdl_type, _alert_hdl_l_limit, _alert_hdl_u_limit, _alert_hdl_l_incl, _alert_hdl_u_incl, _alert_hdl_panel, _alert_hdl_panel_param, _alert_hdl_help, _alert_hdl_min_prio, _alert_hdl_class, _alert_hdl_text, _alert_hdl_active, _alert_hdl_orig_hdl, _alert_hdl_ok_range, _alert_hdl_hyst_type, _alert_hdl_hyst_time, _alert_hdl_multi_instance, _alert_hdl_l_hyst_limit, _alert_hdl_u_hyst_limit, _alert_hdl_text1, _alert_hdl_text0, _alert_hdl_ack_has_prio, _alert_hdl_order, _alert_hdl_dp_pattern, _alert_hdl_dp_list, _alert_hdl_prio_pattern, _alert_hdl_abbr_pattern, _alert_hdl_ack_deletes, _alert_hdl_non_ack, _alert_hdl_came_ack, _alert_hdl_pair_ack, _alert_hdl_both_ack, _alert_hdl_impulse, _alert_hdl_filter_threshold, _alert_hdl_went_text, _alert_hdl_add_text, _alert_hdl_status64_pattern, _alert_hdl_neg, _alert_hdl_status64_match, _alert_hdl_match, _alert_hdl_set
	}
}
