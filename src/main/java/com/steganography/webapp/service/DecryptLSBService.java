package com.steganography.webapp.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DecryptLSBService {

	public String Decrypt(MultipartFile newImageFile) {
        
		String decodedMessage="";
		BufferedImage image;
		try {
			image = ImageIO.read(convertMultipartFileToFile(newImageFile));
			Pixel[] pixels = GetPixelArray(image);
			decodedMessage=DecodeMessageFromPixels(pixels);
			System.out.println(decodedMessage); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decodedMessage;
	}
	
public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		
		File file = new File(System.getProperty("java.io.tmpdir")+"/"+multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		return file;
	}
	
	private Pixel[] GetPixelArray(BufferedImage imageToEncrypt){
		int height = imageToEncrypt.getHeight();
		int width = imageToEncrypt.getWidth();
		Pixel[] pixels = new Pixel[height * width];
		
		int count = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {				
				Color colorToAdd = new Color(imageToEncrypt.getRGB(x, y));
				pixels[count] = new Pixel(x, y, colorToAdd);
				count++;
			}
		}
		return pixels;
	};
	
	private String DecodeMessageFromPixels(Pixel[] pixels) {
		boolean completed = false;
		int pixelArrayIndex = 0; 
		StringBuilder messageBuilder = new StringBuilder("");
		while(completed == false) {
			Pixel[] pixelsToRead = new Pixel[3];
			for(int i = 0; i < 3; i++) {
				pixelsToRead[i] = pixels[pixelArrayIndex];
				pixelArrayIndex++;
			}
			messageBuilder.append(ConvertPixelsToCharacter(pixelsToRead));
			if(IsEndOfMessage(pixelsToRead[2]) == true) {
				completed = true;
			}
		}
		return messageBuilder.toString();
	}
	
	private char ConvertPixelsToCharacter(Pixel[] pixelsToRead) {
		ArrayList<String> binaryValues = new ArrayList<String>();
		for(int i = 0; i < pixelsToRead.length; i++) {
			String[] currentBinary = TurnPixelIntegersToBinary(pixelsToRead[i]);
			binaryValues.add(currentBinary[0]);
			binaryValues.add(currentBinary[1]);
			binaryValues.add(currentBinary[2]);
		}
		return ConvertBinaryValuesToCharacter(binaryValues);
	}
	
	private String[] TurnPixelIntegersToBinary(Pixel pixel) {
		String[] values = new String[3];
		values[0] = Integer.toBinaryString(pixel.getColor().getRed());
		values[1] = Integer.toBinaryString(pixel.getColor().getGreen());
		values[2] = Integer.toBinaryString(pixel.getColor().getBlue());
		return values;
	}
	
	private boolean IsEndOfMessage(Pixel pixel) {
		if(TurnPixelIntegersToBinary(pixel)[2].endsWith("1")) {
			return false;
		}
		return true;
	}
	
	private char ConvertBinaryValuesToCharacter(ArrayList<String> binaryValues) {
		StringBuilder endBinary = new StringBuilder("");
		for(int i = 0; i < binaryValues.size()-1; i++) {
			endBinary.append(binaryValues.get(i).charAt(binaryValues.get(i).length()-1));
		}
		String endBinaryString = endBinary.toString();
		String noZeros = RemovePaddedZeros(endBinaryString);
		int ascii = Integer.parseInt(noZeros, 2);
		return (char) ascii;
	}
	
	private String RemovePaddedZeros(String endBinary) {
		StringBuilder builder = new StringBuilder(endBinary);
		int paddedZeros = 0;
		for(int i = 0; i < builder.length(); i++) {
			if(builder.charAt(i) == '0') {
				paddedZeros++;
			}
			else {
				break;
			}
		}
		for(int i = 0 ; i < paddedZeros; i++) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
	
}
