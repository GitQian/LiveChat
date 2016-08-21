package com.livechat.chat.utils;

public interface SpringListener {

	void onSpringUpdate(Spring spring);

	void onSpringAtRest(Spring spring);

	void onSpringActivate(Spring spring);

	void onSpringEndStateChange(Spring spring);
	
}
