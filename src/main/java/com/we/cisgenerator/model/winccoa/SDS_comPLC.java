package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.PLC;
import com.we.cisgenerator.model.winccoa.ascii.*;
import com.we.cisgenerator.model.winccoa.ascii.AlertValue.AlertElement;

public class SDS_comPLC implements Dp {

	private final static int FIRST_COM_PORT = 2;
	private final static int LAST_COM_PORT = 6;
	private final static int FIRST_ADDRESS = 17288;
	private final static String TYPE_NAME = "SDS_comPLC";
	private final static String[] ALERT_TEXT = {"NO_ERROR","LIB_NOT_SUPPORT","COM_OUTSIDE","NOT_ASSIGNED",
			"ASSIGNED_DIFFERENT","ALREADY_OPEN","ALREADY_CLOSED","NOT_OPENED","WRITE_STILL_ACTIVE","PARAMS_NOT_SUPPORT",
			"SETTING_NOT_READ","LIB_NOT_SUPPORT_SETTING","CANNOT_INIT","ERROR_WRITE","CONTENT_NOT_SENT","INTERNAL_ERROR"};
	private final PLC plc;
	private String exportName = null;
	
	public SDS_comPLC(PLC plc){
		this.plc = plc;
	}
	
	@Override
	public String getExportField(AsciiExportField field) {
		StringBuilder str = new StringBuilder();
		switch(field){
		case AlertValue:
			AlertValue alertValue = AlertValue.createAlertValueType13();
			alertValue.setElement(AlertElement.TypeName, TYPE_NAME);
			AlertValue channelAlert = AlertValue.createAlertValueType13();
			channelAlert.setElement(AlertElement.TypeName, TYPE_NAME)
				.setElement(AlertElement.alert_hdl_type, "12")
				.setElement(AlertElement._alert_hdl_class, "danger.")
				.setElement(AlertElement._alert_hdl_ok_range, "0")
				.setElement(AlertElement._alert_hdl_hyst_type, "0")
				.setElement(AlertElement._alert_hdl_hyst_time, "01.01.1970 00:00:00.000")
				.setElement(AlertElement._alert_hdl_text0, "lt:1 LANG:10027 \"ОК\"");
				
			for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
				alertValue.setElement(AlertElement.ElementName, getExportName() + i + ".comStatus");
				str.append(alertValue.toExportString());
				
				AlertValue alertValue2 = AlertValue.createAlertValueType5();
				alertValue2.setElement(AlertElement.TypeName, TYPE_NAME);
				alertValue2.setElement(AlertElement.ElementName, getExportName() + i + ".comStatus")
					.setElement(AlertElement._alert_hdl_neg, "0");
				for (int j = 1; j <= 16; j++){
					alertValue2.setElement(AlertElement.DetailNr, Integer.toString(j));
					if (j >= 2){
						alertValue2.setElement(AlertElement._alert_hdl_class, "danger.");
						alertValue2.setElement(AlertElement._alert_hdl_match, "\"" + Integer.toString(j - 1) + "\"");
					} else {
						alertValue2.setElement(AlertElement._alert_hdl_match, "\"*\"");
					}
					alertValue2.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"" + ALERT_TEXT[j - 1] + "\"");
					str.append(alertValue2.toExportString());
				}

				AlertValue lcdmAlert13 = AlertValue.createAlertValueType13();
				lcdmAlert13.setElement(AlertElement.TypeName, TYPE_NAME);
				AlertValue lcdmAlert5 = AlertValue.createAlertValueType5();
				lcdmAlert5.setElement(AlertElement.TypeName, TYPE_NAME);
				lcdmAlert5.setElement(AlertElement._alert_hdl_neg, "0");
				for (int j = 1; j <= 6; j++){
					lcdmAlert13.setElement(AlertElement.ElementName, getExportName() + i + ".lcdm" + j + ".lcdmStatus");
					str.append(lcdmAlert13.toExportString());
					lcdmAlert5.setElement(AlertElement.ElementName, getExportName() + i + ".lcdm" + j + ".lcdmStatus")
						.setElement(AlertElement.DetailNr, "1")
						.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"OK\"")
						.setElement(AlertElement._alert_hdl_match, "\"*\"");
					str.append(lcdmAlert5.toExportString());
					lcdmAlert5.setElement(AlertElement.DetailNr, "2")
						.setElement(AlertElement._alert_hdl_class, "danger.")
						.setElement(AlertElement._alert_hdl_text, "lt:1 LANG:10027 \"Не отвечает (нет связи)\"")
						.setElement(AlertElement._alert_hdl_match, "\"*\"")
						.setElement(AlertElement._alert_hdl_match, "\"2\"");
					str.append(lcdmAlert5.toExportString());
					for (int k = 1; k <= 16; k++){
						channelAlert.setElement(AlertElement.ElementName, getExportName() + i + ".lcdm" + j + ".channelAlert.ch" + k)
							.setElement(AlertElement._alert_hdl_text1, "lt:1 LANG:10027 \"Ошибка канала " + k + "\"");
						str.append(channelAlert.toExportString());
					}
				}
			}
			return str.toString();
		case Aliases:
			return "";
		case Datapoint:
			for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
				str.append(getExportName()).append(i);
				str.append("\t").append(TYPE_NAME).append("\t\r\n");
			}
			return str.toString();
		case DbArchiveInfo:
			return "";
		case DistributionInfo:
			ExportField<DistributionInfoE> distrInfo = new ExportField<>(DistributionInfoE.values().length);
			distrInfo.setElement(DistributionInfoE.TypeName, TYPE_NAME)
				.setElement(DistributionInfoE._distrib_type, "56")
				.setElement(DistributionInfoE._distrib_driver, "\\1");
			for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
				distrInfo.setElement(DistributionInfoE.ElementName, getExportName() + i + ".comStatus");
				str.append(distrInfo.toExportString());
				for (int j = 1; j <= 6; j++){
					distrInfo.setElement(DistributionInfoE.ElementName, getExportName() + i + ".lcdm" + j + ".lcdmStatus");
					str.append(distrInfo.toExportString());
					distrInfo.setElement(DistributionInfoE.ElementName, getExportName() + i + ".lcdm" + j + ".channelStatus");
					str.append(distrInfo.toExportString());
				}
			}
			return str.toString();
		case DpDefaultValue:
			return "";
		case DpType:
			return "SDS_comPLC.SDS_comPLC\t1#1\r\n"
					+ "\tcomStatus\t21#22\r\n"
					+ "\tlcdm1\t1#23\r\n"
						+ "\t\tlcdmStatus\t20#44\r\n"
						+ "\t\tchannelStatus\t24#77\r\n"
						+ "\t\tchannelAlert\t1#60\r\n"
							+ "\t\t\tch1\t23#61\r\n"
							+ "\t\t\tch2\t23#62\r\n"
							+ "\t\t\tch3\t23#63\r\n"
							+ "\t\t\tch4\t23#64\r\n"
							+ "\t\t\tch5\t23#65\r\n"
							+ "\t\t\tch6\t23#66\r\n"
							+ "\t\t\tch7\t23#67\r\n"
							+ "\t\t\tch8\t23#68\r\n"
							+ "\t\t\tch9\t23#69\r\n"
							+ "\t\t\tch10\t23#70\r\n"
							+ "\t\t\tch11\t23#71\r\n"
							+ "\t\t\tch12\t23#72\r\n"
							+ "\t\t\tch13\t23#73\r\n"
							+ "\t\t\tch14\t23#74\r\n"
							+ "\t\t\tch15\t23#75\r\n"
							+ "\t\t\tch16\t23#76\r\n"
					+ "\tlcdm2\t1#26\r\n"
						+ "\t\tlcdmStatus\t20#46\r\n"
						+ "\t\tchannelStatus\t24#59\r\n"
						+ "\t\tchannelAlert\t1#78\r\n"
							+ "\t\t\tch1\t23#87\r\n"
							+ "\t\t\tch2\t23#88\r\n"
							+ "\t\t\tch3\t23#89\r\n"
							+ "\t\t\tch4\t23#90\r\n"
							+ "\t\t\tch5\t23#91\r\n"
							+ "\t\t\tch6\t23#92\r\n"
							+ "\t\t\tch7\t23#93\r\n"
							+ "\t\t\tch8\t23#94\r\n"
							+ "\t\t\tch9\t23#95\r\n"
							+ "\t\t\tch10\t23#96\r\n"
							+ "\t\t\tch11\t23#97\r\n"
							+ "\t\t\tch12\t23#98\r\n"
							+ "\t\t\tch13\t23#99\r\n"
							+ "\t\t\tch14\t23#100\r\n"
							+ "\t\t\tch15\t23#101\r\n"
							+ "\t\t\tch16\t23#102\r\n"
					+ "\tlcdm3\t1#29\r\n"
						+ "\t\tlcdmStatus\t20#48\r\n"
						+ "\t\tchannelStatus\t24#79\r\n"
						+ "\t\tchannelAlert\t1#80\r\n"
							+ "\t\t\tch1\t23#103\r\n"
							+ "\t\t\tch2\t23#104\r\n"
							+ "\t\t\tch3\t23#105\r\n"
							+ "\t\t\tch4\t23#106\r\n"
							+ "\t\t\tch5\t23#107\r\n"
							+ "\t\t\tch6\t23#108\r\n"
							+ "\t\t\tch7\t23#109\r\n"
							+ "\t\t\tch8\t23#110\r\n"
							+ "\t\t\tch9\t23#111\r\n"
							+ "\t\t\tch10\t23#112\r\n"
							+ "\t\t\tch11\t23#113\r\n"
							+ "\t\t\tch12\t23#114\r\n"
							+ "\t\t\tch13\t23#115\r\n"
							+ "\t\t\tch14\t23#116\r\n"
							+ "\t\t\tch15\t23#117\r\n"
							+ "\t\t\tch16\t23#118\r\n"
					+ "\tlcdm4\t1#32\r\n"
						+ "\t\tlcdmStatus\t20#50\r\n"
						+ "\t\tchannelStatus\t24#81\r\n"
						+ "\t\tchannelAlert\t1#82\r\n"
							+ "\t\t\tch1\t23#119\r\n"
							+ "\t\t\tch2\t23#120\r\n"
							+ "\t\t\tch3\t23#121\r\n"
							+ "\t\t\tch4\t23#122\r\n"
							+ "\t\t\tch5\t23#123\r\n"
							+ "\t\t\tch6\t23#124\r\n"
							+ "\t\t\tch7\t23#125\r\n"
							+ "\t\t\tch8\t23#126\r\n"
							+ "\t\t\tch9\t23#127\r\n"
							+ "\t\t\tch10\t23#128\r\n"
							+ "\t\t\tch11\t23#129\r\n"
							+ "\t\t\tch12\t23#130\r\n"
							+ "\t\t\tch13\t23#131\r\n"
							+ "\t\t\tch14\t23#132\r\n"
							+ "\t\t\tch15\t23#133\r\n"
							+ "\t\t\tch16\t23#134\r\n"
					+ "\tlcdm5\t1#35\r\n"
						+ "\t\tlcdmStatus\t20#52\r\n"
						+ "\t\tchannelStatus\t24#83\r\n"
						+ "\t\tchannelAlert\t1#84\r\n"
							+ "\t\t\tch1\t23#135\r\n"
							+ "\t\t\tch2\t23#136\r\n"
							+ "\t\t\tch3\t23#137\r\n"
							+ "\t\t\tch4\t23#138\r\n"
							+ "\t\t\tch5\t23#139\r\n"
							+ "\t\t\tch6\t23#140\r\n"
							+ "\t\t\tch7\t23#141\r\n"
							+ "\t\t\tch8\t23#142\r\n"
							+ "\t\t\tch9\t23#143\r\n"
							+ "\t\t\tch10\t23#144\r\n"
							+ "\t\t\tch11\t23#145\r\n"
							+ "\t\t\tch12\t23#146\r\n"
							+ "\t\t\tch13\t23#147\r\n"
							+ "\t\t\tch14\t23#148\r\n"
							+ "\t\t\tch15\t23#149\r\n"
							+ "\t\t\tch16\t23#150\r\n"
					+ "\tlcdm6\t1#38\r\n"
						+ "\t\tlcdmStatus\t20#54\r\n"
						+ "\t\tchannelStatus\t24#85\r\n"
						+ "\t\tchannelAlert\t1#86\r\n"
							+ "\t\t\tch1\t23#151\r\n"
							+ "\t\t\tch2\t23#152\r\n"
							+ "\t\t\tch3\t23#153\r\n"
							+ "\t\t\tch4\t23#154\r\n"
							+ "\t\t\tch5\t23#155\r\n"
							+ "\t\t\tch6\t23#156\r\n"
							+ "\t\t\tch7\t23#157\r\n"
							+ "\t\t\tch8\t23#158\r\n"
							+ "\t\t\tch9\t23#159\r\n"
							+ "\t\t\tch10\t23#160\r\n"
							+ "\t\t\tch11\t23#161\r\n"
							+ "\t\t\tch12\t23#162\r\n"
							+ "\t\t\tch13\t23#163\r\n"
							+ "\t\t\tch14\t23#164\r\n"
							+ "\t\t\tch15\t23#165\r\n"
							+ "\t\t\tch16\t23#166\r\n";
			
		case DpValue:
			return "";
		case PeriphAddrMain:
			ExportField<PeriphAddrE> pAddr = new ExportField<>(PeriphAddrE.values().length);
			pAddr.setElement(PeriphAddrE.TypeName, TYPE_NAME)
				.setElement(PeriphAddrE._address_type, "16")
				.setElement(PeriphAddrE._address_poll_group, "_DiagCyclePLC")
				.setElement(PeriphAddrE._address_offset, "0")
				.setElement(PeriphAddrE._address_subindex, "0")
				.setElement(PeriphAddrE._address_direction, "\\4")
				.setElement(PeriphAddrE._address_internal, "0")
				.setElement(PeriphAddrE._address_lowlevel, "0")
				.setElement(PeriphAddrE._address_active, "1")
				.setElement(PeriphAddrE._address_start, "01.01.1970 00:00:00.000")
				.setElement(PeriphAddrE._address_interval, "01.01.1970 00:00:00.000")
				.setElement(PeriphAddrE._address_reply, "01.01.1970 00:00:00.000")
				.setElement(PeriphAddrE._address_drv_ident, "\"MODBUS\"");
			for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
				pAddr.setElement(PeriphAddrE.ElementName, getExportName() + i + ".comStatus")
					.setElement(PeriphAddrE._address_datatype, "563")
					.setElement(PeriphAddrE._address_reference, "\"M." + plc.getDriverModbus() + ".3." + (FIRST_ADDRESS + (i - FIRST_COM_PORT) * 26) + "\"");
				str.append(pAddr.toExportString());
				for (int j = 1; j <= 6; j++){
					pAddr.setElement(PeriphAddrE.ElementName, getExportName() + i + ".lcdm" + j + ".lcdmStatus")
						.setElement(PeriphAddrE._address_datatype, "563")
						.setElement(PeriphAddrE._address_reference, "\"M." + plc.getDriverModbus() + ".3." + (FIRST_ADDRESS + (i - FIRST_COM_PORT) * 26 + 2 + (j - 1) * 4) + "\"");
					str.append(pAddr.toExportString());
					pAddr.setElement(PeriphAddrE.ElementName, getExportName() + i + ".lcdm" + j + ".channelStatus")
						.setElement(PeriphAddrE._address_datatype, "562")
						.setElement(PeriphAddrE._address_reference, "\"M." + plc.getDriverModbus() + ".3." + (FIRST_ADDRESS + (i - FIRST_COM_PORT) * 26 + 3 + (j - 1) * 4) + "\"");
					str.append(pAddr.toExportString());
				}
			}
			return str.toString();
		case DpFunction:
			ExportField<DpFunctionE> dpFunction = new ExportField<>(DpFunctionE.values().length);
			dpFunction.setElement(DpFunctionE.TypeName, TYPE_NAME)
				.setElement(DpFunctionE._dp_fct_type, "60")
				.setElement(DpFunctionE._dp_fct_old_new_compare, "0");
			for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
				for (int j = 1; j <= 6; j++){
					dpFunction.setElement(DpFunctionE._dp_fct_param, getExportName() + i + ".lcdm" + j + ".channelStatus:_online.._value");
					for (int k = 1; k <= 16; k++){
						dpFunction.setElement(DpFunctionE.ElementName, getExportName() + i + ".lcdm" + j + ".channelAlert.ch" + k)
							.setElement(DpFunctionE._dp_fct_fct, "\"(p1 & 0x" + Integer.toHexString(1 << (k - 1)) + ") & 0x" + Integer.toHexString(1 << (k - 1)) + "0000\"");
						str.append(dpFunction.toExportString());
					}
				}
			}
			return str.toString();
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
		return plc.getCabinet().getName() + "_" + plc.getPosition();
	}

	@Override
	public String getSDSType() {
		return TYPE_NAME;
	}

	@Override
	public boolean isCorrect() {
		return plc != null;
	}
	
	private String getExportName(){
		if (exportName == null){
			exportName = plc.getCabinet().getShortName() + "_" + plc.getPosition() + "_Com";
		}
		return exportName;
	}

}
