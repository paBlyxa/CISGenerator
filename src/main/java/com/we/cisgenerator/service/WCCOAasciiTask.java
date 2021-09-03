package com.we.cisgenerator.service;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WCCOAasciiTask extends Task<Void> {

	private final static Logger logger = LoggerFactory.getLogger(WCCOAasciiTask.class);
	
	private final String path;
	private final String program;
	private final List<String> commands;
	
	public WCCOAasciiTask(){
		this.path = "c:/Siemens/Automation/WinCC_OA/3.15/bin";
		this.program = "WCCOAascii.exe";
		this.commands = new ArrayList<String>();
		commands.add(path + "/" + program);
		logger.debug("Create new WCCOAasciiTask {}/{}", path, program);
	}
	
	public WCCOAasciiTask(String... args){
		this.path = "c:/Siemens/Automation/WinCC_OA/3.15/bin";
		this.program = "WCCOAascii.exe";
		this.commands = new ArrayList<String>();
		commands.add(path + "/" + program);
		for (String arg: args){
			commands.add(arg);
		}
		logger.debug("Create new WCCOAasciiTask {}/{}", path, program);
	}
	
	@Override
	public Void call() throws Exception {
		
		logger.debug("Call");
		ProcessBuilder b = new ProcessBuilder(commands);
		final Process pr = b.start();
		
		BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		
		String line = null;
		
		while((line = input.readLine()) != null){
			logger.debug(line);
		}
		while((line = err.readLine()) != null){
			logger.warn(line);
		}
		
		int exitVal = pr.waitFor();
		logger.debug("Exited with error code " + exitVal);
		return null;
	}

}
