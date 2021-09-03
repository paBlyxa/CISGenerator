package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnManyToOne;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TableAccess(name = "Контроллеры")
public class PLC {
	
	private Integer id;
	private String position;
	private boolean reserved;
	private boolean interfaceModule;
	private Cabinet cabinet;
	private String ipAddress;
	private final List<Module> modules;
	private final Map<Integer, CCDCom> comParameters;
	private final List<PLC> couplers;
	private List<XR> listDAI;
	private final List<AI_Internal> listAIInternal;
	private int driverModbus;
	private int driverIEC;
	private boolean iecAddressCalculated;
	private int iecInSize;
	private int iecOutSize;
	private int iecInFirstDIOffset = 0;
	private PLC reservPLC;
		
	public PLC(Integer id) {
		this();
		this.id = id;
	}
	
	public PLC(String position){
		this();
		this.position = position;
	}

	public PLC(){
		this.modules = new ArrayList<Module>();
		this.comParameters = new HashMap<Integer, CCDCom>();
		this.iecAddressCalculated = false;
		this.couplers = new ArrayList<PLC>();
		this.listAIInternal = new ArrayList<AI_Internal>();
	}
	
	public Integer getId() {
		return id;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Код")
	public void setId(int id){
		this.id = id;
	}
	
	public String getPosition() {
		return position;
	}
	@ColumnAccess(columnName = "Позиция PLC")
	public void setPosition(String position) {
		this.position = position;
	}
	
	public boolean isReserved() {
		return reserved;
	}
	@ColumnAccess(columnName = "Резервирование")
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	public boolean isIecAddressCalculated(){
		return iecAddressCalculated;
	}
	
	public Cabinet getCabinet() {
		return cabinet;
	}
	@ColumnManyToOne(columnName = "Шкаф", foreignClass = Cabinet.class)
	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}
	public void setCabinet(String str){
		String[] elements = str.split(";");
		this.cabinet = new Cabinet();
		cabinet.setId(Integer.parseInt(elements[0]));
		cabinet.setName(elements[1]);
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	@ColumnAccess(columnName = "Ip адрес")
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@ColumnManyToOne(columnName = "ModuleSlot", foreignClass = Module.class,
			count = 40)
	public void addModule(Module module){
		modules.add(module.clone());
		iecAddressCalculated = false;
	}
	public List<Module> getModules(){
		return modules;
	}
	public void setModule(String str){
		String[] strModules = str.split(", ");
		for (String module : strModules){
			if (module.length() > 1){
				modules.add(new Module(module));
			}
		}
	}
	
	public void addCoupler(PLC plc){
		couplers.add(plc);
	}
	public List<PLC> getCouplers(){
		return couplers;
	}
	
	public void addDAI(XR dai){
		if (listDAI == null){
			listDAI = new ArrayList<XR>();
		}
		listDAI.add(dai);
	}
	public List<XR> getListDAI(){
		return listDAI;
	}
	public void setListDAI(List<XR> listDAI){
		this.listDAI = listDAI;
	}
	
	public void addAIInernal(AI_Internal ai){
		listAIInternal.add(ai);
	}
	public List<AI_Internal> getListAIInernal(){
		return listAIInternal;
	}
	
	public void putCCDCom(Integer comNum, CCDCom comParameter){
		comParameters.put(comNum, comParameter);
	}
	public Map<Integer, CCDCom> getComParameters(){
		return comParameters;
	}
	
	public int getDriverModbus() {
		if (isInterfaceModule() && (reservPLC != null)){
			return reservPLC.getDriverModbus();
		}
		return driverModbus;
	}
	@ColumnAccess(columnName = "DriverModbus")
	public void setDriverModbus(int driverModbus) {
		this.driverModbus = driverModbus;
	}
	public void setDriverModbus(String str){
		this.driverModbus = Integer.parseInt(str);
	}
	public int getDriverIEC() {
		if (isInterfaceModule()){
			return reservPLC.getDriverIEC();
		}
		return driverIEC;
	}
	@ColumnAccess(columnName = "DriverIEC104")
	public void setDriverIEC(int driverIEC) {
		this.driverIEC = driverIEC;
	}
	public void setDriverIEC(String str){
		this.driverIEC = Integer.parseInt(str);
	}


	public boolean isInterfaceModule() {
		return interfaceModule;
	}
	@ColumnAccess(columnName = "Интерфейсный модуль")
	public void setInterfaceModule(boolean interfaceModule) {
		this.interfaceModule = interfaceModule;
	}

	public PLC getReservPLC() {
		return reservPLC;
	}
	@ColumnManyToOne(columnName = "ReservedPLC", foreignClass = PLC.class)
	public void setReservPLC(PLC reservPLC) {
		this.reservPLC = reservPLC;
	}
		
	public int getDIOffset(){
		if (isInterfaceModule()){
			return 0;
		}
		return iecInFirstDIOffset;
	}
	
	public void calcIecAddress() {
		iecInSize = 0;
		iecOutSize = 0;
		iecInFirstDIOffset = 0;
		// Первый проход ищем аналоговые или спец модули
		for (Module m : getModules()) {
			switch (m.getType()) {
			case DI: // Type_1 - DI
				break;
			case DO: // Type_10 - DO
				break;
			case AI: // Type_8 - AI
			case AI_PT100: // Type_44 - 750_461 AI PT100
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(m.getChannels() * 2);
				m.setIecOutSize(0);
				iecInSize += m.getIecInSize();
				break;
			case AO: // Type_14 - AO
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(0);
				m.setIecOutSize(m.getChannels() * 2);
				iecOutSize += m.getIecOutSize();
				break;
			case _750_652_24: // Type_30 - 750_652 24 bytes
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(24);
				m.setIecOutSize(24);
				iecInSize += 24;
				iecOutSize += 24;
				iecInFirstDIOffset += 24;
				break;
			case _750_652_48: // Type_33 - 750_652 48 bytes
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(48);
				m.setIecOutSize(48);
				iecInSize += 48;
				iecOutSize += 48;
				iecInFirstDIOffset += 48;
				break;
			case _750_652_8: // Type_40 - 750_652 8 bytes
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(8);
				m.setIecOutSize(8);
				iecInSize += 8;
				iecOutSize += 8;
				iecInFirstDIOffset += 8;
				break;
			default:
				;
			}
		}
		//iecInFirstDIOffset = iecInSize;
		// Второй проход для дискретов
		for (Module m : getModules()) {
			switch (m.getType()) {
			case DI: // Type_1 - DI
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize((m.getChannels() + 7) / 8);
				m.setIecOutSize(0);
				iecInSize += m.getIecInSize();
				break;
			case DO: // Type_10 - DO
				m.setIecIn(iecInSize);
				m.setIecOut(iecOutSize);
				m.setIecInSize(0);
				m.setIecOutSize((m.getChannels() + 7) / 8);
				iecOutSize += m.getIecOutSize();
				break;
			default:
				break;
			}

		}
		iecAddressCalculated = true;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof PLC){
			PLC plc = (PLC) object;
			if (plc.getId() == this.id){
				return true;
			}
			if (plc.getPosition() == this.position){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("PLC: ");
		str.append(position);
		str.append(", Шкаф: ").append(getCabinet());
		str.append(", ipAddress: ").append(ipAddress);
		if (!modules.isEmpty()){
			str.append(", modules:");
			for (Module module : modules){
				str.append(" ");
				str.append(module.getId());
			}
		}
		str.append(", reserved: ").append(reserved);
		str.append(", reservPLC: ").append(reservPLC != null ? reservPLC.getPosition() : "");
		str.append(", interaceModule: ").append(interfaceModule);
		if (!couplers.isEmpty()){
			str.append(", interfacemodules:");
			for (PLC coupler : couplers){
				str.append(" ");
				str.append(coupler.getPosition());
			}
		}
		return str.toString();
	}
	
	public int getIecInSize(){
		return iecInSize;
	}
	
	public int getIecOutSize(){
		return iecOutSize;
	}
}
