/*
 * Copyright (c) 2017, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.sdk.util.threading;

import com.apptentive.android.sdk.ApptentiveLog;

/**
 * A basic class for any dispatch runnable task. Tracks its "schedule" state
 */
public abstract class DispatchTask implements Runnable {

	/**
	 * True if task is already on the queue and would be executed soon.
	 */
	private boolean scheduled;

	/**
	 * Task entry point method
	 */
	protected abstract void execute();

	@Override
	public void run() {
		try {
			setScheduled(false);
			execute();
		} catch (Exception e) {
			ApptentiveLog.e(e, "Exception while executing task");
		}
	}

	synchronized void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	synchronized boolean isScheduled() {
		return scheduled;
	}
}