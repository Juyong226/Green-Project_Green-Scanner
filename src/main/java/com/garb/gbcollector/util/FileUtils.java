package com.garb.gbcollector.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.garb.gbcollector.web.vo.UploadImageVO;
import com.garb.gbcollector.util.Log;

@Component
public class FileUtils {
	
	private final String toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	private final String feedImgUploadPath = Paths.get("C:", "GreenScanner", "upload", "challenge", toDay).toString();
	private Log log = new Log();
	/*
	 * 서버에 생성(저장)할 파일의 이름으로 쓸 랜덤 문자열 반환
	 * @return 랜덤 문자열
	 * */
	public final String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/*
	 * 파일의 확장자가 이미지인지 아닌지 검사하여 boolean값을 반환
	 * @param extention: 파일의 확장자
	 * @return boolean
	 * */
	public final boolean isValidImage(String extention) {
		final String[] validExts = {"jpeg", "jpg", "png"};
		int flag = 0;
		for(int i=0; i<validExts.length; i++) {
			if(extention.equalsIgnoreCase(validExts[i])) {
				flag = 1;
			}
		}
		return (flag == 1);
	}
	
	/*
	 * 서버에 이미지을 생성(저장)하고, 업로드 파일 목록을 반환
	 * @param files: 이미지 Array
	 * @param feedNo: 피드 번호
	 * @return 업로드 파일 목록
	 * */
	public List<UploadImageVO> uploadFeedImages(MultipartFile[] images, Integer feedNo) throws UploadFileException {
		/* 업로드 파일 정보를 담을 비어있는 리스트 */
		List<UploadImageVO> uploadList = new ArrayList<>();
		
		/* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
		File dir = new File(feedImgUploadPath);
		if(dir.exists() == false) {
			dir.mkdirs();
		}
		
		/* 파일 개수만큼 forEach 실행 */
		for(MultipartFile image : images) {
			try {
				log.TraceLog("======================================");
				log.TraceLog("0.\n size: " + image.getSize() + "\n empty여부: " + image.isEmpty() + "\n filename: " + image.getOriginalFilename() + "\n toString: " + image.toString());
				log.TraceLog("======================================");
				if(image.isEmpty() == false) {
					/* 원본파일에서 확장자 얻기 */
					final String extention = FilenameUtils.getExtension(image.getOriginalFilename());
					/* 파일 확장자 유효성 검사 */
					if(isValidImage(extention)) {
						/* 서버에 저장할 파일명 만들기 (랜덤 문자열 + 원본 확장자) */
						final String saveName = getRandomString() + "." + extention;
						/* 업로드 경로에 saveName으로 파일 생성*/
						File target = new File(feedImgUploadPath, saveName);
						image.transferTo(target);
						
						/* 파일 정보 저장 */
						UploadImageVO upload = new UploadImageVO();
						upload.setFeedNo(feedNo);
						upload.setOriginalName(image.getOriginalFilename());
						upload.setSaveName(saveName);
						upload.setImgSize(image.getSize());
						
						/* 파일 정보 리스트에 추가 */
						uploadList.add(upload);
					} else {
						/* 파일이 이미지가 아닌 경우 UploadFileException 발생 */
						throw new UploadFileException("[" + image.getOriginalFilename() + "] failed to save this file. (invalid file type)");
					}
				}	
				
			} catch (IOException e) {
				throw new UploadFileException("[" + image.getOriginalFilename() + "] failed to save this file.");
			} catch (Exception e) {
				e.printStackTrace();
				throw new UploadFileException("[" + image.getOriginalFilename() + "] failed to save this file.");
			}
		} // end for()
		return uploadList;
	}
	
	public File getImagePath(UploadImageVO image) {
		LocalDate insertTime = LocalDate.ofInstant(image.getInsertTime().toInstant(), ZoneId.systemDefault());
		String feedImgDir = Paths.get("C:", "GreenScanner", "upload", "challenge", insertTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))).toString();
		File target = new File(feedImgDir, image.getSaveName());
		log.TraceLog("======================================");
		log.TraceLog("feedImgDir: " + feedImgDir + "\ntargetFile: " + target.toString());
		log.TraceLog("======================================");
		return target;
	}
}
