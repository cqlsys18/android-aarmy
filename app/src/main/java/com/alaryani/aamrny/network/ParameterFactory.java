package com.alaryani.aamrny.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

/**
 * ParameterFactory class builds parameters for web service apis
 * 
 */
public final class ParameterFactory {

	private static String KEY_TYPE_HOME = "sale";
	private static String KEY_TITLE = "title";
	private static String KEY_TYPE = "type";
	private static String KEY_MIN_PRICE = "minprice";
	private static String KEY_MAX_PRICE = "maxprice";
	private static String KEY_CITY = "city";
	private static String KEY_BEDROOM = "bedroom";
	private static String KEY_BATHROOM = "bathroom";
	private static String KEY_MIN_AREA = "minarea";
	private static String KEY_MAX_AREA = "maxarea";
	private static String KEY_GARAGE = "gara";
	private static String KEY_GARDEN = "garden";

	public static List<NameValuePair> createSerchParams(Context context,
			String typehome, String title, String type, String minprice,
			String maxprice, String city, String bedroom, String bathroom,
			String minarea, String maxarea, String garage, String garden) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(KEY_TYPE_HOME, typehome));
		parameters.add(new BasicNameValuePair(KEY_TITLE, title));
		parameters.add(new BasicNameValuePair(KEY_TYPE, type));
		parameters.add(new BasicNameValuePair(KEY_MIN_PRICE, minprice));
		parameters.add(new BasicNameValuePair(KEY_MAX_PRICE, maxprice));
		parameters.add(new BasicNameValuePair(KEY_CITY, city));
		parameters.add(new BasicNameValuePair(KEY_BEDROOM, bedroom));
		parameters.add(new BasicNameValuePair(KEY_BATHROOM, bathroom));
		parameters.add(new BasicNameValuePair(KEY_MIN_AREA, minarea));
		parameters.add(new BasicNameValuePair(KEY_MAX_AREA, maxarea));
		parameters.add(new BasicNameValuePair(KEY_GARAGE, garage));
		parameters.add(new BasicNameValuePair(KEY_GARDEN, garden));
		return parameters;
	}

	public static List<NameValuePair> createRealEstateIdParams(Context context,
			String id) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", id));

		return parameters;
	}

	public static List<NameValuePair> createListRealEstateParams(
			Context context, int index, int page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("start_index", Integer
				.toString(index)));
		parameters.add(new BasicNameValuePair("items_per_page", Integer
				.toString(page)));

		return parameters;
	}

	public static List<NameValuePair> createRealEstateByAgentParams(
			Context context, String id, String page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", id));
		parameters.add(new BasicNameValuePair("page", page));
		return parameters;
	}

	public static List<NameValuePair> createAgentIdParams(Context context,
			String id) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", id));

		return parameters;
	}

	// Add new code for new project
	public static List<NameValuePair> createLoginParams(String user, String pass) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("type", "0"));
		parameters.add(new BasicNameValuePair("user", user));
		parameters.add(new BasicNameValuePair("pass", pass));
		return parameters;
	}

	public static List<NameValuePair> createForgotParams(String email) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("email", email));
		return parameters;
	}

	public static List<NameValuePair> createLoginWithFaceBookParams(
			String name, String email, String facebookId, String image) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("type", "1"));
		parameters.add(new BasicNameValuePair("name", name));
		parameters.add(new BasicNameValuePair("email", email));
		parameters.add(new BasicNameValuePair("facebookId", facebookId));
		parameters.add(new BasicNameValuePair("image", image));
		return parameters;
	}

	public static List<NameValuePair> createResetPasswordParams(
			String currentPassword, String newPass, String userId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("current_pass", currentPassword));
		parameters.add(new BasicNameValuePair("new_pass", newPass));
		parameters.add(new BasicNameValuePair("userId", userId));
		return parameters;
	}

	public static List<NameValuePair> getListPropertyParams(String userId,
			String page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", userId));
		parameters.add(new BasicNameValuePair("page", page));
		return parameters;
	}

	public static List<NameValuePair> getListMessageParams(String userId,
			String page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("hostagentId", userId));
		parameters.add(new BasicNameValuePair("page", page));
		return parameters;
	}

	public static List<NameValuePair> getListSubscriptionParams(String userId,
			String page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("hostagentId", userId));
		parameters.add(new BasicNameValuePair("page", page));
		return parameters;
	}

	public static List<NameValuePair> createDeleteRealEstate(String id) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", id));
		return parameters;
	}

	public static List<NameValuePair> createPaymentParams(String paidDate,
			String endDate, String duration, String value, String estateId,
			String payment) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("paidDate", paidDate));
		parameters.add(new BasicNameValuePair("endDate", endDate));
		parameters.add(new BasicNameValuePair("duration", duration));
		parameters.add(new BasicNameValuePair("value", value));
		parameters.add(new BasicNameValuePair("estateId", estateId));
		parameters.add(new BasicNameValuePair("payment", payment));
		return parameters;
	}

	public static List<NameValuePair> createGetFacebookUserInfoParam(
			String accessToken) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));

		return parameters;
	}

	public static List<NameValuePair> createParams(String page) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("page", page));
		return parameters;
	}

	public static List<NameValuePair> createSendMessageParams(
			String hostagentId, String name, String phone, String email,
			String title, String content) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("estateId", ""));
		parameters.add(new BasicNameValuePair("hostagentId", hostagentId));
		parameters.add(new BasicNameValuePair("name", name));
		parameters.add(new BasicNameValuePair("phone", phone));
		parameters.add(new BasicNameValuePair("email", email));
		parameters.add(new BasicNameValuePair("title", title));
		parameters.add(new BasicNameValuePair("content", content));
		return parameters;
	}
}
