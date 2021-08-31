package com.garb.gbcollector.util;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.garb.gbcollector.constant.Method;

@Controller
public class UiUtils {
	
	public String showMessageWithRedirection(
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "redirectURI", required = false) String redirectURI,
			@RequestParam(value = "method", required = false) Method method,
			@RequestParam(value = "params", required = false) Map<String, Object> params, Model model) {
		
		model.addAttribute("message", message);
		model.addAttribute("redirectURI", redirectURI);
		model.addAttribute("method", method);
		model.addAttribute("params", params);
		
		return "utils/message-redirect";
	}
}
