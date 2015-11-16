package com.maybe.mh.pojo;

public class DoWork {

	private int feedback_id;
	private int category_id;
	private int article_id;
	private String title;
	private String name;
	private String tel;
	private String files;
	private String contents;
	private long time;

	public DoWork() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DoWork(int feedback_id, int category_id, int article_id, String title, String name, String tel, String files, String contents, long time) {
		super();
		this.feedback_id = feedback_id;
		this.category_id = category_id;
		this.article_id = article_id;
		this.title = title;
		this.name = name;
		this.tel = tel;
		this.files = files;
		this.contents = contents;
		this.time = time;
	}

	public int getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(int feedback_id) {
		this.feedback_id = feedback_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
