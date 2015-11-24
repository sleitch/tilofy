package com.tilofy.queue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Maintains the resize job status in a thread sfe map queue.
 * 
 * Each request to resize and image is sent to Rabbit MQ for processing.
 * 
 * @author Stu2
 *
 */
@Component
public class PhotoQueueService {
	public static final String MSG_ID = "id";
	public static final String MSG_URL= "url";
	public static final String MSG_WIDTH = "width";
	public static final String MSG_HEIGHT = "height";


	public static final String STATUS_PROCESSING = "processing";

	static protected final Logger logger = LoggerFactory.getLogger(PhotoQueueService.class);

	

	@Value("${rabbitmq.image.queue}")
	private String queue;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	
	private final Map<String,QueueStatus>  mapQueue = Collections.synchronizedMap( new LinkedHashMap<String,QueueStatus>()); 


	public Map<String, QueueStatus> getMapQueue() {
	
		return mapQueue;
	}

	/**
	 * Update the status of the Job
	 * 
	 * @param qstatus
	 */
	public void updateStatus(final QueueStatus qstatus) {
		
		QueueStatus qs =getMapQueue().get(qstatus.getId());
		qs.setImage(qstatus.getImage());
		qs.setStatus(qstatus.getStatus());

	}

	/**
	 * Create a processing status entry and sends the requets off to rabbit MQ.
	 * 
	 * Also sets an expire date for the length of time a status stays in the queue map.
	 * 
	 * @param request
	 */
	public void queueImageTransform(Map<String, Object> request) {
		
		logger.info("queueImageTransform queue: {}",queue);

		String id = (String)request.get(MSG_ID);
		
		QueueStatus qstatus = new QueueStatus();
		qstatus.setStatus(STATUS_PROCESSING);
		qstatus.setUrl((String)request.get(MSG_URL));
		qstatus.setId(id);
		
		String fileName = qstatus.getUrl().substring( qstatus.getUrl().lastIndexOf("/") + 1);
		qstatus.setImageName(fileName);


		Calendar cal = Calendar.getInstance(); 
	    cal.add(Calendar.HOUR_OF_DAY, 1); // expire after one hour
	    cal.getTime();
	    qstatus.setExpireDate(cal.getTime());
	    

		
		if (getMapQueue().containsKey(id)) {
			logger.warn("MapQueue contained a duplicate key!! - very small odds!");
		}
		getMapQueue().put(id, qstatus);
	
		rabbitTemplate.convertAndSend(queue, request);
		
	}
	
	
	/**
	 * Lookup job id - will return null if not found.
	 * 
	 * @param id
	 * @return status or null
	 */
	public QueueStatus lookupJob(final String id) {
		QueueStatus qstatus = getMapQueue().get(id );		
		
		if (qstatus == null) {
			logger.error("No status found for job id: {}", id);
		} else {
			Date now = new Date();
			if (qstatus.getExpireDate().after(now)) {
				
				qstatus.setStatus("expired");
				
			}
		}
				
		return qstatus;
	}

	/**
	 * Look up all (non-expired) jobs.
	 * 
	 * @return list of job status
	 */
	public List<QueueStatus> lookupAllJobs( ) {
		
		List<QueueStatus> list = new ArrayList<QueueStatus>(getMapQueue().values());	
	
		
		for (Iterator<QueueStatus> it = list.iterator(); it.hasNext(); ) {
			QueueStatus qs = it.next();
			Date now = new Date();
			if (now.after(qs.getExpireDate())) {
				
				logger.info("Removing expired ID: {}", qs.getId());
				it.remove();
				
			}
		}

		return list;
	}
	
}
