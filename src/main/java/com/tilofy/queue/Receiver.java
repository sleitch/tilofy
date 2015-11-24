package com.tilofy.queue;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tilofy.ImageResizeService;

/**
 * 
 * Messaging reciever that will process requets to resize and image.
 * 
 * @author Stu2
 *
 */
@Component
public class Receiver {
	
	static private final Logger logger = LoggerFactory.getLogger(Receiver.class);

	@Autowired
    private ImageResizeService imageService;
	
	@Autowired
	private PhotoQueueService photoService;
	

	/**
	 * Receive a MQ message request to resize and image.
	 * 
	 * @param message - map of params (id, width, height, url)
	 */
	public void receiveMessage(Map<String, Object>  message) {
				
		String id = (String)message.get(PhotoQueueService.MSG_ID);
		Integer width = (Integer)message.get(PhotoQueueService.MSG_WIDTH);
		Integer height = (Integer)message.get(PhotoQueueService.MSG_HEIGHT);
		String url = (String)message.get(PhotoQueueService.MSG_URL);

		logger.debug("[receiveMessage] msg: {}", message);
		
		QueueStatus updatedStatus = new QueueStatus();
		updatedStatus.setId(id);
		try {
			
			//
			// resize the image...
			
			String reseizedImg = imageService.resizeImage(url,width,height);
			
			//
			// Update the status....
			
			updatedStatus.setImage(reseizedImg);
			updatedStatus.setStatus("complete");

			photoService.updateStatus(updatedStatus);
			
		} catch (Exception e) {
			logger.error("Failed to resize image!", e);
			updatedStatus.setStatus("Failed: " + e.getMessage());
		}
	
	}

}
