package com.tilofy;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tilofy.queue.PhotoQueueController;
import com.tilofy.queue.QueueStatus;

@SpringApplicationConfiguration(classes =TilofyApplication.class)
@WebAppConfiguration
@RunWith( SpringJUnit4ClassRunner.class )
public class ApplicationTests {
		
	
	static private final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
	
	final static String[] IMAGES = new String[] { "https://scontent.cdninstagram.com/hphotos-xaf1/t51.2885-15/s640x640/sh0.08/e35/12301404_1689744377929780_572387996_n.jpg",
			"https://scontent.cdninstagram.com/hphotos-xpf1/t51.2885-15/s640x640/sh0.08/e35/12256756_482190938635495_432783086_n.jpg",
			"https://scontent.cdninstagram.com/hphotos-xpa1/t51.2885-15/s640x640/sh0.08/e35/12256894_720156524782163_1349930470_n.jpg",
			"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-15/s640x640/sh0.08/e35/12292667_1027337003953562_997590195_n.jpg",
			"https://scontent.cdninstagram.com/hphotos-xfp1/t51.2885-15/s640x640/sh0.08/e35/12256802_1642435692672023_1269221380_n.jpg", "http://www.tilofy.com/assets/contact-60b0d1955352499dc4e6003b39a656e5.jpg" };
	
	// https://scontent.cdninstagram.com/hphotos-xpa1/t51.2885-15/s640x640/sh0.08/e35/12256894_720156524782163_1349930470_n.jpg

	@Value("${rabbitmq.image.queue}")
	private String queue;
	
	@Autowired
    public RabbitTemplate rabbitTemplate;
	
	@Autowired
    public 	PhotoQueueController photoControl;
    
	
	/**
	 * Test that a photo can be posted to queue and then the same job id lookedup.
	 */
	@Test
	public void testPhotoControlPost() {		
			
		long start = System.nanoTime();	
		try {
			
			//
			// Call a pist with new image and size....
			//
			
			String size ="800x600";			
			Map<String, Object>  response = photoControl.queue("http://www.tilofy.com/assets/contact-60b0d1955352499dc4e6003b39a656e5.jpg", size);
			
			Boolean success = (Boolean) response.get("success");			
			assertEquals(success, true);
						
        	logger.info("response = {}, ", response);

        	//
        	// ..now get the ID to use to test lookup....
        	//
        	String id = (String)response.get("id");
        	
        	logger.info("id = {}, ", id);
        	
        	logger.info("Waiting....");

        	Thread.sleep(2000);
        	
        	
        	Map<String, Object>  readResponse= photoControl.readQueue(id);
        	
        	QueueStatus qstatus = (QueueStatus)readResponse.get("result");
        	
        	assertNotNull("Q status was not null!", qstatus) ;
        	
        	
        	//
        	// Now try bad id...
        	//
        	readResponse= photoControl.readQueue("badid");
         	
         	qstatus = (QueueStatus)readResponse.get("result");
         	
         	assertNull("Expected a bad status but got a good one!", qstatus) ;
        	
        	logger.info("qstatus = {}, ", readResponse);


		} catch (Exception e) {
			logger.error("oops!",e);
			fail("caught exception!" + e.getMessage());
			
		}

		long took = System.nanoTime() - start;		
		logger.info("Time taken: {} ms, {} secs",took, TimeUnit.SECONDS.convert(took, TimeUnit.NANOSECONDS ) );
		
	}
	
	
	
	
	/**
	 * 
	 * Test the receiver component.
	 */
	@Test
	public void testRabbitReceiver() {		
			
		long start = System.nanoTime();	
		try {
			
			Map<String, Object>  request = new HashMap<String,Object>();

			String id = UUID.randomUUID().toString();
			
			request.put("id", "id");
			request.put("size", "800x600");
			request.put("url", "http://www.tilofy.com/assets/contact-60b0d1955352499dc4e6003b39a656e5.jpg");        	
        	
				
			rabbitTemplate.convertAndSend( queue , request);
			
        	logger.info("request = {}, queue:{}  ", request, queue);


		} catch (Exception e) {
			logger.error("oops!",e);
			fail("caught exception!" + e.getMessage());

		}

		long took = System.nanoTime() - start;		
		logger.info("Time taken: {} ms, {} secs",took, TimeUnit.SECONDS.convert(took, TimeUnit.NANOSECONDS ) );
		
	}
	

	
	
	

	
	@Test
	public void testPhotoControlPostAndGet() {		
			
		long start = System.nanoTime();	
		try {
			
			String size ="800x600";
			
			 for (String image : IMAGES) {
				logger.info("Testing url image:  {}, ", image);

				Map<String, Object>  response = photoControl.queue(image, size);
			
				logger.info("response: {}, ", response);
 
				 
			 }
		
			 logger.info("Read all jobs....");

			 Map<String, Object>  readResponse= photoControl.readQueue(null);
			 
			 List<QueueStatus> list = (List<QueueStatus>) readResponse.get("result");
			 
			 if (list == null || list.isEmpty()) {
			        logger.info("List sis empty!");

			 }else {
				 for (QueueStatus qs : list) {
				        logger.info("getId: {}, ", qs.getId());
				        logger.info("getStatus: {}", qs.getStatus());
				        logger.info("getImage: {}, ", qs.getImage());

				 }
			 }
			
			 
	        logger.info("readResponse = {}, ", readResponse);

		} catch (Exception e) {
			logger.error("oops!",e);
		}

		long took = System.nanoTime() - start;		
		logger.info("Time taken: {} ms, {} secs",took, TimeUnit.SECONDS.convert(took, TimeUnit.NANOSECONDS ) );
		
	}
	
	@Test
	public void testNonRepeat() {		
        String str ="stress";

		  HashMap<Character,Integer>  characterhashtable= 
                  new HashMap<Character ,Integer>();
     int i,length ;
     Character c ;
     length= str.length();  // Scan string and build hash table
     for (i=0;i < length;i++)
     {
         c=str.charAt(i);
         if(characterhashtable.containsKey(c))
         {
             // increment count corresponding to c
             characterhashtable.put(  c ,  characterhashtable.get(c) +1 );
         }
         else
         {
             characterhashtable.put( c , 1 ) ;
         }
     }
     // Search characterhashtable in in order of string str
     
     for (i =0 ; i < length ; i++ )
     {
         c= str.charAt(i);
         if( characterhashtable.get(c)  == 1 )
        	 logger.info("C ===: {}",c);
     }
		
		   
		 
	
	}
	
	public static boolean containsSum(int[] a, int sum){
        HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

        for (int i= 0; i< a.length; i++) {
            map.put(sum - a[i], true);
        }

        for (int i = 0; i < a.length; i++) {
            if (map.containsKey(a[i]) && map.get(a[i])) {
                System.out.println("("+(sum-a[i])+","+a[i]+")");
                return true;

            }
        }

        return false;
    }
	@Test
	public void testSum() {		
		int[] test1 = {1,3,-10,4,2};

		logger.info("Contains 6 in {} , result: {}", test1,containsSum(test1, 6));
		logger.info("Contains 21 in {} , result: {}", test1,containsSum(test1, 21));
		int[] test2 = {-10,1,2,3,4};

		logger.info("Contains 6 in {} , result: {}", test2,containsSum(test2, 6));
		logger.info("Contains 21 in {} , result: {}", test2,containsSum(test2, 21));

	}
	
}
	
	
