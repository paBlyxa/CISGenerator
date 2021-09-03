package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Дискретные выходные")
public class DQ {

	private final static int ADDR_FIRST_DQ = 0 - 1;
	private final static int COUPLER_ADDR_FIRST_DQ = 1600 - 1;
	
	private String name;
	private Cabinet cabinet;
	private PLC plc;
	private String ident;
	private Integer moduleNum;
	private Integer channelNum;
	private Period period;
	private String infoSystem;
	private String inputPoint;
	private String coil;
	private String alarmClass;
	private String tag;
	
	public DQ(String ident){
		this.ident = ident;
	}
	
	public DQ(){
	}
		
	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "Наименование параметра")
	public void setName(String name) {
		this.name = name;
	}

	public Cabinet getCabinet() {
		return cabinet;
	}
	@ColumnManyToOne(columnName = "Шкаф подключения ИВС", foreignClass = Cabinet.class)
	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}

	public PLC getPlc() {
		return plc;
	}
	@ColumnManyToOne(columnName = "PLC", foreignClass = PLC.class)
	public void setPlc(PLC plc) {
		this.plc = plc;
	}

	public String getIdent() {
		return ident;
	}
	@ColumnAccess(columnName = "Идентификатор")
	public void setIdent(String ident){
		this.ident = ident;
	}
	
	public Integer getModuleNum() {
		return moduleNum;
	}
	@ColumnAccess(columnName = "№ модуля")
	public void setModuleNum(Integer moduleNum) {
		this.moduleNum = moduleNum;
	}

	public Integer getChannelNum() {
		return channelNum;
	}
	@ColumnAccess(columnName = "№ канала")
	public void setChannelNum(Integer channelNum) {
		this.channelNum = channelNum;
	}
	
	public Period getPeriod(){
		return period;
	}
	@ColumnManyToOne(columnName = "Период опроса", foreignClass = Period.class)
	public void setPeriod(Period period){
		this.period = period;
	}
	
	public String getInfoSystem() {
		return infoSystem;
	}
	@ColumnAccess(columnName = "Куда идет")
	public void setInfoSystem(String infoSystem) {
		this.infoSystem = infoSystem;
	}
	
	public String getInputPoint() {
		return inputPoint;
	}
	@ColumnAccess(columnName = "Точка выхода")
	public void setInputPoint(String inputPoint) {
		this.inputPoint = inputPoint;
	}

	public String getAlarmClass(){
		return alarmClass;
	}
	@ColumnAccess(columnName = "AlarmClass")
	public void setAlarmClass(String alarmClass){
		this.alarmClass = alarmClass;
	}

	public String getCoil(){
		return coil;
	}
	@ColumnAccess(columnName = "Поз реле")
	public void setCoil(String coil){
		this.coil = coil;
	}
	
	public String getTag(){
		return tag;
	}
	@ColumnAccess(columnName = "Расчетный параметр BO")
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public int getAddress(){
		if (plc == null){
			return 0;
		}
		if (!plc.isIecAddressCalculated()){
			plc.calcIecAddress();
		}
		return ((plc.getModules().get(moduleNum - 1).getIecOut() / 2) * 16 + channelNum
		+ (getPlc().isInterfaceModule() ? COUPLER_ADDR_FIRST_DQ : ADDR_FIRST_DQ));
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("DQ: ");
		str.append(getIdent());
		str.append(" - ");
		str.append(name);
		str.append(", Шкаф: ").append(getCabinet() != null ? getCabinet().getName() : null);
		str.append(", PLC: ").append(getPlc() != null ? getPlc().getPosition() : null);
		str.append(", Module: ").append(getModuleNum());
		str.append(", Channel: ").append(getChannelNum());
		return str.toString();
	}
	
}
