package com.aem.SampleProject.core.models;

public class Model {
	private String title;
	private String path;

	public Model() {

	}

	public Model(String title, String path) {

		this.path = path;
		this.title = title;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
