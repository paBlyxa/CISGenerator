package com.we.cisgenerator.model;

import com.we.cisgenerator.model.internal.AnalogSignal;
import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.TableAccess;

/**
 * Класс соответствующий таблице 'Аналоговые сигналы'
 * @author fakadey
 *
 */
@TableAccess(name = "Аналоговые сигналы")
public class AI extends AnalogSignal{
	
	private String name;
	private Cabinet cabinet;
	private PLC plc;
	private Byte comNum;
	private Byte lcdmNum;
	private Byte channelNum;
	private AI_Type type;
	private float sensorMax;
	private float sensorMin;
	private Integer address;
	private String ident;
	private String infoSystem;
	private Integer adrCompensation;
	private Period period;
	private String inputPoint;
	private boolean fast;
	
	public AI(){
	}
	
	public AI(String ident){
		this();
		this.ident = ident;
	}
	
	/**
	 * Возвращает идентификатор аналогового сигнала
	 * @return String ident
	 */
	public String getIdent() {
		return ident;
	}
	@ColumnAccess(columnName = "Идент")
	public void setIdent(String ident) {
		this.ident = ident;
	}
	
	/**
	 * Возвращает наименование аналогового сигнала
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	@ColumnAccess(columnName = "НАИМЕНОВАНИЕ")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Возвращает шкаф ввода аналогового сигнала
	 * @return Cabinet cabinet
	 */
	public Cabinet getCabinet() {
		return cabinet;
	}
	@ColumnManyToOne(columnName = "Шкаф подключения ИВС", foreignClass = Cabinet.class)
	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}

	/**
	 * Возвращает контроллер ввода аналогового сигнала
	 * @return PLC plc
	 */
	public PLC getPlc() {
		return plc;
	}
	@ColumnManyToOne(columnName = "PLC", foreignClass= PLC.class)
	public void setPlc(PLC plc) {
		this.plc = plc;
	}

	/**
	 * Возвращает номер COM-порта ввода аналогового сигнала
	 * @return Byte comNum
	 */
	public Byte getComNum() {
		return comNum;
	}
	@ColumnAccess(columnName = "№ COM-порта")
	public void setComNum(Byte comNum) {
		this.comNum = comNum;
	}

	/**
	 * Возвращает номер LCDM ввода аналогового сигнала
	 * на соответствующем COM-порте.
	 * @return Byte lcdmNum
	 */
	public Byte getLcdmNum() {
		return lcdmNum;
	}
	@ColumnAccess(columnName = "№ LCDM")
	public void setLcdmNum(Byte lcdmNum) {
		this.lcdmNum = lcdmNum;
	}

	/**
	 * Возвращает номер канала ввода аналогового сигнала
	 * на соответствующем LCDM концентраторе.
	 * @return Byte channelNum
	 */
	public Byte getChannelNum() {
		return channelNum;
	}
	@ColumnAccess(columnName = "№ ch LCDM")
	public void setChannelNum(Byte channelNum) {
		this.channelNum = channelNum;
	}

	/**
	 * Возвращает тип аналогового сигнала
	 * @return AI_Type type
	 */
	public AI_Type getType() {
		return type;
	}
	@ColumnManyToOne(columnName = "Тип сигнала", foreignClass = AI_Type.class)
	public void AI_Type(AI_Type type) {
		this.type = type;
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

	public Integer getAddress(){
		return address;
	}
	@ColumnAccess(columnName = "Адрес регистра")
	public void setAddress(Integer address){
		this.address = address;
	}


	public String getInfoSystem() {
		return infoSystem;
	}
	@ColumnAccess(columnName = "МЕСТО ОТБОРА")
	public void setInfoSystem(String infoSystem) {
		this.infoSystem = infoSystem;
	}


	public Integer getAdrCompensation() {
		return (adrCompensation != null ? adrCompensation : 0);
	}
	@ColumnAccess(columnName = "Адрес компенсации")
	public void setAdrCompensation(Integer adrCompensation) {
		this.adrCompensation = adrCompensation;
	}


	public Period getPeriod() {
		return period;
	}
	@ColumnManyToOne(columnName = "Период опроса", foreignClass = Period.class)
	public void setPeriod(Period period) {
		this.period = period;
	}

	public String getInputPoint() {
		return inputPoint;
	}
	@ColumnAccess(columnName = "Точка ввода в ИВС")
	public void setInputPoint(String inputPoint) {
		this.inputPoint = inputPoint;
	}

	public boolean isFast() {
		return fast;
	}
	@ColumnAccess(columnName = "Быстрый")
	public void setFast(boolean fast) {
		this.fast = fast;
	}
	
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("AI: ");
		str.append(getIdent());
		str.append(" - ");
		str.append(getName());
		str.append(", Шкаф: ").append(getCabinet() != null ? getCabinet().getName() : null);
		str.append(", PLC: ").append(getPlc() != null ? getPlc().getPosition() : null);
		str.append(", COM: ").append(getComNum());
		str.append(", LCDM: ").append(getLcdmNum());
		str.append(", Channel: ").append(getChannelNum());
		str.append(", Address: ").append(getAddress());
		str.append(", Type: ").append(getType());
		str.append(", InfoSystem: ").append(getInfoSystem());
		return str.toString();
	}
	
	public Float getWarnLow() {
		if (xLWarn[0] == null){
			return (float) (getEmergLow() + (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xLWarn[0];
	}

	public Float getWarnHigh() {
		if (xHWarn[0] == null){
			return (float) (getEmergHigh() - (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xHWarn[0];
	}

	public Float getEmergLow() {
		if (xLAlarm[0] == null){
			return (float) (getSensorMin()  + (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xLAlarm[0];
	}

	public Float getEmergHigh() {
		if (xHAlarm[0] == null){
			return (float) (getSensorMax()  - (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xHAlarm[0];
	}
}
