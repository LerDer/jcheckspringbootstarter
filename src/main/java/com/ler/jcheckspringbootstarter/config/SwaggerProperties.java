package com.ler.jcheckspringbootstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lww
 */
@ConfigurationProperties("ler.swagger")
public class SwaggerProperties {

	private String controllerPackageName;

	private String title;

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getControllerPackageName() {
		return controllerPackageName;
	}

	public void setControllerPackageName(String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
