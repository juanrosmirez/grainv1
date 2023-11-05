package ar.edu.unlam.criptografía.grain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Converter {

	public static byte[] imageToByteArray(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "bmp", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		byte[] imagenByteArray = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return imagenByteArray;
	}

	public static BufferedImage byteArrayToImage(byte[] imagenByteArray) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(imagenByteArray);
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		inputStream.close();
		return bufferedImage;
	}
}
