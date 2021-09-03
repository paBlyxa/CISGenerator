package com.we.cisgenerator.model.internal;

import com.we.cisgenerator.model.PLC_TYPE;
import com.we.cisgenerator.model.XR;
import com.we.modbus.model.Function;

import java.util.ArrayList;
import java.util.List;

public class ModbusTag implements Comparable<ModbusTag> {

	private String name;
	private PLC_TYPE type;
	private int address;
	private boolean value;
	private boolean out;
	private XR dai;
	/**
	 * Tags with same address
	 */
	private List<ModbusTag> tags;

	/**
	 * 
	 * @param dai
	 * @param name
	 * @param type
	 * @param address
	 * @param value
	 * @param out
	 */
	public ModbusTag(XR dai, String name, PLC_TYPE type, int address, boolean value, boolean out) {
		this.name = name;
		this.type = type;
		this.setAddress(address);
		this.value = value;
		this.dai = dai;
		this.out = out;
		tags = new ArrayList<ModbusTag>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PLC_TYPE getType() {
		return type;
	}

	public void setType(PLC_TYPE type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = PLC_TYPE.valueOf(type);
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public boolean isValue() {
		return value;
	}

	public boolean isBool() {
		return type == PLC_TYPE.BOOL;
	}

	public boolean isOut() {
		return out;
	}

	public XR getDAI() {
		return dai;
	}

	public List<ModbusTag> getTags() {
		return tags;
	}

	public void addTag(ModbusTag tag) {
		tags.add(tag);
	}

	public Function getFunction() {
		if (isBool()) {
			if (isOut()) {
				return Function.WRITE_MULTIPLE_COILS;
			} else {
				return Function.READ_COIL_STATUS;
			}
		} else {
			if (isOut()) {
				return Function.WRITE_MULTIPLE_REGISTERS;
			} else {
				return Function.READ_MULTIPLE_REGISTERS;
			}
		}
	}

	@Override
	public int compareTo(ModbusTag o) {
		if (this.isOut() == o.isOut()) {
			if (this.isBool() == o.isBool()) {
				return this.address - o.getAddress();
			} else {
				return this.isBool() ? -1 : 1;
			}
		} else {
			return this.isOut() ? 1 : -1;
		}
	}

	private String toExportString(String name) {
		StringBuilder result = new StringBuilder();
		switch (dai.getType()) {
		case BOOL:
			if (isOut()) {
				int adr1 = (getDAI().getAddress() + 16) / 16;
				int adrBit = (getDAI().getAddress() - 0) % 16;
				result.append(name);
				if (dai.getExternalAddrBit() != null) {
					result.append(".").append(dai.getExternalAddrBit());
				}
				result.append(" := ");
				if (isValue()) {
					result.append("boolValueOut[" + adr1 + "]." + adrBit);
				} else {
					result.append("0");
				}
			} else {
				if (isValue()) {
					int adr1 = (getDAI().getAddress() - 6144 + 16) / 16;
					int adrBit = (getDAI().getAddress() - 6144) % 16;
					result.append("boolValueIn[" + adr1 + "]." + adrBit);
					result.append(" := ");
					result.append(name);
					if (dai.getExternalAddrBit() != null) {
						result.append(".").append(dai.getExternalAddrBit());
					}
				} else {
					return "";
				}
			}
			result.append(";\r\n");
			break;
		case DWORD:
		case INT:
		case REAL:
		case UINT:
		case WORD:
			if (dai.getType() != PLC_TYPE.BOOL) {
				int adr2 = (dai.getAddress() - 12284) / 4;
				StringBuilder value3 = new StringBuilder("realValue[");
				value3.append(adr2);
				value3.append("].");
				value3.append(isValue() ? "value" : "stat");
				if (isOut()) {
					result.append(name);
					result.append(" := ");
					result.append(value3);
				} else {
					result.append(value3);
					result.append(" := ");
					result.append(name);
				}
				if (isValue() && getDAI().getFactor() != null) {
					result.append(" * ");
					result.append(getDAI().getFactor());
				}

				result.append(";\r\n");
			}
			break;
		default:
			break;
		}
		return result.toString();
	}

	public String toExportString() {
		StringBuilder result = new StringBuilder();
		StringBuilder name = new StringBuilder("MBCFG_");
		name.append(dai.getSystem().getName());
		name.append(".");
		name.append(getName());
		String nameStr = name.toString();
		result.append(toExportString(nameStr));
		if (!tags.isEmpty()) {
			for (ModbusTag tag : tags) {
				result.append(tag.toExportString(nameStr));

			}
		}
		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("ModbusTag: ");
		str.append("name - ").append(name).append(", type - ").append(type).append(", address - ").append(address)
				.append(", isValue - ").append(value).append(", isOut - ").append(out);
		return str.toString();
	}

}
