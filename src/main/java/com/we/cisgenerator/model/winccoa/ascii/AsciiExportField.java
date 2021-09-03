package com.we.cisgenerator.model.winccoa.ascii;

import java.util.ArrayList;
import java.util.List;

public enum AsciiExportField {
	DpType("DpType", "TypeName"),
	Datapoint("Datapoint/DpId", "DpName", "TypeName", "ID"),
	Aliases("Aliases/Comments", "AliasId", "AliasName", "CommentName"),
	DpValue("DpValue", "Manager/User", "ElementName", "TypeName", "_original.._value",	"_original.._status64",
			"_original.._stime"),
	DpDefaultValue("DpDefaultValue", "Manager/User", "ElementName",	"TypeName",	"_default.._type",
			"_default.._value",	"_default.._set_ibit", "_default.._set_pvrange"),
	PvssRangeCheck("PvssRangeCheck", "Manager/User", "ElementName", "TypeName", "_pv_range.._type",
			"_pv_range.._ignor_inv", "_pv_range.._neg", "_pv_range.._min", "_pv_range.._max",
			"_pv_range.._incl_min", "_pv_range.._incl_max", "_pv_range.._set", "_pv_range.._match"),
	AlertValue("AlertValue", "Manager/User", "ElementName",	"TypeName",	"DetailNr",	"_alert_hdl.._type",
			"_alert_hdl.._l_limit", "_alert_hdl.._u_limit", "_alert_hdl.._l_incl", "_alert_hdl.._u_incl",
			"_alert_hdl.._panel", "_alert_hdl.._panel_param", "_alert_hdl.._help", "_alert_hdl.._min_prio",
			"_alert_hdl.._class", "_alert_hdl.._text", "_alert_hdl.._active", "_alert_hdl.._orig_hdl",
			"_alert_hdl.._ok_range", "_alert_hdl.._hyst_type", "_alert_hdl.._hyst_time",
			"_alert_hdl.._multi_instance", "_alert_hdl.._l_hyst_limit", "_alert_hdl.._u_hyst_limit",
			"_alert_hdl.._text1", "_alert_hdl.._text0", "_alert_hdl.._ack_has_prio", "_alert_hdl.._order",
			"_alert_hdl.._dp_pattern", "_alert_hdl.._dp_list", "_alert_hdl.._prio_pattern",
			"_alert_hdl.._abbr_pattern", "_alert_hdl.._ack_deletes", "_alert_hdl.._non_ack",
			"_alert_hdl.._came_ack", "_alert_hdl.._pair_ack", "_alert_hdl.._both_ack", "_alert_hdl.._impulse",
			"_alert_hdl.._filter_threshold", "_alert_hdl.._went_text", "_alert_hdl.._add_text",
			"_alert_hdl.._status64_pattern", "_alert_hdl.._neg", "_alert_hdl.._status64_match", "_alert_hdl.._match",
			"_alert_hdl.._set"),
	DistributionInfo("DistributionInfo", "Manager/User", "ElementName", "TypeName", "_distrib.._type",
			"_distrib.._driver"),
	DpConvRawToIngMain("DpConvRawToIngMain", "Manager/User", "ElementName", "TypeName", "DetailNr",
			"_msg_conv.._type", "_msg_conv.._poly_grade", "_msg_conv.._poly_a", "_msg_conv.._poly_b",
			"_msg_conv.._poly_c", "_msg_conv.._poly_d", "_msg_conv.._poly_e", "_msg_conv.._linint_num",
			"_msg_conv.._linint1_x", "_msg_conv.._linint1_y", "_msg_conv.._linint2_x", "_msg_conv.._linint2_y",
			"_msg_conv.._linint3_x", "_msg_conv.._linint3_y", "_msg_conv.._linint4_x", "_msg_conv.._linint4_y",
			"_msg_conv.._linint5_x", "_msg_conv.._linint5_y", "_msg_conv.._null_from", "_msg_conv.._null_to",
			"_msg_conv.._null_res", "_msg_conv.._log_base", "_msg_conv.._round_val", "_msg_conv.._round_inv",
			"_msg_conv.._trig_lim", "_msg_conv.._trig_up", "_msg_conv.._imp_edge", "_msg_conv.._imp_rstval"),
	DpFunction("DpFunction", "Manager/User", "ElementName", "TypeName", "_dp_fct.._type", "_dp_fct.._param",
			"_dp_fct.._fct", "_dp_fct.._global", "_dp_fct.._old_new_compare", "_dp_fct.._stat_type",
			"_dp_fct.._interval", "_dp_fct.._time", "_dp_fct.._day_of_week", "_dp_fct.._day", "_dp_fct.._month",
			"_dp_fct.._delay", "_dp_fct.._read_archive", "_dp_fct.._inv_func", "_dp_fct.._inv_limit",
			"_dp_fct.._def_func", "_dp_fct.._def_limit", "_dp_fct.._user1_func", "_dp_fct.._user1_limit",
			"_dp_fct.._user2_func", "_dp_fct.._user2_limit", "_dp_fct.._user3_func", "_dp_fct.._user3_limit",
			"_dp_fct.._user4_func", "_dp_fct.._user4_limit", "_dp_fct.._user5_func", "_dp_fct.._user5_limit",
			"_dp_fct.._user6_func", "_dp_fct.._user6_limit", "_dp_fct.._user7_func", "_dp_fct.._user7_limit",
			"_dp_fct.._user8_func", "_dp_fct.._user8_limit", "_dp_fct.._interm_res", "_dp_fct.._interm_res_cyc"),
	PeriphAddrMain("PeriphAddrMain", "Manager/User", "ElementName", "TypeName", "_address.._type",
			"_address.._reference", "_address.._poll_group", "_address.._connection", "_address.._offset",
			"_address.._subindex", "_address.._direction",	"_address.._internal",	"_address.._lowlevel",
			"_address.._active",	"_address.._start",	"_address.._interval",	"_address.._reply",
			"_address.._datatype",	"_address.._drv_ident"),
	DbArchiveInfo("DbArchiveInfo", "Manager/User", "ElementName", "TypeName", "DetailNr", "_archive.._type",
			"_archive.._archive", "_archive.._class", "_archive.._interv", "_archive.._interv_type",
			"_archive.._std_type", "_archive.._std_tol", "_archive.._std_time", "_archive.._round_val",
			"_archive.._round_inv");
	
	private AsciiExportField(String name, String... columns){
		exportName = name;
		this.columns = new ArrayList<String>();
		for(String column : columns){
			this.columns.add(column);
		}
	}
	
	private final List<String> columns;
	private final String exportName;
	
	public List<String> getColumns(){
		return columns;
	}
	
	public String getExportName(){
		return exportName;
	}
}
