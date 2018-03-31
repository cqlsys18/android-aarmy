package com.alaryani.aamrny.modelmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ParameterFactory {
	
	public static List<NameValuePair> createLoginParams(String gcm_id, String email, String ime, String type, 
			String lat,String lng) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("gcm_id", gcm_id));
		parameters.add(new BasicNameValuePair("email", email));
		parameters.add(new BasicNameValuePair("ime", ime));
		parameters.add(new BasicNameValuePair("type", type));
		parameters.add(new BasicNameValuePair("lat", lat));
		parameters.add(new BasicNameValuePair("long", lng));
		return parameters;
	}
	
	public static List<NameValuePair> createRegisterParams(String id, String full_name,
			String image, String email) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("gcm_id", id));
		parameters.add(new BasicNameValuePair("name", full_name));
		parameters.add(new BasicNameValuePair("image", image));
		parameters.add(new BasicNameValuePair("email", email));

		return parameters;
	}

}
