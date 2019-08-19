package com.erya.bean.bo;

import java.io.Serializable;

public class WxUserSearch implements Serializable{
	private String openid;

	private Integer searchCount;

	private boolean clickLink;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

	public boolean getClickLink() {
		return clickLink;
	}

	public void setClickLink(boolean clickLink) {
		this.clickLink = clickLink;
	}

	@Override
	public String toString() {
		return "WxUserSearch{" +
				"openid='" + openid + '\'' +
				", searchCount=" + searchCount +
				", clickLink=" + clickLink +
				'}';
	}
}
