/*
 * Copyright (c) 2016, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.sdk.tests.push;

import android.support.test.runner.AndroidJUnit4;

import com.apptentive.android.sdk.Apptentive;
import com.apptentive.android.sdk.tests.ApptentiveTestCaseBase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Fcm extends ApptentiveTestCaseBase {

	@Test
	public void pushDataApptentive() {
		Map<String, String> data = new HashMap<>();
		data.put("apptentive", "{\"action\":\"pmc\"}");
		data.put("title", "The title.");
		data.put("body", "The body.");
		assertNotNull(Apptentive.buildPendingIntentFromPushNotification(data));
		assertTrue(Apptentive.isApptentivePushNotification(data));
		assertEquals(Apptentive.getTitleFromApptentivePush(data), "The title.");
		assertEquals(Apptentive.getBodyFromApptentivePush(data), "The body.");
	}

	@Test
	public void pushDataNonApptentive() {
		Map<String, String> data = new HashMap<>();
		assertNull(Apptentive.buildPendingIntentFromPushNotification(data));
		assertFalse(Apptentive.isApptentivePushNotification(data));
		assertNull(Apptentive.getTitleFromApptentivePush(data));
		assertNull(Apptentive.getBodyFromApptentivePush(data));
	}

	@Test
	public void nullPushData() {
		Map<String, String> data = null;
		assertNull(Apptentive.buildPendingIntentFromPushNotification(data));
		assertFalse(Apptentive.isApptentivePushNotification(data));
		assertNull(Apptentive.getTitleFromApptentivePush(data));
		assertNull(Apptentive.getBodyFromApptentivePush(data));
	}
}