package com.we.cisgenerator.model.internal;

import com.we.jackcess.annotations.ColumnAccess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class XZAlarms {

	private final static int COUNT_ALARM_MODES = 6;
	
	protected final Float[] xLAlarm;
	protected final Float[] xLWarn;
	protected final Float[] xHAlarm;
	protected final Float[] xHWarn;
	protected final String[] zLAlarm;
	protected final String[] zLWarn;
	protected final String[] zHAlarm;
	protected final String[] zHWarn;
	
	public XZAlarms(){
		xLAlarm = new Float[COUNT_ALARM_MODES];
		xLWarn = new Float[COUNT_ALARM_MODES];
		xHAlarm = new Float[COUNT_ALARM_MODES];
		xHWarn = new Float[COUNT_ALARM_MODES];
		zLAlarm = new String[COUNT_ALARM_MODES];
		zLWarn = new String[COUNT_ALARM_MODES];
		zHAlarm = new String[COUNT_ALARM_MODES];
		zHWarn = new String[COUNT_ALARM_MODES];
	}
	
	public abstract String getIdent();
	
	public Float[] getXLAlarm(){
		return xLAlarm;
	}
	@ColumnAccess(columnName = "XLALARM0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addXLAlarm(Float alarmValue, int index){
		xLAlarm[index] = alarmValue;
	}
	
	public Float[] getXLWarn(){
		return xLWarn;
	}
	@ColumnAccess(columnName = "XLWARN0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addXLWarn(Float alarmValue, int index){
		xLWarn[index] = alarmValue;
	}
	
	public Float[] getXHAlarm(){
		return xHAlarm;
	}
	@ColumnAccess(columnName = "XHALARM0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addXHAlarm(Float alarmValue, int index){
		xHAlarm[index] = alarmValue;
	}
	
	public Float[] getXHWarn(){
		return xHWarn;
	}
	@ColumnAccess(columnName = "XHWARN0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addXHWarn(Float alarmValue, int index){
		xHWarn[index] = alarmValue;
	}
	
	public String[] getZLAlarm(){
		return zLAlarm;
	}
	@ColumnAccess(columnName = "ZLALARM0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addZLAlarm(String alarmStr, int index){
		zLAlarm[index] = alarmStr;
	}
	
	public String[] getZLWarn(){
		return zLWarn;
	}
	@ColumnAccess(columnName = "ZLWARN0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addZLWarn(String alarmStr, int index){
		zLWarn[index] = alarmStr;
	}
	
	public String[] getZHAlarm(){
		return zHAlarm;
	}
	@ColumnAccess(columnName = "ZHALARM0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addZHAlarm(String alarmStr, int index){
		zHAlarm[index] = alarmStr;
	}
	
	public String[] getZHWarn(){
		return zHWarn;
	}
	@ColumnAccess(columnName = "ZHWARN0", count = COUNT_ALARM_MODES, accessByIndex = true)
	public void addZHWarn(String alarmStr, int index){
		zHWarn[index] = alarmStr;
	}
	
	public String getAlarms(String type){
		String dateTimeStr = "\t0x8300000000000001\t"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
		StringBuilder str = new StringBuilder("");
		str.append(getXAlarms(getXHAlarm(), ".alarm.hAlarm.x_hAlarm.mode", dateTimeStr, type));
		str.append(getXAlarms(getXHWarn(), ".alarm.hWarn.x_hWarn.mode", dateTimeStr, type));
		str.append(getXAlarms(getXLAlarm(), ".alarm.lAlarm.x_lAlarm.mode", dateTimeStr, type));
		str.append(getXAlarms(getXLWarn(), ".alarm.lWarn.x_lWarn.mode", dateTimeStr, type));
		str.append(getZAlarms(getZHAlarm(), ".alarm.hAlarm.z_hAlarm.mode", dateTimeStr, type));
		str.append(getZAlarms(getZHWarn(), ".alarm.hWarn.z_hWarn.mode", dateTimeStr, type));
		str.append(getZAlarms(getZLAlarm(), ".alarm.lAlarm.z_lAlarm.mode", dateTimeStr, type));
		str.append(getZAlarms(getZLWarn(), ".alarm.lWarn.z_lWarn.mode", dateTimeStr, type));
		return str.toString();
	}
	
	private StringBuilder getXAlarms(Float[] alarms, String alarmStruct, String dateTimeStr, String type) {
		StringBuilder str = new StringBuilder("");
		for (int i = 0; i < alarms.length; i++) {
			Float alarmValue = alarms[i];
			if (alarmValue != null) {
				str.append("ASC (1)/0\t");
				str.append(getIdent());
				str.append(alarmStruct);
				str.append(i + 1);
				str.append("\t" + type + "\t");
				str.append(alarmValue);
				str.append(dateTimeStr);
			}
		}
		return str;
	}

	private StringBuilder getZAlarms(String[] alarms, String alarmStruct, String dateTimeStr, String type) {
		StringBuilder str = new StringBuilder("");
		for (int i = 0; i < alarms.length; i++) {
			String alarmStr = alarms[i];
			if (alarmStr != null) {
				str.append("ASC (1)/0\t");
				str.append(getIdent());
				str.append(alarmStruct);
				str.append(i + 1);
				str.append("\t" + type + "\t");
				str.append(alarmStr);
				str.append(dateTimeStr);
			}
		}
		return str;
	}
}
