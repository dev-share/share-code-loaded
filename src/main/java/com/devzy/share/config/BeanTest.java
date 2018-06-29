package com.devzy.share.config;

import java.util.Date;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
public class BeanTest {
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public JSONObject test(){
		JSONObject json = new JSONObject();
		json.put("id", new Random().nextInt(100));
		json.put("date", new Date());
		return json;
	}
	public JSONObject test1(){
		JSONObject json = new JSONObject();
		json.put("status", new Random().nextInt(100));
		json.put("datetime", new Date());
		json.put("timestamp", new Date().getTime());
		return json;
	}
	@Override
	public String toString(){
		return "11111";
	}
}
