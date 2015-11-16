package com.maybe.mh.pojo;

public class User {

	private String group_id;
	private String username;
	private String pwd;


	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public User(String group_id, String username, String pwd) {
		super();
		this.group_id = group_id;
		this.username = username;
		this.pwd = pwd;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [group_id=" + group_id + ", username=" + username + ", pwd=" + pwd + "]";
	}

}
