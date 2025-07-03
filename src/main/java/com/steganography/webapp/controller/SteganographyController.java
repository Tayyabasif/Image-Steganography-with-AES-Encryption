package com.steganography.webapp.controller;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.steganography.webapp.model.UserData;
import com.steganography.webapp.model.Users;
import com.steganography.webapp.service.AESService;
import com.steganography.webapp.service.DecryptLSBService;
import com.steganography.webapp.service.EncryptLSBService;
import com.steganography.webapp.service.UserServiceImpl;

import org.springframework.ui.Model;

@Controller
public class SteganographyController {
	
	
	@Autowired
	EncryptLSBService encService;
	
	@Autowired
	DecryptLSBService decService;
	
	@Autowired
	AESService aesService;
	
	@Autowired
	UserServiceImpl userService;

	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@PostMapping("/index")
	public String index1() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute Users user, Model model) {
		boolean status = userService.registerUser(user);
		
		if(status) {
			model.addAttribute("successMsg","User registered successfully");
		}
		else {
			model.addAttribute("errorMsg","User not registered");
		}
		
		return "signup";
	}

	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/start")
	public String encryptOrDecrypt(@ModelAttribute UserData userData,@RequestParam("imageFile") MultipartFile imageFile, Model model) {
		System.out.println(userData.toString());
			
		System.out.println(imageFile.getOriginalFilename());
		
		if(userData.getProcess().equals("Encrypt")) {
			String encMessage="";
			byte[] salt =aesService.generateSalt();
			
			try{
				SecretKeySpec aesKey = aesService.getAESKeyFromPassword(userData.getPassword(), salt);
				encMessage=aesService.encrypt(userData.getMessage(),aesKey,salt);
			}
			catch(Exception e){
		    e.printStackTrace();
			}
			encService.Encrypt(imageFile, encMessage);
		}
		else if(userData.getProcess().equals("Decrypt")) {
			String decMessage="";
			System.out.println("inside dec");
			String msg=decService.Decrypt(imageFile);
			byte[] salt = aesService.getSalt(msg);
			
			try{
				SecretKeySpec aesKey = aesService.getAESKeyFromPassword(userData.getPassword(), salt);
				decMessage=aesService.decrypt(msg,aesKey);
				
			}
			catch(Exception e){
		    e.printStackTrace();
			}
			userData.setMessage(decMessage);
			model.addAttribute("message",decMessage);
		}
			
		return "index";
	}

	
}
