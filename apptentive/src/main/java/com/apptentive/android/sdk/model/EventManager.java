/*
 * Copyright (c) 2014, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.sdk.model;

import com.apptentive.android.sdk.ApptentiveInternal;
import com.apptentive.android.sdk.storage.EventStore;

/**
 * @author Sky Kelsey
 */
public class EventManager {

	private static EventStore getEventStore() {
		return ApptentiveInternal.getInstance().getApptentiveTaskManager();
	}

	public static void sendEvent(Event event) {
		getEventStore().addPayload(event);
	}
}
