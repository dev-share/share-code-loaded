package com.ucloudlink.css.config;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
@Component
public class ComponentTest {
	public JSONObject test(){
		JSONObject json = new JSONObject();
		json.put("id", new Random().nextInt(100));
		json.put("timestamp", new Date());
		return json;
	}
	public JSONObject test1(){
		JSONObject json = new JSONObject();
		json.put("status", new Random().nextInt(100));
		json.put("datetime", new Date());
		return json;
	}
}
