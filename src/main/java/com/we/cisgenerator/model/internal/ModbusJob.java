package com.we.cisgenerator.model.internal;

import com.we.modbus.model.DataModelTable;
import com.we.modbus.model.Function;

public class ModbusJob {

	private final int address;
	private int quantity;
	private final boolean write;
	private final Function function;
	
	public ModbusJob(int address, int quantity, Function function){
		this.address = address;
		this.quantity = quantity;
		this.function = function;
		switch(function){
		case READ_COIL_STATUS:
		case READ_DISCRETE_INPUTS:
		case READ_INPUT_REGISTERS:
		case READ_MULTIPLE_REGISTERS:
			write = false;
			break;
		case MASK_WRITE_REGISTER:
		case WRITE_MULTIPLE_COILS:
		case WRITE_MULTIPLE_REGISTERS:
		case WRITE_SINGLE_COIL:
		case WRITE_SINGLE_REGISTER:
			write = true;
			break;
		default:
			write = false;
			break;
		}
	}
	
	public boolean isBool(){
		return (function.getDataModelTable() == DataModelTable.Coils) ||
				(function.getDataModelTable() == DataModelTable.DiscretesInput);
	}
	
	public int getAddress(){
		return address;
	}
	
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	public boolean isWrite(){
		return write;
	}
	
	public Function getFunction(){
		return function;
	}
	
	public String toExportString(){
		StringBuilder str = new StringBuilder("\t(\tFunctioncode\t\t\t:= ");
		str.append(write ? 16 : 3);
		str.append(",\r\n\t\tReadStartAddress\t\t:= ");
		str.append(write ? 0 : address);
		str.append(",\r\n\t\tReadQuantity\t\t\t:= ");
		str.append(write ? 0 : quantity);
		str.append(",\r\n\t\tWriteStartAddress\t\t:= ");
		str.append(write ? address : 0);
		str.append(",\r\n\t\tWriteQuantity\t\t\t:= ");
		str.append(write ? quantity : 0);
		str.append(",\r\n\t\tptReadData\t\t:= 0,\r\n\t\tptWriteData\t\t:= 0 ),\r\n");
		return str.toString();
	}
	
	public String toString(){
		return "ModbusJob: address - " + address + ", quantity - " + quantity + ", function - " + function;
	}
}
