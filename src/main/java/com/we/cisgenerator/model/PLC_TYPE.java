package com.we.cisgenerator.model;

public enum PLC_TYPE {

	BOOL(1),
	UINT(1),
	INT(1),
	WORD(1),
	DWORD(2),
	REAL(2);
	
	private int size;
	
	private PLC_TYPE(int size){
		this.size = size;
	}
	
	public int getSize(){
		return size;
	}
}
