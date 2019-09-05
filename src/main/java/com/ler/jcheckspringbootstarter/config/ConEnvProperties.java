package com.ler.jcheckspringbootstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lww
 * @date 2019-09-05 00:37
 */
//在其他项目引入该Starter，在properties文件中配置的时候的前缀，如 ler.con.env=local
@ConfigurationProperties("ler.con")
public class ConEnvProperties {

	//默认值
	private String env = "local";

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public Boolean islocalOrDaily() {
		return "local".equalsIgnoreCase(this.getEnv()) || "daily".equalsIgnoreCase(this.getEnv());
	}

	public Boolean isGray() {
		return "gray".equalsIgnoreCase(this.getEnv());
	}

	public Boolean isOnline() {
		return "online".equalsIgnoreCase(this.getEnv());
	}

}
