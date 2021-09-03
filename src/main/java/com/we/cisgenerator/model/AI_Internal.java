package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "AI внутренние")
public class AI_Internal {

	private final static int ADDR_FIRST_AI = 1000;
	private final static int COUPLER_ADDR_FIRST_AI = 1200;
	
	private String name;
	private String ident;
	private PLC plc;
	private int moduleNum;
	private int channelNum;
	private float sensorMax;
	private float sensorMin;
	private String format;
	private String units;
	private Period period;
	private Float polinomA = 0.0f;
	private Float polinomB = 0.0f;
	private Float polinomC = 0.0f;
	
	protected Float xLAlarm;
	protected Float xLWarn;
	protected Float xHAlarm;
	protected Float xHWarn;
	
	public AI_Internal(){
		this.units = "";
	}

	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "Наименование")
	public void setName(String name) {
		this.name = name;
	}

	public String getIdent() {
		return ident;
	}
	@ColumnAccess(columnName = "Идент")
	public void setIdent(String ident) {
		this.ident = ident;
	}

	public PLC getPlc() {
		return plc;
	}
	@ColumnManyToOne(columnName = "PLC", foreignClass = PLC.class)
	public void setPlc(PLC plc) {
		this.plc = plc;
	}

	public int getModuleNum() {
		return moduleNum;
	}
	@ColumnAccess(columnName = "№ модуля")
	public void setModuleNum(int moduleNum) {
		this.moduleNum = moduleNum;
	}

	public int getChannelNum() {
		return channelNum;
	}
	@ColumnAccess(columnName = "№ канала")
	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public int getAddress() {
		if (plc == null){
			return 0;
		}
		if (!plc.isIecAddressCalculated()){
			plc.calcIecAddress();
		}
		if (plc.isInterfaceModule() && (plc.getReservPLC() == null)){
			// Расчет адреса для интерфейсного модуля
			return plc.getModules().get(moduleNum - 1).getIecIn() / 2  + (channelNum - 1);
		} else {
			// Расчет адреса для контроллера
			return ((plc.getModules().get(moduleNum - 1).getIecIn() / 2) + (channelNum - 1)
					+ (getPlc().isInterfaceModule() ? COUPLER_ADDR_FIRST_AI : ADDR_FIRST_AI));
		}
	}
	//@ColumnAccess(columnName = "Адрес регистра")
	//public void setAddress(int address) {
	//	this.address = address;
	//}
	
	public Float getXLAlarm(){
		return xLAlarm;
	}
	@ColumnAccess(columnName = "XLALARM01")
	public void addXLAlarm(Float alarmValue){
		xLAlarm = alarmValue;
	}
	
	public Float getXLWarn(){
		return xLWarn;
	}
	@ColumnAccess(columnName = "XLWARN01")
	public void addXLWarn(Float alarmValue){
		xLWarn = alarmValue;
	}
	
	public Float getXHAlarm(){
		return xHAlarm;
	}
	@ColumnAccess(columnName = "XHALARM01")
	public void addXHAlarm(Float alarmValue){
		xHAlarm = alarmValue;
	}
	
	public Float getXHWarn(){
		return xHWarn;
	}
	@ColumnAccess(columnName = "XHWARN01")
	public void addXHWarn(Float alarmValue){
		xHWarn = alarmValue;
	}
	
	public Float getWarnLow() {
		if (xLWarn == null){
			return (float) (getEmergLow() + (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xLWarn;
	}

	public Float getWarnHigh() {
		if (xHWarn == null){
			return (float) (getEmergHigh() - (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xHWarn;
	}

	public Float getEmergLow() {
		if (xLAlarm == null){
			return (float) (getSensorMin()  + (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xLAlarm;
	}

	public Float getEmergHigh() {
		if (xHAlarm == null){
			return (float) (getSensorMax()  - (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xHAlarm;
	}
	
	public float getSensorMax() {
		return sensorMax;
	}
	@ColumnAccess(columnName = "Максимальное значение")
	public void setSensorMax(float sensorMax) {
		this.sensorMax = sensorMax;
	}

	public float getSensorMin() {
		return sensorMin;
	}
	@ColumnAccess(columnName = "Минимальное значение")
	public void setSensorMin(float sensorMin) {
		this.sensorMin = sensorMin;
	}

	/*public float getSensorMax(){
		return (float) 850.0;
	}
	public float getSensorMin(){
		return (float) -200.0;
	}*/

	public String getFormat() {
		return format;
	}
	@ColumnAccess(columnName = "Формат")
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getUnits() {
		return units;
	}
	@ColumnAccess(columnName = "ЕД ИЗМ")
	public void setUnits(String units) {
		if (units != null){
			this.units = units;
		} else {
			this.units = "";
		}
	}
	
	public Period getPeriod() {
		return period;
	}
	@ColumnManyToOne(columnName = "Период опроса", foreignClass = Period.class)
	public void setPeriod(Period period) {
		this.period = period;
	}
	
	public Float getPolinomA() {
		return polinomA;
	}
	@ColumnAccess(columnName = "Коэффициент A")
	public void setPolinomA(Float polinomA) {
		if (polinomA != null)
			this.polinomA = polinomA;
	}

	public Float getPolinomB() {
		return polinomB;
	}
	@ColumnAccess(columnName = "Коэффициент B")
	public void setPolinomB(Float polinomB) {
		if (polinomB != null)
			this.polinomB = polinomB;
	}

	public Float getPolinomC() {
		return polinomC;
	}
	@ColumnAccess(columnName = "Коэффициент C")
	public void setPolinomC(Float polinomC) {
		if (polinomC != null)
			this.polinomC = polinomC;
	}
	
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("AI внутр.: ");
		str.append(ident);
		str.append(" - ");
		str.append(name);
		str.append(", PLC - ").append(plc != null ? plc.getPosition() : null);
		str.append(", ModuleNum - ").append(moduleNum);
		str.append(", ChannelNum - ").append(channelNum);
		return str.toString();
	}

	
}
