package com.raghu.newsvolley;

/** Data Model class for News.
 *  Contains setters and getters*/
public class NewsModel {
	
	/** JSON Key = headLine */
	private String title;
	
	/** JSON Key = thumbnailImageHref */
	private String link;
	
	/** JSON Key = slugLine */
	private String description;
	
	/** JSON Key = dateLine */
	private String pubDate;
	
	/** JSON Key = tinyUrl */
	private String tinyURL;

	void setTitle(String title) {
		this.title = title;
	}

	void setLink(String link) {
		this.link = link;
	}

	void setDescription(String description) {
		this.description = description;
	}

	void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	String getLink() {
		return link;
	}

	String getDescription() {
		return description;
	}

	String getPubDate() {
		return pubDate;
	}

	String getTitle() {

		return title;
	}

	public String getTinyURL() {
		return tinyURL;
	}

	public void setTinyURL(String tinyURL) {
		this.tinyURL = tinyURL;
	}
}
