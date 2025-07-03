package com.steganography.webapp.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users {
	
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
private String email;
private String userPassword;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getUserPassword() {
	return userPassword;
}
public void setUserPassword(String userPassword) {
	this.userPassword = userPassword;
}


	
}
