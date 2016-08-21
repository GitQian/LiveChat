package com.livechat.chat.utils;

import java.util.concurrent.CopyOnWriteArraySet;

public class Spring {

	private static int ID = 0;
	private static final double MAX_DELTA_TIME_SEC = 0.064;
	private static final double SOLVER_TIMESTEP_SEC = 0.001;
	private SpringConfig mSpringConfig;
	private boolean mOvershootClampingEnabled;

	private static class PhysicsState {
		double position;
		double velocity;
	}

	private final String mId;
	private final PhysicsState mCurrentState = new PhysicsState();
	private final PhysicsState mPreviousState = new PhysicsState();
	private final PhysicsState mTempState = new PhysicsState();
	private double mStartValue;
	private double mEndValue;
	private boolean mWasAtRest = true;
	private double mRestSpeedThreshold = 0.005;
	private double mDisplacementFromRestThreshold = 0.005;
	private CopyOnWriteArraySet<SpringListener> mListeners = new CopyOnWriteArraySet<SpringListener>();
	private double mTimeAccumulator = 0;

	private final BaseSpringSystem mSpringSystem;

	Spring(BaseSpringSystem springSystem) {
		if (springSystem == null) {
			throw new IllegalArgumentException("Spring cannot be created outside of a BaseSpringSystem");
		}
		mSpringSystem = springSystem;
		mId = "spring:" + ID++;
		setSpringConfig(SpringConfig.defaultConfig);
	}

	public void destroy() {
		mListeners.clear();
		mSpringSystem.deregisterSpring(this);
	}

	public String getId() {
		return mId;
	}

	public Spring setSpringConfig(SpringConfig springConfig) {
		if (springConfig == null) {
			throw new IllegalArgumentException("springConfig is required");
		}
		mSpringConfig = springConfig;
		return this;
	}

	public SpringConfig getSpringConfig() {
		return mSpringConfig;
	}

	public Spring setCurrentValue(double currentValue) {
		mStartValue = currentValue;
		mCurrentState.position = currentValue;
		mSpringSystem.activateSpring(this.getId());
		for (SpringListener listener : mListeners) {
			listener.onSpringUpdate(this);
		}
		return this;
	}

	public double getStartValue() {
		return mStartValue;
	}

	public double getCurrentValue() {
		return mCurrentState.position;
	}

	public double getCurrentDisplacementDistance() {
		return getDisplacementDistanceForState(mCurrentState);
	}

	private double getDisplacementDistanceForState(PhysicsState state) {
		return Math.abs(mEndValue - state.position);
	}

	public Spring setEndValue(double endValue) {
		if (mEndValue == endValue && isAtRest()) {
			return this;
		}
		mStartValue = getCurrentValue();
		mEndValue = endValue;
		mSpringSystem.activateSpring(this.getId());
		for (SpringListener listener : mListeners) {
			listener.onSpringEndStateChange(this);
		}
		return this;
	}

	public double getEndValue() {
		return mEndValue;
	}

	public Spring setVelocity(double velocity) {
		mCurrentState.velocity = velocity;
		mSpringSystem.activateSpring(this.getId());
		return this;
	}

	public double getVelocity() {
		return mCurrentState.velocity;
	}

	public Spring setRestSpeedThreshold(double restSpeedThreshold) {
		mRestSpeedThreshold = restSpeedThreshold;
		return this;
	}

	public double getRestSpeedThreshold() {
		return mRestSpeedThreshold;
	}

	public Spring setRestDisplacementThreshold(
			double displacementFromRestThreshold) {
		mDisplacementFromRestThreshold = displacementFromRestThreshold;
		return this;
	}

	public double getRestDisplacementThreshold() {
		return mDisplacementFromRestThreshold;
	}

	public Spring setOvershootClampingEnabled(boolean overshootClampingEnabled) {
		mOvershootClampingEnabled = overshootClampingEnabled;
		return this;
	}

	public boolean isOvershootClampingEnabled() {
		return mOvershootClampingEnabled;
	}

	public boolean isOvershooting() {
		return (mStartValue < mEndValue && getCurrentValue() > mEndValue)
				|| (mStartValue > mEndValue && getCurrentValue() < mEndValue);
	}

