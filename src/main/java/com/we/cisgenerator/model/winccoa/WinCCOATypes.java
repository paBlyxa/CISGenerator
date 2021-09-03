package com.we.cisgenerator.model.winccoa;

public enum WinCCOATypes {

	STRUCT(1),
	DYN_UINT(4),
	DYN_STRING(9),
	CHAR(19),
	UINT(20),
	INT(21),
	FLOAT(22),
	BOOL(23),
	BIT32(24),
	STRING(25),
	BLOB(46),
	DYN_BLOB(48);
	
	private WinCCOATypes(int index){
		this.index = index;
	}
	
	private final int index;
	
	/**
	 * Return index of type
	 * @return
	 */
	public int get(){
		return index;
	}
}
