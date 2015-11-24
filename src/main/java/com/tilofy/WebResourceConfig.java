package com.tilofy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 
 *  Used to specify the resized image local file location as a web resource (so we can access it via web app).
 * 
 * 
 * @author Stu2
 *
 */
@Configuration
@ComponentScan
public class WebResourceConfig extends WebMvcConfigurerAdapter {

	@Value("${resized.images.dir}")
	private String imageDir;
	
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry
	      .addResourceHandler("/resized/**")
	      .addResourceLocations("file:///" + imageDir +"/");
	 }

}