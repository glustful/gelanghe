package com.maybe.mh.pojo;

public class LoginResponse {

	private boolean success;

	private User user;

	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginResponse(boolean success, User user) {
		super();
		this.success = success;
		this.user = user;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "LoginResponse [success=" + success + ", user=" + user + "]";
	}

}
