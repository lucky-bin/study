package com.example.study.cache.java;

import java.io.Serializable;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7710153170636593560L;
	private String userName;
    private String userId;

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

	@Override
	public String toString() {
		return "User [userName=" + userName + ", userId=" + userId + "]";
	}

    
}
