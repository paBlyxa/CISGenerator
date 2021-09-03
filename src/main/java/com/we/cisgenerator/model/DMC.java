package com.we.cisgenerator.model;

public class DMC {

	private int regAddress;
	private float sensorMax;
	private float sensorMin;
	private int adrCompensation;
	private DMC_TYPE type;
	
	public DMC(){
		this.type = DMC_TYPE.RAW_VALUE;
	}
	
	public enum DMC_TYPE{
		
		RAW_VALUE((short)0, ""),
		T_50M_1_4280_Cu_50((short)1, "Ом"),
		T_50M_1_4260_Cu_50((short)3, "Ом"),
		T_100M_1_4280_Cu_100((short)4, "Ом"),
		T_100M_1_4260_Cu_100((short)5, "Ом"),
		T_53M_1_4260_gr23((short)6, "Ом"),
		T_50M_0_004280((short)7, "Ом"),
		T_100M_0_004280((short)8, "Ом"),
		T_100P_1_3910_Pt_100((short)0x0A, "Ом"),
		T_50P_1_3910_Pt_50((short)0x0B, "Ом"),
		T_100P_1_3850_Pt_100((short)0x0C, "Ом"),
		T_50P_1_3850_Pt_50((short)0x0D, "Ом"),
		T_100P_0_00391((short)0x0E, "Ом"),
		T_50P_0_00391((short)0x0F, "Ом"),
		T_100P_0_00385((short)0x10, "Ом"),
		T_50P_0_00385((short)0x11, "Ом"),
		T_GR21((short)0x13, "Ом"),
		T_TPP_R((short)0x80, "мВ"),
		T_TPP_S((short)0x81, "мВ"),
		T_TPR_B((short)0x82, "мВ"),
		T_TJK_J((short)0x83, "мВ"),
		T_TMK_T((short)0x84, "мВ"),
		T_TXK_E((short)0x85, "мВ"),
		T_TXK_A((short)0x86, "мВ"),
		T_THH_N((short)0x87, "мВ"),
		T_TBP_A1((short)0x88, "мВ"),
		T_TBP_A2((short)0x89, "мВ"),
		T_TBP_A3((short)0x8A, "мВ"),
		T_TXK_L((short)0x8B, "мВ"),
		T_TMK_M((short)0x8C, "мВ"),
		T_TMK_E((short)0x8D, "мВ"),
		V_0_1((short)0xC0, "В"),
		V_0_5((short)0xC1, "В"),
		V_0_10((short)0xC2, "В"),
		V_minus5_5((short)0xC3, "В"),
		V_minus10_10((short)0xC4, "В"),
		V_1_5((short)0xC5, "В"),
		A_0_5((short)0xE0, "мА"),
		A_minus5_5((short)0xE1, "мА"),
		A_0_20((short)0xE2, "мА"),
		A_4_20((short)0xE3, "мА"),
		A_20_4((short)0x1E3, "мА"),
		MV_0_500((short)0x1C0, "мВ");
		
		
		private short value;
		private String units;
		
		private DMC_TYPE(short value, String units){
			this.value = value;
		}
		
		public short getValue(){
			return value;
		}
		
		public String getUnits(){
			return units;
		}
	}



	public int getRegAddress() {
		return regAddress;
	}



	public void setRegAddress(int regAddress) {
		this.regAddress = regAddress;
	}



	public float getSensorMax() {
		return sensorMax;
	}



	public void setSensorMax(float sensorMax) {
		this.sensorMax = sensorMax;
	}



	public float getSensorMin() {
		return sensorMin;
	}



	public void setSensorMin(float sensorMin) {
		this.sensorMin = sensorMin;
	}



	public DMC_TYPE getType() {
		return type;
	}



	public void setType(DMC_TYPE type) {
		this.type = type;
	}



	public int getAdrCompensation() {
		return adrCompensation;
	}



	public void setAdrCompensation(int adrCompensation) {
		this.adrCompensation = adrCompensation;
	}
}
