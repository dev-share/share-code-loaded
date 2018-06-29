package com.devzy.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		if(args!=null&&args.length>0){
			for(int i=0;i<args.length;i++){
				String arg = args[i];
				if(!StringUtils.isEmpty(arg)&&arg.contains("=")&&arg.startsWith("base")){
					String base_path = arg.substring(arg.indexOf("=")+1);
					System.setProperty("log.path", base_path);
				}
			}
		}
		SpringApplication.run(Application.class, args);
	}
}