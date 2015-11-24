package com.tilofy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * Service used to resize a given image.
 * 
 * The image is stored in the local file location specified by <code>imageDir</code>
 * 
 * @author Stu2
 *
 */
@Component
public class ImageResizeService  implements Serializable{

	private static final long serialVersionUID = -851513127338413256L;

	static protected final Logger logger = LoggerFactory.getLogger(ImageResizeService.class);

	@Value("${resized.images.dir}")
	private String imageDir;
	
	   
	   
   /**
    * Resize and Image and store in the file system.
	*
    * @param imageUrl
    * @param width
    * @param height
    * @return file path of resized image
    * @throws Exception
    */
	public String resizeImage(final String imageUrl, final int width, final int height) throws Exception {

		URL url = new URL(imageUrl);
		BufferedImage inputImage = ImageIO.read(url);

		//
		// Create new image
		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

		//
		// Set new width/height
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, width, height, null);
		g2d.dispose();
		

		String formatName = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

		// writes to output file
		
		String outputImagePath = imageDir;
		new File(outputImagePath).mkdirs();

		outputImagePath = outputImagePath + "\\"+ fileName;
		
		if (logger.isDebugEnabled()) {
			logger.debug("outputImagePath: {}, formatName: {}, fileName: {}", outputImagePath, formatName, fileName);
		}
		
		ImageIO.write(outputImage, formatName, new File(outputImagePath));

		return outputImagePath;
	}
	
}
