package com.tilofy.queue;

import java.util.Date;

/**
 * 
 * Simple bean to encapsulate quest status data.
 * 
 * @author Stu2
 *
 */
public class QueueStatus {
	
	
	private String id;
	private String status;
	private String url;
	private String image;
	private String imageName;

	private Date expireDate;
	
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getId() {
		return id;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueueStatus [id=");
		builder.append(id);
		builder.append(", status=");
		builder.append(status);
		builder.append(", url=");
		builder.append(url);
		builder.append(", image=");
		builder.append(image);
		builder.append(", imageName=");
		builder.append(imageName);
		builder.append(", expireDate=");
		builder.append(expireDate);
		builder.append("]");
		return builder.toString();
	}

}
