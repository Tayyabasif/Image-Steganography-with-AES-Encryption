package com.steganography.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageStegangraphyProjectApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		
		SpringApplication.run(ImageStegangraphyProjectApplication.class, args);
	}

}
