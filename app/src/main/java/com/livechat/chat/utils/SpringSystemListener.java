package com.livechat.chat.utils;

public interface SpringSystemListener {

	void onBeforeIntegrate(BaseSpringSystem springSystem);

	void onAfterIntegrate(BaseSpringSystem springSystem);
	
}
