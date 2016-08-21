package com.livechat.chat.utils;

public class SpringSystem extends BaseSpringSystem {

	public static SpringSystem create() {
		return new SpringSystem(AndroidSpringLooperFactory.createSpringLooper());
	}

	private SpringSystem(SpringLooper springLooper) {
		super(springLooper);
	}

}