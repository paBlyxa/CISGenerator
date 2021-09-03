package com.we.cisgenerator.service;

import com.we.cisgenerator.model.CCDCom;
import com.we.cisgenerator.model.DMC;
import com.we.cisgenerator.model.DMC.DMC_TYPE;
import com.we.cisgenerator.model.LCDM;
import com.we.cisgenerator.model.PLC;
import com.we.cisgenerator.util.HexUtils;
import com.we.modbus.ModbusTCPMaster;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModbusReadWriteService {

	private final static Logger logger = LoggerFactory.getLogger(ModbusReadWriteService.class);

	private final static int FIRST_REGISTER = 22288;
	private final static int FIRST_COM_PORT = 2;
	private final static int LAST_COM_PORT = 6;

	public static Task<Map<Integer, CCDCom>> getReadParametersTask(PLC plc) {
		return new Task<Map<Integer, CCDCom>>() {
			@Override
			public Map<Integer, CCDCom> call() {
				Map<Integer, CCDCom> comParameters = new HashMap<Integer, CCDCom>();
				int count = 0;
				try {
					this.updateMessage("Connecting to " + plc.getIpAddress());
					this.updateProgress(-1, LAST_COM_PORT);
					ModbusTCPMaster master = new ModbusTCPMaster(plc.getIpAddress(), 502);
					this.updateProgress(0, LAST_COM_PORT);
					this.updateMessage("Connection to " + plc.getIpAddress() + " is established");
					logger.debug("Created new ModbusTCPMaster - {}", plc.getIpAddress());
					this.updateProgress(1, 6);
					for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++) {
						byte[] values = new byte[2];
						values[0] = Function.READ_COM_PARAM.getCode();
						values[1] = (byte) (i << 4);
						if (!master.writeMultipleRegisters(FIRST_REGISTER, 1, values)) {
							logger.warn("Write registers {} not success", FIRST_REGISTER);
							master.close();
							return null;
						}
						byte[] results = new byte[256];
						count = 0;
						while (results[0] != 0x11 && count != 5) {
							Thread.sleep(200);
							if (!master.readMultipleRegisters(FIRST_REGISTER, 25, results)) {
								logger.warn("Read registers {} not success", FIRST_REGISTER);
								master.close();
								return null;
							}
							count++;
						}
						if (count == 5) {
							logger.warn("No response from PLC");
							master.close();
							return null;

						}
						CCDCom comParameter = new CCDCom(i);
						int lcdmCount = results[3];
						logger.debug("COM{} lcdmCount {}", i, lcdmCount);
						for (int j = 1; j <= lcdmCount; j++) {
							values[0] = Function.READ_ALL_CHANNELS.getCode();
							values[1] = (byte) ((i << 4) + j);
							if (!master.writeMultipleRegisters(FIRST_REGISTER, 1, values)) {
								logger.warn("Write registers {} not success", FIRST_REGISTER);
								master.close();
								return null;
							}
							count = 0;
							results[0] = 0x00;
							while (results[0] != 0x15 && count != 5) {
								Thread.sleep(200);
								if (!master.readMultipleRegisters(FIRST_REGISTER, 100, results)) {
									logger.warn("Read registers {} not success", FIRST_REGISTER);
									master.close();
									return null;
								}
								count++;
							}
							if (count == 5) {
								logger.warn("No response from PLC");
								master.close();
								return null;

							}
							comParameter.addnewLCDM(results[3]);
							LCDM lcdm = comParameter.getLCDMbyAddr(results[3]);
							lcdm.setChannelMask((short) ((results[4] << 8) + (results[5] & 0xFF)));
							for (int k = 1; k <= 16; k++) {
								DMC dmc = lcdm.getDMC(k);
								dmc.setRegAddress(HexUtils.bytesToInt16(results, k * 12 - 6));
								dmc.setSensorMax(HexUtils.bytesToFloat(results, k * 12 - 4));
								dmc.setSensorMin(HexUtils.bytesToFloat(results, k * 12));
								short type = (short) (((results[k * 12 + 4] & 0xFF) << 8) + (results[k * 12 + 5] & 0xFF));
								for (DMC_TYPE t : DMC_TYPE.values()) {
									if (t.getValue() == type)
										dmc.setType(t);
								}
							}
							logger.debug("Get {}", lcdm);
						}
						comParameters.put(i, comParameter);
						this.updateProgress(i, 6);
					}
					this.updateMessage("Read parameters's done");
				} catch (IOException e) {
					logger.error("An error occured while reading parameters", e);
					this.updateMessage(e.getMessage());
					this.updateProgress(0, LAST_COM_PORT);
				} catch (InterruptedException e) {
					logger.warn("A task was interrupted", e);
					this.updateProgress(0, LAST_COM_PORT);
				}
				return comParameters;
			}
		};

	}

	public static Task<Void> getWriteParametersTask(PLC plc){
		return new Task<Void>(){
			@Override
			public Void call(){
				try {
					this.updateMessage("Connecting to " + plc.getIpAddress());
					this.updateProgress(-1, LAST_COM_PORT);
					ModbusTCPMaster master = new ModbusTCPMaster(plc.getIpAddress(), 502);
					this.updateProgress(0, LAST_COM_PORT);
					this.updateMessage("Connection to " + plc.getIpAddress() + " is established");
					logger.debug("Created new ModbusTCPMaster - {}", plc.getIpAddress());
					this.updateProgress(1, LAST_COM_PORT);
					for (int i = FIRST_COM_PORT; i <= LAST_COM_PORT; i++){
						byte[] values = new byte[256];
						int countRegs = 0;
						CCDCom comParameter = plc.getComParameters().get(i);
						for (int j = 1; j <= CCDCom.LCDM_MAX_COUNT; j++) {
							LCDM lcdm = (comParameter == null) || (comParameter.getCountLCDM() < j) ? null : comParameter.getLCDM(j);
							// Code function
							values[0] = Function.WRITE_ALL_CHANNELS.getCode();
							values[1] = (byte) ((i << 4) + j);
							countRegs = 1;
							if (lcdm != null){
								// Adress LCDM
								values[2] = 0;
								values[3] = lcdm.getAddress();
								countRegs++;
								// Channel Mask
								values[4] = (byte)(lcdm.getChannelMask() >> 8);
								values[5] = (byte)(lcdm.getChannelMask() & 0xFF);
								countRegs++;
								for (int k = 1; k <= 16; k++){
									DMC dmc = lcdm.getDMC(k);
									// Address register
									values[k * 12 - 6] = (byte)(dmc.getRegAddress() >> 8);
									values[k * 12 - 5] = (byte)(dmc.getRegAddress() & 0xFF);
									// Sensor max
									int floatValue = Float.floatToIntBits(dmc.getSensorMax());
									values[k * 12 - 4] = (byte)(floatValue >> 8);
									values[k * 12 - 3] = (byte)(floatValue);
									values[k * 12 - 2] = (byte)(floatValue >> 24);
									values[k * 12 - 1] = (byte)(floatValue >> 16);
									// Sensor min
									floatValue = Float.floatToIntBits(dmc.getSensorMin());
									values[k * 12] = (byte)(floatValue >> 8);
									values[k * 12 + 1] = (byte)(floatValue);
									values[k * 12 + 2] = (byte)(floatValue >> 24);
									values[k * 12 + 3] = (byte)(floatValue >> 16);
									// Type
									values[k * 12 + 4] = (byte)(dmc.getType().getValue() >> 8);
									values[k * 12 + 5] = (byte)(dmc.getType().getValue() & 0xFF);
									
									countRegs += 6;;
								}
							} else{
								for (int k = 2; k <= 197; k++){
									values[k] = 0;
								}
								countRegs += 98;
							}
							// Write values by modbus							
							if (!master.writeMultipleRegisters(FIRST_REGISTER, countRegs, values)) {
								logger.warn("Write registers {} not success", FIRST_REGISTER);
								master.close();
								return null;
							}
							// Wait response
							int count = 0;
							byte[] results = new byte[256];
							while(results[0] != 0x16 && count != 5){
								Thread.sleep(200);
								if (!master.readMultipleRegisters(FIRST_REGISTER, 1, results)) {
									logger.warn("Read registers {} not success", FIRST_REGISTER);
									master.close();
									return null;
								}
								count++;
							}
							if (count == 5){
								logger.warn("No response from PLC");
								this.updateMessage("No response from PLC");
								master.close();
								return null;
							}
							logger.info("Write parameters COM{} CCD{}", i, j);			
						}
						// Write count LCDM
						values[0] = Function.WRITE_COM_PARAM.getCode();
						values[1] = (byte) (i << 4);
						values[2] = 0;
						values[3] = comParameter == null ? 0 : (byte)(comParameter.getCountLCDM() & 0xFF);
						if (!master.writeMultipleRegisters(FIRST_REGISTER, 2, values)) {
							logger.warn("Write registers {} not success", FIRST_REGISTER);
							master.close();
							return null;
						}
						byte[] results = new byte[256];
						int count = 0;
						while(results[0] != 0x12 && count != 5){
							Thread.sleep(200);
							if (!master.readMultipleRegisters(FIRST_REGISTER, 1, results)) {
								logger.warn("Read registers {} not success", FIRST_REGISTER);
								master.close();
								return null;
							}
							count++;
						}
						if (count == 5){
							logger.warn("No response from PLC");
							this.updateMessage("No response from PLC");
							master.close();
							return null;
						}
						this.updateProgress(i, LAST_COM_PORT);
					}
				} catch (IOException e) {
					logger.error("An error occured while reading parameters", e);
					this.updateProgress(0, LAST_COM_PORT);
				} catch (InterruptedException e){
					logger.warn("A task was interrupted", e);
					this.updateProgress(0, LAST_COM_PORT);
				}
				this.updateMessage("Write parameters's done");
				return null;
			}
		};
	}

	public enum Function {
		READ_COM_PARAM((byte) 0x01), WRITE_COM_PARAM((byte) 0x02), READ_ONE_CHANNEL((byte) 0x03), WRITE_ONE_CHANNEL(
				(byte) 0x04), READ_ALL_CHANNELS((byte) 0x05), WRITE_ALL_CHANNELS((byte) 0x06), READ_DIAGN((byte) 0x07);

		private Function(byte code) {
			this.code = code;
		}

		private byte code;

		public byte getCode() {
			return code;
		}
	}
}
