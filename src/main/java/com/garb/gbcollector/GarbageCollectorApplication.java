package com.garb.gbcollector;

import java.util.Locale;
import java.util.TimeZone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.garb.gbcollector.web.dao")
public class GarbageCollectorApplication {

	public static void main(String[] args) {
		TimeZone tz = TimeZone.getDefault();
		System.out.println("TimeZone.getDefault(): " + tz);
		System.out.println("TimeZone.getID(): " + tz.getID());
		SpringApplication.run(GarbageCollectorApplication.class, args);
	}

}