	void advance(double realDeltaTime) {

		boolean isAtRest = isAtRest();

		if (isAtRest && mWasAtRest) {
			return;
		}
		double adjustedDeltaTime = realDeltaTime;
		if (realDeltaTime > MAX_DELTA_TIME_SEC) {
			adjustedDeltaTime = MAX_DELTA_TIME_SEC;
		}
		mTimeAccumulator += adjustedDeltaTime;

		double tension = mSpringConfig.tension;
		double friction = mSpringConfig.friction;

		double position = mCurrentState.position;
		double velocity = mCurrentState.velocity;
		double tempPosition = mTempState.position;
		double tempVelocity = mTempState.velocity;

		double aVelocity, aAcceleration;
		double bVelocity, bAcceleration;
		double cVelocity, cAcceleration;
		double dVelocity, dAcceleration;

		double dxdt, dvdt;

		while (mTimeAccumulator >= SOLVER_TIMESTEP_SEC) {
			mTimeAccumulator -= SOLVER_TIMESTEP_SEC;
			if (mTimeAccumulator < SOLVER_TIMESTEP_SEC) {
				mPreviousState.position = position;
				mPreviousState.velocity = velocity;
			}
			aVelocity = velocity;
			aAcceleration = (tension * (mEndValue - tempPosition)) - friction
					* velocity;
			tempPosition = position + aVelocity * SOLVER_TIMESTEP_SEC * 0.5;
			tempVelocity = velocity + aAcceleration * SOLVER_TIMESTEP_SEC * 0.5;
			bVelocity = tempVelocity;
			bAcceleration = (tension * (mEndValue - tempPosition)) - friction
					* tempVelocity;

			tempPosition = position + bVelocity * SOLVER_TIMESTEP_SEC * 0.5;
			tempVelocity = velocity + bAcceleration * SOLVER_TIMESTEP_SEC * 0.5;
			cVelocity = tempVelocity;
			cAcceleration = (tension * (mEndValue - tempPosition)) - friction
					* tempVelocity;

			tempPosition = position + cVelocity * SOLVER_TIMESTEP_SEC;
			tempVelocity = velocity + cAcceleration * SOLVER_TIMESTEP_SEC;
			dVelocity = tempVelocity;
			dAcceleration = (tension * (mEndValue - tempPosition)) - friction
					* tempVelocity;
			dxdt = 1.0 / 6.0 * (aVelocity + 2.0 * (bVelocity + cVelocity) + dVelocity);
			dvdt = 1.0 / 6.0 * (aAcceleration + 2.0
					* (bAcceleration + cAcceleration) + dAcceleration);

			position += dxdt * SOLVER_TIMESTEP_SEC;
			velocity += dvdt * SOLVER_TIMESTEP_SEC;
		}

		mTempState.position = tempPosition;
		mTempState.velocity = tempVelocity;

		mCurrentState.position = position;
		mCurrentState.velocity = velocity;

		if (mTimeAccumulator > 0) {
			interpolate(mTimeAccumulator / SOLVER_TIMESTEP_SEC);
		}
		if (isAtRest() || (mOvershootClampingEnabled && isOvershooting())) {
			mStartValue = mEndValue;
			mCurrentState.position = mEndValue;
			setVelocity(0);
			isAtRest = true;
		}
		boolean notifyActivate = false;
		if (mWasAtRest) {
			mWasAtRest = false;
			notifyActivate = true;
		}
		boolean notifyAtRest = false;
		if (isAtRest) {
			mWasAtRest = true;
			notifyAtRest = true;
		}
		for (SpringListener listener : mListeners) {
			if (notifyActivate) {
				listener.onSpringActivate(this);
			}
			listener.onSpringUpdate(this);
			if (notifyAtRest) {
				listener.onSpringAtRest(this);
			}
		}
	}

	public boolean systemShouldAdvance() {
		return !isAtRest() || !wasAtRest();
	}

	public boolean wasAtRest() {
		return mWasAtRest;
	}

	public boolean isAtRest() {
		return Math.abs(mCurrentState.velocity) <= mRestSpeedThreshold
				&& getDisplacementDistanceForState(mCurrentState) <= mDisplacementFromRestThreshold;
	}

	public Spring setAtRest() {
		mEndValue = mCurrentState.position;
		mTempState.position = mCurrentState.position;
		mCurrentState.velocity = 0;
		return this;
	}

	private void interpolate(double alpha) {
		mCurrentState.position = mCurrentState.position * alpha
				+ mPreviousState.position * (1 - alpha);
		mCurrentState.velocity = mCurrentState.velocity * alpha
				+ mPreviousState.velocity * (1 - alpha);
	}

	public Spring addListener(SpringListener newListener) {
		if (newListener == null) {
			throw new IllegalArgumentException("newListener is required");
		}
		mListeners.add(newListener);
		return this;
	}

	public Spring removeListener(SpringListener listenerToRemove) {
		if (listenerToRemove == null) {
			throw new IllegalArgumentException("listenerToRemove is required");
		}
		mListeners.remove(listenerToRemove);
		return this;
	}

	public Spring removeAllListeners() {
		mListeners.clear();
		return this;
	}

	public boolean currentValueIsApproximately(double value) {
		return Math.abs(getCurrentValue() - value) <= getRestDisplacementThreshold();
	}

}
