package com.livechat.chat.utils;

public abstract class SpringLooper {

	protected BaseSpringSystem mSpringSystem;

	public void setSpringSystem(BaseSpringSystem springSystem) {
		mSpringSystem = springSystem;
	}

	public abstract void start();

	public abstract void stop();
	
}