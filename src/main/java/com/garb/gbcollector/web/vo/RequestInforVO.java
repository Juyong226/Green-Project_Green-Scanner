package com.garb.gbcollector.web.vo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestInforVO {

	private String ip;
	private String requestURI;
	private String id;
	private String infor;
	
	public RequestInforVO(String ip, String requestURI, String id) {
		super();
		setIp(ip);
		setRequestURI(requestURI);
		setId(id);
		setInfor(inforToStr(ip, requestURI, id));
	}
	
	public RequestInforVO(HttpServletRequest request) {
		super();
		setIp(request.getRemoteHost());
		setRequestURI(request.getMethod() + " " + request.getRequestURI());
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			MemberVO member = (MemberVO) session.getAttribute("member");
			if(member != null) {
				setId((String) session.getAttribute("email"));
			}
			else {
				setId("NOT_logined");
			}
		}
		else {
			setId("NOT_logined");
		}
		setInfor(inforToStr(ip, requestURI, id));
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfor() {
		return infor;
	}
	public void setInfor(String infor) {
		this.infor = infor;
	}

	private String inforToStr(String ip, String requestURI, String id) {
		String format = "[%1$s][%2$s][%3$s] ";
		String infor = String.format(format, ip, requestURI, id);
		return infor;
	}
		
}
