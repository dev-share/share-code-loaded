package com.ucloudlink.css.controller.pojo;

public class Test {
	private int id;
	private String name;
	private long total;
	public Test() {
		super();
	}
	public Test(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Test(int id, String name, long total) {
		super();
		this.id = id;
		this.name = name;
		this.total = total;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
