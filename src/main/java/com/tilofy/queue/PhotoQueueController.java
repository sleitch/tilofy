package com.tilofy.queue;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for pushing and pulling from the image reszie Queue.
 * 
 * Uses the <code>photoService</code> to retrieve and resize images.
 * 
 * @author Stu2
 *
 */
@RestController
public class PhotoQueueController  implements Serializable{

	private static final long serialVersionUID = -851513127338413256L;

	static protected final Logger logger = LoggerFactory.getLogger(PhotoQueueController.class);


	static int jobId = 1000;
	
	@Autowired
	private PhotoQueueService photoService;
	
	/**
	 * GET all jobs status from the queue.
	 * 
	 * @return Map containing the result.
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/queue", method = RequestMethod.GET, produces = "application/json")
	public  Map<String, Object> readQueueAll() throws Exception {
		Map<String, Object>  response = new HashMap<String,Object>();
		
		List<QueueStatus> list = photoService.lookupAllJobs();
				
		logger.info("Size of list from lookupAllJobs: {}", list == null ? 0 : list.size() );

		response.put("result",list);

		
		return response;

	}
	
	/**
	 * GET a specific job status from the queue.
	 * 
	 * @param jobId - path variable to the job id.
	 * @return Map containing the result.
	 * 
	 * @throws Exception
	 */
	/**
	 */
	@RequestMapping(value = "/queue/{jobId}", method = RequestMethod.GET, produces = "application/json")
	public  Map<String, Object> readQueue( @PathVariable String jobId) throws Exception {
		Map<String, Object>  response = new HashMap<String,Object>();

		
		if (jobId == null) {
			response.put("error", "no job id given");

			
		} else {
			
			QueueStatus qstatus = photoService.lookupJob(jobId);
			if (qstatus == null) {
				response.put("error", "unknonw job id");
			} else {
				response.put("result",qstatus);

			}
			
		}
		return response;

	}


	
	@RequestMapping(value = "/queue", method = RequestMethod.POST, produces = "application/json")
	public  Map<String, Object> queue( @RequestParam(value = "url", required = false) String url,  @RequestParam(value = "size", required = false) String size) throws Exception {

		
			logger.info("********TODO /queue, url: {}, size: {}", url, size);
	
		
		Map<String, Object>  request = new HashMap<String,Object>();

		List<String>  errors = validate(url, size, request);
	
		Map<String, Object>  response = new HashMap<String,Object>();

		if (errors.isEmpty()) {
			
			//
			// ..then all good. 			
		//	request.put("id", UUID.randomUUID().toString());
			request.put("id", ""+jobId++ );

			
			photoService.queueImageTransform(request);
			
			//
			// Simply return the message id with the q request
			
			response = request;
			response.put("jobs",readQueueAll());

		}else {
			response.put("errors", errors);
		}
	
		response.put("success", errors.isEmpty());
			
		return response;
       	
	}
	
	/**
	 * Validates input params. The <code>request</code> is added to if valid.
	 *  
	 * @param url - must be a url to a image
	 * @param size - format is <pre><int width< x | X> < int height></pre>, for example, 400x600
	 * @param request - this is filled with valid params
	 * 
	 * @return a list of errors - will be empty if valid
	 */
	private List<String> validate(final String url, final String size, Map<String, Object> request) {
		List<String> errors = new ArrayList<String>();
		
		if (size == null || size.length() == 0) {
			errors.add("Size parameter expected, example: 800x600");
		}else {
			
			String parts[]  = size.trim().split("x");
		
			if (parts == null){
				parts = size.split("X");				
			} 
			if (parts == null || parts.length != 2){
				errors.add("Size parameter format is <width>x<height>, example: 800x600");
			}else {
				try {
					request.put("width", Integer.parseInt(parts[0]));
					request.put("height", Integer.parseInt(parts[1]));
				} catch (Exception e) {
					errors.add("Size parameter format is <integer width>x<integer height>, example: 800x600");
				}				
			}
		}
		
		if (url == null || url.length() == 0) {
			errors.add("'url' parameter expected. Must be a URL to an image.");
		} else {
			
			if( !isImage(url.trim())) {
				errors.add("'url' parameter does not appear to be an image.");
			}else{
				request.put("url", url.trim());
			}
		}
		
		return errors;
	}
	
	
	/**
	 * Check image url
	 * 
	 * @param url - url of potential image
	 * @return true if image
	 */
	public Boolean isImage(String url){  
        try {  
            
            return ImageIO.read(new URL(url)) != null;
          
        } catch (Exception e) {  
           logger.error("Failed to read image!",e);
        }
        return false;
    }

	
	

	
}
