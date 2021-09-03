package com.we.cisgenerator.model;

import java.util.Set;
import java.util.TreeSet;

public class CCDCom {

	public final static int LCDM_MAX_COUNT = 6;
	
	private int countLCDM;
	private int comNumber;
	private final Set<LCDM> setLCDM;
	
	public CCDCom(int comNumber){
		this.comNumber = comNumber;
		this.countLCDM = 0;
		this.setLCDM = new TreeSet<LCDM>();
	}
	
	public int getCountLCDM() {
		return countLCDM;
	}
	public Set<LCDM> getListLCDM() {
		return setLCDM;
	}
	public LCDM getLCDMbyAddr(int address){
		for (LCDM lcdm : setLCDM){
			if (lcdm.getAddress() == (byte)address){
				return lcdm;
			}
		}
		return null;
	}
	public LCDM getLCDM(int numberLCDM){
		int i = 1;
		for (LCDM lcdm : setLCDM){
			if (i == numberLCDM){
				return lcdm;
			} else{
				i++;
			}
		}
		return null;
	}
	public LCDM addnewLCDM(int numberLCDM){
		LCDM lcdm = new LCDM(numberLCDM);
		setLCDM.add(lcdm);
		countLCDM++;
		return lcdm;
	}
	public int getComNumber() {
		return comNumber;
	}
	public void setComNumber(int comNumber) {
		this.comNumber = comNumber;
	}
	
	
}
