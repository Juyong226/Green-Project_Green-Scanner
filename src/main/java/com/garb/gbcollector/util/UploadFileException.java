package com.garb.gbcollector.util;

@SuppressWarnings("serial")
public class UploadFileException extends RuntimeException {

	public UploadFileException(String message) {
		super(message);
	}
	
	public UploadFileException(String message, Throwable cause) {
		super(message);
	}
}
