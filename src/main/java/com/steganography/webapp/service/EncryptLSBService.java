package com.steganography.webapp.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EncryptLSBService {

	public void Encrypt(MultipartFile imageFile, String message) {
		
		
		String directory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		String newImageFileString = directory + "\\export.png";
		File newImageFile = new File(newImageFileString);
		
		BufferedImage image;
		try {
			image = ImageIO.read(convertMultipartFileToFile(imageFile));
			BufferedImage imageToEncrypt = GetImageToEncrypt(image);
			Pixel[] pixels = GetPixelArray(imageToEncrypt);
			String[] messageInBinary = ConvertMessageToBinary(message);
			EncodeMessageBinaryInPixels(pixels, messageInBinary);
			ReplacePixelsInNewBufferedImage(pixels, image);
			SaveNewFile(image, newImageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		
		File file = new File(System.getProperty("java.io.tmpdir")+"/"+multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		return file;
	}
	
	/*
	 * Copies the image into a new buffered image.
	 */
	private BufferedImage GetImageToEncrypt(BufferedImage image) {
		ColorModel colorModel = image.getColorModel();
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}
	
	/*
	 * Gets two dimensional array of colors from the image to encrypt.
	 */
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
	
	/*
	 * Converting the message into binary.
	 */
	private String[] ConvertMessageToBinary(String message) {
		int[] messageInAscii = ConvertMessageToAscii(message);
		String[] binary = ConvertAsciiToBinary(messageInAscii);
		return binary;
	}
	
	/*
	 * Converting the message into ASCII.
	 */
	private int[] ConvertMessageToAscii(String message) {
		int[] messageCharactersInAscii = new int[message.length()];
		for(int i = 0; i < message.length(); i++) {
			int asciiValue = (int) message.charAt(i);
			messageCharactersInAscii[i] = asciiValue;
		}
		return messageCharactersInAscii;
	}
	
	/*
	 * Converting the ASCII code to Binary.
	 */
	private String[] ConvertAsciiToBinary(int[] messageInAscii) {
		String[] messageInBinary = new String[messageInAscii.length];
		for(int i = 0; i < messageInAscii.length; i++) {
			String asciiBinary = LeftPadZeros(Integer.toBinaryString(messageInAscii[i]));
			messageInBinary[i] = asciiBinary;
		}
		return messageInBinary;
	}
	
	/*
	 * Left padding the binary value with zeros to make an 8 digit string.
	 */
	private String LeftPadZeros(String value) {
		StringBuilder paddedValue = new StringBuilder("00000000");
		int offSet = 8 - value.length();
		for(int i = 0 ; i < value.length(); i++) {
			paddedValue.setCharAt(i+offSet, value.charAt(i));
		}
		return paddedValue.toString();
	}
	
	
	/*
	 * Encoding the message in the pixels.
	 */
	private void EncodeMessageBinaryInPixels(Pixel[] pixels, String[] messageBinary) {
		int pixelIndex = 0;
		boolean isLastCharacter = false;
		for(int i = 0; i < messageBinary.length; i++) {
			Pixel[] currentPixels = new Pixel[] {pixels[pixelIndex], pixels[pixelIndex+1], pixels[pixelIndex+2]};
			if(i+1 == messageBinary.length) {
				isLastCharacter = true; 
			}
			ChangePixelsColor(messageBinary[i], currentPixels, isLastCharacter);
			pixelIndex = pixelIndex +3;
		}
	}
	
	private void ChangePixelsColor(String messageBinary, Pixel[] pixels, boolean isLastCharacter) {
		int messageBinaryIndex = 0;
		for(int i =0; i < pixels.length-1; i++) {
			char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), messageBinary.charAt(messageBinaryIndex+2)};
			String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[i], messageBinaryChars);
			pixels[i].setColor(GetNewPixelColor(pixelRGBBinary));
			messageBinaryIndex = messageBinaryIndex + 3;
		}
		if(isLastCharacter == false) {
			char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), '1'};
			String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[pixels.length-1], messageBinaryChars);
			pixels[pixels.length-1].setColor(GetNewPixelColor(pixelRGBBinary));
		}else {
			char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), '0'};
			String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[pixels.length-1], messageBinaryChars);
			pixels[pixels.length-1].setColor(GetNewPixelColor(pixelRGBBinary));
		}
	}
	
	private String[] GetPixelsRGBBinary(Pixel pixel, char[] messageBinaryChars) {
		String[] pixelRGBBinary = new String[3];
		pixelRGBBinary[0] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getRed()), messageBinaryChars[0]);
		pixelRGBBinary[1] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getGreen()), messageBinaryChars[1]);
		pixelRGBBinary[2] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getBlue()), messageBinaryChars[2]);
		return pixelRGBBinary;
	}
	
	private String ChangePixelBinary(String pixelBinary, char messageBinaryChar) {
		StringBuilder sb = new StringBuilder(pixelBinary);
		sb.setCharAt(pixelBinary.length()-1, messageBinaryChar);
		return sb.toString();
	}
	
	private Color GetNewPixelColor(String[] colorBinary) {
		return new Color(Integer.parseInt(colorBinary[0], 2), Integer.parseInt(colorBinary[1], 2), Integer.parseInt(colorBinary[2], 2));
	}
	
	private void ReplacePixelsInNewBufferedImage(Pixel[] newPixels, BufferedImage newImage) {
		for(int i = 0; i < newPixels.length; i++) {
			newImage.setRGB(newPixels[i].getX(), newPixels[i].getY(), newPixels[i].getColor().getRGB());
		}
	};
	
	private void SaveNewFile(BufferedImage newImage, File newImageFile) {
		try {
			ImageIO.write(newImage, "png", newImageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
