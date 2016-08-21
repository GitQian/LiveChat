package com.livechat.chat.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BaseSpringSystem {

	private final Map<String, Spring> mSpringRegistry = new HashMap<>();
	private final Set<Spring> mActiveSprings = new CopyOnWriteArraySet<>();
	private final SpringLooper mSpringLooper;
	private final CopyOnWriteArraySet<SpringSystemListener> mListeners = new CopyOnWriteArraySet<>();
	private boolean mIdle = true;

	public BaseSpringSystem(SpringLooper springLooper) {
		if (springLooper == null) {
			throw new IllegalArgumentException("springLooper is required");
		}
		mSpringLooper = springLooper;
		mSpringLooper.setSpringSystem(this);
	}

	public boolean getIsIdle() {
		return mIdle;
	}

	public Spring createSpring() {
		Spring spring = new Spring(this);
		registerSpring(spring);
		return spring;
	}

	/*public Spring getSpringById(String id) {
		if (id == null) {
			throw new IllegalArgumentException("id is required");
		}
		return mSpringRegistry.get(id);
	}

	public List<Spring> getAllSprings() {
		Collection<Spring> collection = mSpringRegistry.values();
		List<Spring> list;
		if (collection instanceof List) {
			list = (List<Spring>) collection;
		} else {
			list = new ArrayList<>(collection);
		}
		return Collections.unmodifiableList(list);
	}*/

	void registerSpring(Spring spring) {
		if (spring == null) {
			throw new IllegalArgumentException("spring is required");
		}
		if (mSpringRegistry.containsKey(spring.getId())) {
			throw new IllegalArgumentException("spring is already registered");
		}
		mSpringRegistry.put(spring.getId(), spring);
	}

	void deregisterSpring(Spring spring) {
		if (spring == null) {
			throw new IllegalArgumentException("spring is required");
		}
		mActiveSprings.remove(spring);
		mSpringRegistry.remove(spring.getId());
	}

	void advance(double deltaTime) {
		for (Spring spring : mActiveSprings) {
			if (spring.systemShouldAdvance()) {
				spring.advance(deltaTime / 1000.0);
			} else {
				mActiveSprings.remove(spring);
			}
		}
	}

	public void loop(double ellapsedMillis) {
		for (SpringSystemListener listener : mListeners) {
			listener.onBeforeIntegrate(this);
		}
		advance(ellapsedMillis);
		if (mActiveSprings.isEmpty()) {
			mIdle = true;
		}
		for (SpringSystemListener listener : mListeners) {
			listener.onAfterIntegrate(this);
		}
		if (mIdle) {
			mSpringLooper.stop();
		}
	}

	void activateSpring(String springId) {
		Spring spring = mSpringRegistry.get(springId);
		if (spring == null) {
			throw new IllegalArgumentException("springId " + springId + " does not reference a registered spring");
		}
		mActiveSprings.add(spring);
		if (getIsIdle()) {
			mIdle = false;
			mSpringLooper.start();
		}
	}

	public void addListener(SpringSystemListener newListener) {
		if (newListener == null) {
			throw new IllegalArgumentException("newListener is required");
		}
		mListeners.add(newListener);
	}

	public void removeListener(SpringSystemListener listenerToRemove) {
		if (listenerToRemove == null) {
			throw new IllegalArgumentException("listenerToRemove is required");
		}
		mListeners.remove(listenerToRemove);
	}

	public void removeAllListeners() {
		mListeners.clear();
	}
	
}
