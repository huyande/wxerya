package com.erya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WxUserActiveController {

	@RequestMapping("activeView")
	public String activeView(){
		return "/index";
	}
}
