package com.we.cisgenerator.model;

import com.we.jackcess.annotations.ColumnAccess;
import com.we.jackcess.annotations.ColumnPrimaryKey;
import com.we.jackcess.annotations.TableAccess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TableAccess(name = "Модули WAGO PLC")
public class Module {

	private final static Pattern PATTERN_TYPE = Pattern.compile("Type_(\\d+)_(\\d+)");
	
	private Integer id;
	private String name;
	private String sectionName;
	private int iecIn;
	private int iecOut;
	private TYPE type;
	private int channels;
	private int iecInSize;
	private int iecOutSize;
	
	public Module(String module){
		String[] str = module.substring(1, module.length() - 1).split(";");
		this.id = Integer.parseInt(str[0]);
		this.name = str[1];
		this.sectionName = str[2];
		getTypeAndChannels(this.sectionName);
	}
	
	public Module(){
	}
	
	public Module clone(){
		Module module = new Module();
		module.id = id;
		module.name = name;
		module.sectionName = sectionName;
		module.iecIn = iecIn;
		module.iecOut = iecOut;
		module.type = type;
		module.channels = channels;
		return module;
	}
	
	public Integer getId() {
		return id;
	}
	@ColumnPrimaryKey
	@ColumnAccess(columnName = "Код")
	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	@ColumnAccess(columnName = "Название")
	public void setName(String name) {
		this.name = name;
	}


	public String getSectionName() {
		return sectionName;
	}

	@ColumnAccess(columnName = "Тип")
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
		getTypeAndChannels(this.sectionName);
	}


	public Integer getIecIn() {
		return iecIn;
	}


	public void setIecIn(Integer iecIn) {
		this.iecIn = iecIn;
	}


	public Integer getIecOut() {
		return iecOut;
	}


	public void setIecOut(Integer iecOut) {
		this.iecOut = iecOut;
	}
	
	public TYPE getType() {
		return type;
	}

	public int getChannels() {
		return channels;
	}
	
	private void getTypeAndChannels(String section){
		Matcher matcher = PATTERN_TYPE.matcher(section);
		if (matcher.find()){
			 int numType = Integer.parseInt(matcher.group(1));
			 for (TYPE t : TYPE.values()){
				 if (t.getId() == numType){
					 type = t;
					 break;
				 }
			 }
			 channels = Integer.parseInt(matcher.group(2));
		}
	}

	public int getIecInSize() {
		return iecInSize;
	}

	public void setIecInSize(int iecInSize) {
		this.iecInSize = iecInSize;
	}

	public int getIecOutSize() {
		return iecOutSize;
	}

	public void setIecOutSize(int iecOutSize) {
		this.iecOutSize = iecOutSize;
	}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder("Module: ");
		str.append(name);
		str.append(", iecIn: ").append(iecIn);
		str.append(", iecOut: ").append(iecOut);
		return str.toString();
	}
	
	public enum TYPE{
		DI(1), DO(10), AI(8), AI_PT100(44), AO(14), _750_652_24(30), _750_652_48(33), _750_652_8(40), _750_602(0);
		
		private int id;
		
		private TYPE(int id){
			this.id = id;
		}
		
		public int getId(){
			return id;
		}
		
	}
	
}
