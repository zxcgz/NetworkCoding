package com.zxc.network_coding.bean;

public class Ip {
	private String ip ;
	private int port ;
	public Ip(){}
	public Ip(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean equals(Ip ip) {
		return ip.getIp().equals(this.ip)&&ip.getPort() == this.port;
	}
	
}
