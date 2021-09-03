package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.*;
import com.we.cisgenerator.model.Module;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

public class SDS_PLC extends PLC implements Dp {
	
	public SDS_PLC(String dpName){
		super(dpName);
	}
	
	public SDS_PLC(PLC plc){
		super(plc.getId());
		setPosition(plc.getPosition());
		setReserved(plc.isReserved());
		setCabinet(plc.getCabinet());
		setIpAddress(plc.getIpAddress());
		for (Module module : plc.getModules()){
			addModule(module);
		}
		for (Entry<Integer, CCDCom> entry : plc.getComParameters().entrySet()){
			putCCDCom(entry.getKey(), entry.getValue());
		}
		setDriverModbus(plc.getDriverModbus());
		setDriverIEC(plc.getDriverIEC());
	}

	@Override
	public String getExportField(AsciiExportField field) {
		switch(field){
		case AlertValue:
		case Aliases:
		case DbArchiveInfo:
		case DistributionInfo:
		case DpDefaultValue:
		case DpFunction:	
		case PeriphAddrMain:
		case PvssRangeCheck:	
		case DpConvRawToIngMain:	
			return "";
		case Datapoint:
			return getPosition() + "\tSDS_PLC\t" + /*dp.getId() +*/ "\r\n";
		case DpType:
			return "SDS_PLC.SDS_PLC	1#1\r\n\tposition\t25#2\r\n\tmodule\t9#3\r\n"
					+ "\tcabinet\t25#4\r\n\tdriverModbus\t20#5\r\n\tdriverIEC\t20#6\r\n";
		case DpValue:
			String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"));
			StringBuilder strModules = new StringBuilder();
			for (Module module : getModules()){
				strModules.append("\"" + module.getId() + ";" + module.getName() + ";" + module.getSectionName() + "\", ");
			}
			if (strModules.length() > 1){
				strModules.deleteCharAt(strModules.length() - 1);
				strModules.deleteCharAt(strModules.length() - 1);
			}
			return 	// Position
					"ASC (1)/0\t" + getPosition() + ".position\tSDS_PLC\t" + getPosition() + "\t0x8300000000000101\t" + dateTimeStr + "\r\n" +
					// Module
					"ASC (1)/0\t" + getPosition() + ".module\tSDS_PLC\t" + strModules.toString() + "\t0x8300000000000101\t" + dateTimeStr + "\r\n" +
					// Cabinet
					"ASC (1)/0\t" + getPosition() + ".cabinet\tSDS_PLC\t" + getCabinet().getId() + ";" + getCabinet().getName() + "\t0x8300000000000101\t" + dateTimeStr + "\r\n" +
					// DriverModbus
					"ASC (1)/0\t" + getPosition() + ".driverModbus\tSDS_PLC\t" + getDriverModbus() + "\t0x8300000000000101\t" + dateTimeStr + "\r\n" +
					// DriverIEC
					"ASC (1)/0\t" + getPosition() + ".driverIEC\tSDS_PLC\t" + getDriverIEC() + "\t0x8300000000000101\t" + dateTimeStr + "\r\n";
		}
		return null;
	}

	@Override
	public String getDpName() {
		return getPosition();
	}

	@Override
	public String getSDSType(){
		return "SDS_PLC";
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return getPosition() != null;
	}
}
