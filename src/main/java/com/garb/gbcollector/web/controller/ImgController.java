package com.garb.gbcollector.web.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImgController {
	
	@PostMapping("/uploadImg")
	// MultipartFile ->> text타입 이외의 다른 유형(여기선 이미지)파일을 후처리 없이 받을 수 있음
	// RequestParam("")을 앞에 붙이면, http request 객체의 Parameter에 담겨 온 데이터를 매개변수(여기선 MultipartFile file)에 담아 줌.
	public String uploadImg(@RequestParam("file") MultipartFile file) { 
		System.out.println(file);								   
		
		try {
			file.transferTo(new File("d:\\"+file.getOriginalFilename()));
			return "업로드 되었습니다.";
		} catch (IllegalStateException e) {			
			e.printStackTrace();
			return "업로드를 실패 하였습니다.";
		} catch (IOException e) {			
			e.printStackTrace();
			return "업로드를 실패 하였습니다.";
		}
	}
}
