package com.steganography.webapp.service;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

	import javax.crypto.Cipher;
	import javax.crypto.KeyGenerator;
	import javax.crypto.SecretKey;
	import javax.crypto.spec.GCMParameterSpec;
	import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

	
	@Service
	public class AESService {
	
	   // private SecretKey key;
	    //private int KEY_SIZE = 128;
	    private int T_LEN = 128;
	    private byte[] IV;
	    Cipher encryptionCipher;
	    

	   /* public void init() throws Exception {
	        KeyGenerator generator = KeyGenerator.getInstance("AES");
	        generator.init(KEY_SIZE);
	        key = generator.generateKey();
	    }

	    public void initFromStrings(String secretKey, String IV) {
	    	
	        key = new SecretKeySpec(decode(secretKey), "AES");
	        this.IV = decode(IV);
	    }	*/
	    public String encrypt(String message,SecretKeySpec key,byte[] salt) throws Exception {
	    	byte[] iv = new byte[12];
	    	new SecureRandom().nextBytes(iv);
	    	GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
	        byte[] messageInBytes = message.getBytes();
	        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
	        encryptionCipher.init(Cipher.ENCRYPT_MODE, key,spec);
	        byte[] ciphertext = encryptionCipher.doFinal(messageInBytes);
	        
	        byte[] encryptedBytes = new byte[iv.length + ciphertext.length + salt.length];
	        System.arraycopy(iv, 0, encryptedBytes, 0, iv.length);
	        System.arraycopy(salt, 0, encryptedBytes, iv.length, salt.length);
	        System.arraycopy(ciphertext, 0, encryptedBytes, iv.length+salt.length, ciphertext.length);
	       
	        return encode(encryptedBytes);
	    }
	    

	    public String decrypt(String encryptedMessage,SecretKeySpec key) throws Exception {
	        byte[] messageInBytes = decode(encryptedMessage);
	        byte[] iv = Arrays.copyOfRange(messageInBytes, 0, 12);
	        byte[] ciphertext = Arrays.copyOfRange(messageInBytes, 28, messageInBytes.length);
	        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
	        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
	        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
	        byte[] decryptedBytes = decryptionCipher.doFinal(ciphertext);
	        return new String(decryptedBytes);
	    }



	    private String encode(byte[] data) {
	        return Base64.getEncoder().encodeToString(data);
	    }

	    private byte[] decode(String data) {
	        return Base64.getDecoder().decode(data);
	    }
	    
	    public SecretKeySpec getAESKeyFromPassword(String password, byte[] salt) throws Exception {
	        int iterations = 65536;
	        int keyLength = 128; // bits (128 for AES-128)

	        // Create key specification
	        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);

	        // Use PBKDF2 with HMAC-SHA256
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

	        // Generate the secret key and wrap in SecretKeySpec for AES
	        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
	        return new SecretKeySpec(keyBytes, "AES");
	    }

	    // Generate a secure random 128-bit salt
	    public byte[] generateSalt() {
	        byte[] salt = new byte[16]; // 16 bytes = 128 bits
	        new SecureRandom().nextBytes(salt);
	        return salt;
	    }
	    
	    public byte[] getSalt(String message) {
	    	byte[] encryptedBytes =decode(message);
	    	byte[] salt = Arrays.copyOfRange(encryptedBytes, 12, 28);
	    	return salt;
	    	
	    }


	   /* public static void main(String[] args) {
	        try {
	            Server server = new Server();
	            server.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==", "e3IYYJC2hxe24/EO");
	            String encryptedMessage = server.encrypt("TheXCoders_2");
	            System.err.println("Encrypted Message : " + encryptedMessage);
	        } catch (Exception ignored) {
	        }
	    }*/
	   
	}
	

