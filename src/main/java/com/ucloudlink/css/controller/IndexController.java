package com.ucloudlink.css.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ucloudlink.css.config.BeanTest;
import com.ucloudlink.css.config.ComponentTest;
import com.ucloudlink.css.controller.pojo.ATest;
import com.ucloudlink.css.controller.pojo.Result;
import com.ucloudlink.css.controller.pojo.Test;
import com.ucloudlink.css.controller.pojo.TestA;

@RestController
@RequestMapping("/index")
@EnableAutoConfiguration
public class IndexController {
//	@Autowired
//	private MetricRegistry regist;
	@Autowired
	private BeanTest beanTest;
	@Autowired
	private ComponentTest componentTest;
	@RequestMapping("/")
	public ResponseEntity<Object> r(){
		return new ResponseEntity<Object>(new Result.Success(), HttpStatus.OK);
	}
	@RequestMapping(value="/r1",method={RequestMethod.GET})
	public ResponseEntity<Object> r1(){
		
		return new ResponseEntity<Object>(new Result.Success(), HttpStatus.OK);
	}
	@RequestMapping(value="/r2",method={RequestMethod.GET})
	public ResponseEntity<Object> r2(int id,String name){
//		final Timer jobtimer = regist.timer("Controller-Metric-ExecuteTime");
//		final Timer.Context context = jobtimer.time();
		JSONObject json = new JSONObject();
		json.put("id_1", id);
		json.put("name_2", name);
		Test test = new Test(25, "aaa",30);
		json.put("test", test);
		json.put("Total", test.getTotal());
		ATest at= new ATest();
		at.setId(20);
		at.setName("ffff");
		json.put("at", at);
		json.put("child", true);
		json.put("beanTest1", beanTest.test1());
		json.put("over", beanTest.toString());
		json.put("componentTest1", componentTest.test1());
		json.put("info", info());
		TestA testa = new TestA();
		testa.setId(205);
		json.put("testa", testa);
		try {
			return new ResponseEntity<Object>(new Result.Success(json), HttpStatus.OK);
		}finally{
//			context.stop();
		}
	}
	public String info(){
		String info="-----1111success--------";
		return info;
	}
	@RequestMapping(value="/r3",method={RequestMethod.GET})
	public ResponseEntity<Object> r3(@RequestBody(required=false) JSONObject json){
		return new ResponseEntity<Object>(new Result.Success(json), HttpStatus.OK);
	}
	@RequestMapping(value="/r4",method={RequestMethod.POST})
	public ResponseEntity<Object> r4(@ModelAttribute JSONObject json){
		return new ResponseEntity<Object>(new Result.Success(json), HttpStatus.OK);
	}
//	@RequestMapping(value="/r5",method={RequestMethod.GET})
//	public ResponseEntity<Object> r5(int id,String name,String task){
//		JSONObject json = new JSONObject();
//		json.put("id", id);
//		json.put("name", name);
//		json.put("task", task);
//		Test test = new Test(11+"");
////		test.setTotal(20);
//		json.put("test", test);
//		ATest at= new ATest();
//		at.setId(202);
//		json.put("at", at);
//		return new ResponseEntity<Object>(new Result.Success(json), HttpStatus.OK);
//	}
}
