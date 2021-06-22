package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.TrashCanDAO;
import com.garb.gbcollector.web.vo.TrashCanVO;

@Service
public class TrashCanService {
	
	@Autowired(required=true)
	TrashCanDAO trashCanDAO;

	public List<TrashCanVO> showTrashCan() {		
		return trashCanDAO.showTrashCan();
	}

}
