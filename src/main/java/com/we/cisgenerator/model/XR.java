package com.we.cisgenerator.model;

import com.we.cisgenerator.model.internal.XZAlarms;
import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.TableAccess;

@TableAccess(name = "Цифровые сигналы")
public class XR extends XZAlarms {

	private String name;
	private PLC plc;
	private float sensorMax;
	private float sensorMin;
	private int address;
	private int externalAddr;
	private Integer externalAddrBit;
	private Integer externalAddrStatus;
	private Integer externalAddrBitStatus;
	private String ident;
	private String units;
	private ExternalSystem system;
	private PLC_TYPE type;
	private PLC_TYPE statusType = PLC_TYPE.INT;
	private String format;
	private Period period;
	private Float factor;
	private boolean out;
	private BYTE_ORDER byteOrder;
	private String tag;
	private String alarmClass;

	public XR(String ident) {
		this.ident = ident;
		this.units = "";
	}

	public XR() {
		byteOrder = BYTE_ORDER.B1B2B3B4;
	}

	public String getName() {
		return name;
	}

	@ColumnAccess(columnName = "НАИМЕНОВАНИЕ")
	public void setName(String name) {
		this.name = name;
	}

	public PLC getPlc() {
		return plc;
	}

	@ColumnManyToOne(columnName = "PLC", foreignClass = PLC.class)
	public void setPlc(PLC plc) {
		this.plc = plc;
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

	public int getAddress() {
		return address;
	}

	@ColumnAccess(columnName = "Адрес регистра")
	public void setAddress(int address) {
		this.address = address;
	}

	public int getExternalAddr() {
		return externalAddr;
	}

	public Integer getExternalAddrBit() {
		return externalAddrBit;
	}

	@ColumnAccess(columnName = "Внешний адрес")
	public void setExternalAddr(String externalAddr) {
		if (externalAddr.contains(".")) {
			String[] str = externalAddr.split("\\.");
			this.externalAddr = Integer.parseInt(str[0]);
			this.externalAddrBit = Integer.parseInt(str[1]);
			statusType = PLC_TYPE.BOOL;
			type = PLC_TYPE.BOOL;
		} else {
			this.externalAddr = Integer.parseInt(externalAddr);
			this.externalAddrBit = null;
		}
	}

	public Integer getExternalAddrStatus() {
		return externalAddrStatus;
	}

	public Integer getExternalAddrBitStatus() {
		return externalAddrBitStatus;
	}

	@ColumnAccess(columnName = "Адрес достоверности")
	public void setExternalAddrStatus(String externalAddrStat) {
		if (externalAddrStat != null) {
			if (externalAddrStat.contains(".")) {
				String[] str = externalAddrStat.split("\\.");
				this.externalAddrStatus = Integer.parseInt(str[0]);
				this.externalAddrBitStatus = Integer.parseInt(str[1]);
				statusType = PLC_TYPE.BOOL;
			} else {
				this.externalAddrStatus = Integer.parseInt(externalAddrStat);
				this.externalAddrBitStatus = null;
			}
		} else {
			externalAddrStatus = null;
		}
	}

	public String getUnits() {
		return units;
	}

	@ColumnAccess(columnName = "ЕД ИЗМ")
	public void setUnits(String units) {
		this.units = units;
	}

	public ExternalSystem getSystem() {
		return system;
	}

	@ColumnManyToOne(columnName = "МЕСТО ОТБОРА", foreignClass = ExternalSystem.class)
	public void setSystem(ExternalSystem system) {
		this.system = system;
	}

	public PLC_TYPE getType() {
		return type;
	}

	@ColumnAccess(columnName = "Тип данных")
	public void setType(String type) {
		if (type.contains(":")) {
			String[] strs = type.split(":");
			this.type = PLC_TYPE.valueOf(strs[0]);
			this.byteOrder = BYTE_ORDER.valueOf(strs[1]);
		} else {
			this.type = PLC_TYPE.valueOf(type);
		}
	}

	public PLC_TYPE getStatusType() {
		return statusType;
	}

	public Period getPeriod() {
		return period;
	}

	@ColumnManyToOne(columnName = "Период", foreignClass = Period.class)
	public void setPeriod(Period period) {
		this.period = period;
	}

	public String getIdent() {
		return ident;
	}

	@ColumnAccess(columnName = "Идент")
	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getFormat() {
		return format;
	}

	@ColumnAccess(columnName = "Формат")
	public void setFormat(String format) {
		this.format = format;
	}

	public Float getFactor() {
		return factor;
	}

	@ColumnAccess(columnName = "Коэффициент")
	public void setFactor(Float factor) {
		this.factor = factor;
	}

	public boolean isOut() {
		return out;
	}

	@ColumnAccess(columnName = "Выход")
	public void setOut(boolean out) {
		this.out = out;
	}

	public String getTag() {
		return tag;
	}

	@ColumnAccess(columnName = "ZINPNT1")
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAlarmClass() {
		return alarmClass;
	}

	@ColumnAccess(columnName = "AlarmClass")
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}

	public BYTE_ORDER getByteOrder() {
		return byteOrder;
	}

	public Float getWarnLow() {
		if (xLWarn[0] == null) {
			return (float) (getEmergLow() + (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xLWarn[0];
	}

	public Float getWarnHigh() {
		if (xHWarn[0] == null) {
			return (float) (getEmergHigh() - (getSensorMax() - getSensorMin()) * 0.01);
		}
		return xHWarn[0];
	}

	public Float getEmergLow() {
		if (xLAlarm[0] == null) {
			return (float) (getSensorMin() + (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xLAlarm[0];
	}

	public Float getEmergHigh() {
		if (xHAlarm[0] == null) {
			return (float) (getSensorMax() - (getSensorMax() - getSensorMin()) * 0.0001);
		}
		return xHAlarm[0];
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("DAI: ");
		str.append(getIdent());
		str.append(" - ");
		str.append(getName());
		str.append(", PLC: ").append(getPlc() != null ? getPlc().getPosition() : null);
		str.append(", Type: ").append(getType());
		str.append(", Address: ").append(getAddress());
		str.append(", ExtAddress: ").append(getExternalAddr());
		str.append(", InfoSystem: ").append(getSystem());
		return str.toString();
	}

	public enum BYTE_ORDER {
		B1B2B3B4("MBCFG_BYTE_ORDER_0"), B2B1B4B3("MBCFG_BYTE_ORDER_1"), B4B3B2B1("MBCFG_BYTE_ORDER_2"), B3B4B1B2(
				"MBCFG_BYTE_ORDER_3");

		private BYTE_ORDER(String enumName) {
			this.enumName = enumName;
		}

		private String enumName;

		public String getEnumName() {
			return enumName;
		}
	}
}
