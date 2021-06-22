package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.ZeroWasteShopDAO;
import com.garb.gbcollector.web.vo.ZeroWasteShopVO;

@Service
public class ZeroWasteShopService {
	
	@Autowired(required=true)
	ZeroWasteShopDAO zeroWasteShopDAO;

	public List<ZeroWasteShopVO> showZeroWasteShop() {		
		return zeroWasteShopDAO.showZeroWasteShop();
	}
}
