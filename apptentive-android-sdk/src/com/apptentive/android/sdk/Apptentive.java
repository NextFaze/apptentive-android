/*
 * Copyright (c) 2011, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.apptentive.android.sdk.module.metric.MetricPayload;
import com.apptentive.android.sdk.offline.PayloadManager;
import com.apptentive.android.sdk.util.EmailUtil;
import com.apptentive.android.sdk.util.Util;

/**
 * The Apptentive class is responsible for general initialization, and access to each Apptentive Module.
 * @author Sky Kelsey
 */
public class Apptentive {

	public static final String APPTENTIVE_API_VERSION = "0.1";

	private static Apptentive instance = null;
	private Application application;

	private Apptentive() {
	}

	/**
	 * Gets the Apptentive singleton instance.
	 * @return The Apptentive singleton instance.
	 */
	public static Apptentive getInstance() {
		if (instance == null) {
			instance = new Apptentive();
		}
		return instance;
	}

	/**
	 * Passes your application's Activity to Apptentive so we can initialize.
	 * @param activity The activity from which you are calling this method.
	 */
	public void setActivity(Activity activity) {
		this.application = activity.getApplication();
		Context appContext = activity.getApplicationContext();

		GlobalInfo.carrier = ((TelephonyManager) (application.getSystemService(Application.TELEPHONY_SERVICE))).getSimOperatorName();
		GlobalInfo.currentCarrier = ((TelephonyManager) (application.getSystemService(Application.TELEPHONY_SERVICE))).getNetworkOperatorName();
		GlobalInfo.networkType = ((TelephonyManager) (application.getSystemService(Application.TELEPHONY_SERVICE))).getNetworkType();
		GlobalInfo.appPackage = activity.getApplicationContext().getPackageName();
		GlobalInfo.androidId = Settings.Secure.getString(application.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		GlobalInfo.userEmail = getUserEmail(application.getApplicationContext());

		PayloadManager.getInstance().setContext(appContext);
		PayloadManager.getInstance().start();

		// Initialize modules.
		RatingModule.getInstance().setContext(application.getApplicationContext());
		FeedbackModule.getInstance().setContext(application.getApplicationContext());

		// Instrumentation
		MetricPayload metric = new MetricPayload(MetricPayload.Event.app__launch);
		PayloadManager.getInstance().putPayload(metric);
	}

	/**
	 * Call this from your main Activity's onDestroy() method, so we can clean up.
	 */
	public void onDestroy(){
		// Instrumentation
		MetricPayload metric = new MetricPayload(MetricPayload.Event.app__exit);
		PayloadManager.getInstance().putPayload(metric);
	}

	/**
	 * Sets your Apptentive API key.<br/><br/>
	 * This will be a long base64 token like:<br/>
	 * <strong>0d7c775a973b30ed6a8cb2cf6469af3168a8c5e38ccd26755d1fdaa3397c6575</strong>
	 * @param apiKey The API key.
	 */
	public void setApiKey(String apiKey) {
		GlobalInfo.apiKey = apiKey;
	}

	/**
	 * Sets your app's display name.<br/><br/>
	 * Should be something like "My App Name".
	 * @param name The display name of your app.
	 */
	public void setAppDisplayName(String name) {
		GlobalInfo.appDisplayName = name;
	}

	/**
	 * Sets the user email address. This address will be used in the feedback module, or elsewhere where needed.
	 * This method will override the email address that Apptentive looks for programmatically, but will not override
	 * an email address that the user has previously entered in an Apptentive dialog.
	 * @param email The user's email address.
	 */
	public void setUserEmail(String email) {
		GlobalInfo.userEmail = email;
	}


	/**
	 * Gets the Apptentive Rating Module.
	 * @return The Apptentive Rating Module.
	 */
	public RatingModule getRatingModule() {
		return RatingModule.getInstance();
	}

	/**
	 * Gets the Apptentive Feedback Module.
	 * @return The Apptentive Feedback Module.
	 */
	public FeedbackModule getFeedbackModule() {
		return FeedbackModule.getInstance();
	}

	/**
	 * Gets the Apptentive Survey Module.
	 * @return The Apptentive Survey Module.
	 */
	public SurveyModule getSurveyModule() {
		return SurveyModule.getInstance();
	}

	private static String getUserEmail(Context context) {
		if (Util.packageHasPermission(context, "android.permission.GET_ACCOUNTS")) {
			String email = EmailUtil.getEmail(context);
			if (email != null) {
				return email;
			}
		}
		return "";
	}
}
