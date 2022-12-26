package com.digital4.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digital4.utils.HttpConnectionUtils;

@Component
public class InventoryProductContext {
	//public boolean subtractQuantity//여기서 httpUrlConnection 호출
	public String insertInventory(long productId, String token) throws Exception {
		String targetUrl = "APIG";
		String requestUrl = "/rest/inventory/insertInventory";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		String response = HttpConnectionUtils.postRequest(targetUrl, requestUrl, map,token);
		System.out.println("postRequest:" + response);
		return response;
	}
}
