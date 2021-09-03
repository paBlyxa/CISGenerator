package com.we.cisgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class LCDM implements Comparable<LCDM> {

	private byte address;
	private short channelMask;
	private final List<DMC> listDMC;
	
	public LCDM(int address){
		this.address = (byte)address;
		this.listDMC = new ArrayList<DMC>(16);
		for (int i = 0; i < 16; i++){
			this.listDMC.add(new DMC());
		}
	}
	
	public byte getAddress() {
		return address;
	}
	public void setAddress(byte address) {
		this.address = address;
	}
	public short getChannelMask() {
		return channelMask;
	}
	public void setChannelMask(short channelMask) {
		this.channelMask = channelMask;
	}
	public List<DMC> getListDMC() {
		return listDMC;
	}
	public DMC getDMC(int index){
		return listDMC.get(index-1);
	}
	@Override
	public String toString(){
		return "LCDM: address = " + address + ", channelMask = " + Integer.toHexString(channelMask & 0xFFFF);
	}

	@Override
	public int compareTo(LCDM o) {
		return address - o.address;
	}
	
}
