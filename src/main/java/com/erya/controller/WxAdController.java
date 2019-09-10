package com.erya.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wxad")
public class WxAdController {
	@Value("${wx.ad.videoShow}")
	private boolean wxVideoAdShow;

	@RequestMapping("/video")
	public String wxVideoAd(){
		return "{\"data\":"+wxVideoAdShow+"}";
	}
}
