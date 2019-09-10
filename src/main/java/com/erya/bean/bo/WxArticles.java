package com.erya.bean.bo;

public class WxArticles {

	//标题
	private String title;
	//封面素材id
	private String thumb_media_id;
	//作者
	private String author;
	//摘要
	private String digest;
	//是否显示封面	
	private int show_cover_pic;
	//文章内容
	private String content;
	//图文消息的原文地址
	private String content_source_url;
	//是否打开评论
	private int need_open_comment;
	//是否只有粉丝可以评论
	private int only_fans_can_comment;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumb_media_id() {
		return thumb_media_id;
	}
	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public int getShow_cover_pic() {
		return show_cover_pic;
	}
	public void setShow_cover_pic(int show_cover_pic) {
		this.show_cover_pic = show_cover_pic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent_source_url() {
		return content_source_url;
	}
	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}
	public int getNeed_open_comment() {
		return need_open_comment;
	}
	public void setNeed_open_comment(int need_open_comment) {
		this.need_open_comment = need_open_comment;
	}
	public int getOnly_fans_can_comment() {
		return only_fans_can_comment;
	}
	public void setOnly_fans_can_comment(int only_fans_can_comment) {
		this.only_fans_can_comment = only_fans_can_comment;
	}
}
