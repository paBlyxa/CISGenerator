package com.we.cisgenerator.util;

public class HexUtils {

	public static float bytesToFloat(byte[] bytes, int index){
		return Float.intBitsToFloat(
				((bytes[index] & 0xFF)  << 8) + (bytes[index + 1] & 0xFF)
				+ (  ( ((bytes[index + 2] & 0xFF) << 8) + (bytes[index + 3] & 0xFF) ) << 16)
				);
	}
	
	public static int bytesToInt16(byte[] bytes, int index){
		return (bytes[index] << 8 & 0xFF00) + (bytes[index + 1] & 0xFF);
	}
}
