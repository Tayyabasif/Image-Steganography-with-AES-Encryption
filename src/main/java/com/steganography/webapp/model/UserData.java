package com.steganography.webapp.model;

public class UserData {

	String message;
	String password;
	String encryption;
	String process;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEncryption() {
		return encryption;
	}
	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	@Override
	public String toString() {
		return "UserData [message=" + message + ", password=" + password + ", encryption=" + encryption + ", process="
				+ process + "]";
	}
	
	
	
}
