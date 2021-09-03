package com.we.cisgenerator.service;

import com.we.cisgenerator.model.winccoa.*;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointService {
	
	private final static Logger logger = LoggerFactory.getLogger(DatapointService.class);
	
	public static Map<String, Dp> importDp(File file) throws IOException{
		List<List<String>> data = new ArrayList<>();
		Map<String, Dp> dpMap = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			List<String> listStr = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					// Create new field
					if (!listStr.isEmpty()) {
						data.add(listStr);
					}
					listStr = new ArrayList<>();
				} else {
					listStr.add(line);
				}
			}
			if (!listStr.isEmpty()) {
				data.add(listStr);
			}
		} catch (IOException e) {
			logger.error("An error occured while reading file {}", file);
			throw e;
		}
		data.forEach((listString) -> {
			if (listString.size() > 1) {
				String typeField = listString.get(0);
				if (typeField.startsWith("# ")){
					typeField = typeField.substring(2, typeField.length());
				} else if (typeField.startsWith("#")) {
					typeField = typeField.substring(1, typeField.length());
				} else {
					logger.warn("An unknown line from file: \"{}\"", listString.get(0));
				}
				AsciiExportField field = AsciiExportField.valueOf(typeField.split("/")[0]);
				switch(field){
				case AlertValue:
					break;
				case Aliases:
					break;
				case Datapoint:
					for (int i = 2; i < listString.size(); i++){
						Dp dp = createDp(listString.get(i));
						dpMap.put(dp.getDpName(), dp);
					}
					break;
				case DbArchiveInfo:
					break;
				case DistributionInfo:
					// TODO
					/*
					for (int i = 2; i < listString.size(); i++){
						String[] line = listString.get(i).split("\t");
						String[] elementName = line[1].split("\\.");
						invokeDpMethod(dpMap.get(elementName[0]), "set", "driver", line[4]);
					}*/
					break;
				case DpDefaultValue:
					for (int i = 2; i < listString.size(); i++){
						String[] line = listString.get(i).split("\t");
						String[] elementName = line[1].split("\\.");
						invokeDpMethod(dpMap.get(elementName[0]), "setDefault", elementName[1], line[3]);
					}
					break;
				case DpFunction:
					break;
				case DpType:
					break;
				case DpValue:
					for (int i = 2; i < listString.size(); i++){
						String[] line = listString.get(i).split("\t");
						String[] elementName = line[1].split("\\.");
						invokeDpMethod(dpMap.get(elementName[0]), "set", elementName[1], line[3]);									
					}
					break;
				case PeriphAddrMain:
					break;
				case PvssRangeCheck:
					for (int i = 2; i < listString.size(); i++){
						String[] line = listString.get(i).split("\t");
						String[] elementName = line[1].split("\\.");
						Dp dp = dpMap.get(elementName[0]);
						invokeDpMethod(dp, "set", "sensorMax", line[7]);
						invokeDpMethod(dp, "set", "sensorMin", line[6]);
					}
					break;
				default:
					logger.warn("An unknown field {} in Ascii file", field);
					break;
				}
			} else {
				logger.debug("An empty list string");
			}
		});
		return dpMap;
	}
	
	private static void invokeDpMethod(Dp dp, String methodName, String elementName, String value){
		Method method;
		String element = elementName.replaceFirst(elementName.substring(0, 1), elementName.substring(0, 1).toUpperCase());
		try {
			method = dp.getClass().getMethod(methodName + element, String.class);
			method.invoke(dp, value);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static Dp createDp(String str){
		String[] lines = str.split("\t");
		switch(lines[1]){
		case "SDS_DI":
			logger.debug("Create new SDS_DI - {}", lines[0]);
			return new SDS_DI(lines[0]);
		case "SDS_AI":
			logger.debug("Create new SDS_AI - {}", lines[0]);
			return new SDS_AI(lines[0], Integer.parseInt(lines[2]));
		case "SDS_PLC":
			logger.debug("Create new SDS_PLC - {}", lines[0]);
			return new SDS_PLC(lines[0]);
		case "SDS_CALC":
			logger.debug("Create new SDS_CALC - {}", lines[0]);
			return new SDS_PA(lines[0]);
		}
		logger.warn("An unknown dp type {}", lines[1]);
		return null;
	}
}
