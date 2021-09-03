package com.we.cisgenerator.service;

import com.we.cisgenerator.model.winccoa.Dp;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WinCCOAService {

	private final static Logger logger = LoggerFactory.getLogger(WinCCOAService.class);

	public static void writeToFile(File file, List<Dp> listDp, Collection<AsciiExportField> fields, boolean append) {

		logger.debug("Write to file: {}", file.getPath());

		Map<AsciiExportField, StringBuilder> result = new HashMap<>();
		for (AsciiExportField field : fields) {
			result.put(field, new StringBuilder());
		}

		List<String> listTypes = new ArrayList<>();
		for (Dp dp : listDp) {
			if ((dp != null) && (dp.isCorrect())) {

				logger.info("Export dp: {}", dp.toString());

				for (AsciiExportField field : fields) {

					// DpType
					if (field == AsciiExportField.DpType) {

						if (!listTypes.contains(dp.getSDSType())) {
							listTypes.add(dp.getSDSType());
							result.get(field).append(dp.getExportField(AsciiExportField.DpType));
						}
					} else {

						result.get(field).append(dp.getExportField(field));
					}
				}
			} else {
				logger.warn("Unable to export dp '{}'.", dp.toString());
			}
		}
		StringBuilder str = new StringBuilder();
		str.append("# ascii dump of database\r\n\r\n");
		for (AsciiExportField field : AsciiExportField.values()) {
			if (fields.contains(field)) {
				if (result.get(field).length() > 1) {
					str.append("# " + field.getExportName());
					str.append("\r\n");
					str.append(export(field.getColumns()));
					str.append(result.get(field));
					str.append("\r\n");
				}
			}
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
			bw.write(str.toString());
		} catch (IOException e) {
			logger.error("An error occured while writing to file", e);
		}
	}

	private static String export(List<String> strs) {
		StringBuilder strB = new StringBuilder();
		for (String str : strs) {
			strB.append(str);
			strB.append("\t");
		}
		strB.append("\r\n");
		return strB.toString();
	}

}
/*
 * StringBuilder strDpType = new StringBuilder("#DPType\r\nTypeName\r\n");
 * StringBuilder strDpId = new
 * StringBuilder("# Datapoint/DpId\r\nDpName\tTypeName\tID\r\n"); StringBuilder
 * strAliases = new
 * StringBuilder("# Aliases/Comments\r\nAliasId\tAliasName\tCommentName\r\n");
 * StringBuilder strDpValue = new
 * StringBuilder("#DpValue\r\nManager/User\tElementName\t" +
 * "TypeName\t_original.._value\t_original.._status64\t_original.._stime\r\n");
 * StringBuilder strDpDefaultValue = new StringBuilder(
 * "# DpDefaultValue\r\nManager/User\t" +
 * "ElementName\tTypeName\t_default.._type\t_default.._value\t" +
 * "_default.._set_ibit\t_default.._set_pvrange\r\n"); StringBuilder
 * strRangeCheck = new StringBuilder("# PvssRangeCheck\r\nManager/User\t" +
 * "ElementName\tTypeName\t_pv_range.._type\t_pv_range.._ignor_inv\t_pv_range.._neg\t"
 * +
 * "_pv_range.._min\t_pv_range.._max\t_pv_range.._incl_min\t_pv_range.._incl_max\t_pv_range.._set\t"
 * + "_pv_range.._match\r\n"); StringBuilder strAlertValue = new
 * StringBuilder("# AlertValue\r\nManager/User\t" +
 * "ElementName\tTypeName\tDetailNr\t_alert_hdl.._type\t_alert_hdl.._l_limit\t_alert_hdl.._u_limit\t"
 * +
 * "_alert_hdl.._l_incl\t_alert_hdl.._u_incl\t_alert_hdl.._panel\t_alert_hdl.._panel_param\t"
 * +
 * "_alert_hdl.._help\t_alert_hdl.._min_prio\t_alert_hdl.._class\t_alert_hdl.._text\t"
 * +
 * "_alert_hdl.._active\t_alert_hdl.._orig_hdl\t_alert_hdl.._ok_range\t_alert_hdl.._hyst_type\t"
 * +
 * "_alert_hdl.._hyst_time\t_alert_hdl.._multi_instance\t_alert_hdl.._l_hyst_limit\t"
 * +
 * "_alert_hdl.._u_hyst_limit\t_alert_hdl.._text1\t_alert_hdl.._text0\t_alert_hdl.._ack_has_prio\t"
 * +
 * "_alert_hdl.._order\t_alert_hdl.._dp_pattern\t_alert_hdl.._dp_list\t_alert_hdl.._prio_pattern\t"
 * +
 * "_alert_hdl.._abbr_pattern\t_alert_hdl.._ack_deletes\t_alert_hdl.._non_ack\t_alert_hdl.._came_ack\t"
 * +
 * "_alert_hdl.._pair_ack\t_alert_hdl.._both_ack\t_alert_hdl.._impulse\t_alert_hdl.._filter_threshold\t"
 * +
 * "_alert_hdl.._went_text\t_alert_hdl.._add_text\t_alert_hdl.._status64_pattern\t_alert_hdl.._neg\t"
 * + "_alert_hdl.._status64_match\t_alert_hdl.._match\t_alert_hdl.._set\r\n");
 * StringBuilder strDistrInfo = new
 * StringBuilder("# DistributionInfo\r\nManager/User\t" +
 * "ElementName\tTypeName\t_distrib.._type\t_distrib.._driver\r\n");
 * StringBuilder strDpFun = new
 * StringBuilder("# DpFunction\r\nManager/User\tElementName\t" +
 * "TypeName\t_dp_fct.._type\t_dp_fct.._param\t_dp_fct.._fct\t_dp_fct.._global\t_dp_fct.._old_new_compare\t"
 * +
 * "_dp_fct.._stat_type\t_dp_fct.._interval\t_dp_fct.._time\t_dp_fct.._day_of_week\t_dp_fct.._day\t"
 * +
 * "_dp_fct.._month\t_dp_fct.._delay\t_dp_fct.._read_archive\t_dp_fct.._inv_func\t_dp_fct.._inv_limit\t"
 * +
 * "_dp_fct.._def_func\t_dp_fct.._def_limit\t_dp_fct.._user1_func\t_dp_fct.._user1_limit\t"
 * +
 * "_dp_fct.._user2_func\t_dp_fct.._user2_limit\t_dp_fct.._user3_func\t_dp_fct.._user3_limit\t"
 * +
 * "_dp_fct.._user4_func\t_dp_fct.._user4_limit\t_dp_fct.._user5_func\t_dp_fct.._user5_limit\t"
 * +
 * "_dp_fct.._user6_func\t_dp_fct.._user6_limit\t_dp_fct.._user7_func\t_dp_fct.._user7_limit\t"
 * +
 * "_dp_fct.._user8_func\t_dp_fct.._user8_limit\t_dp_fct.._interm_res\t_dp_fct.._interm_res_cyc\r\n"
 * ); StringBuilder strAddr = new
 * StringBuilder("# PeriphAddrMain\r\nManager/User\t" +
 * "ElementName\tTypeName\t_address.._type\t_address.._reference\t_address.._poll_group\t"
 * +
 * "_address.._connection\t_address.._offset\t_address.._subindex\t_address.._direction\t"
 * +
 * "_address.._internal\t_address.._lowlevel\t_address.._active\t_address.._start\t"
 * +
 * "_address.._interval\t_address.._reply\t_address.._datatype\t_address.._drv_ident\r\n"
 * ); StringBuilder strArch = new
 * StringBuilder("# DbArchiveInfo\r\nManager/User\tElementName\tTypeName\tDetailNr\t"
 * +
 * "_archive.._type\t_archive.._archive\t_archive.._class\t_archive.._interv\t_archive.._interv_type\t"
 * +
 * "_archive.._std_type\t_archive.._std_tol\t_archive.._std_time\t_archive.._round_val\t"
 * + "_archive.._round_inv\r\n");
 */