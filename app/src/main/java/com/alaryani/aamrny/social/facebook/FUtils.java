package com.alaryani.aamrny.social.facebook;

public class FUtils {
	
	public static String getAvatarUrl(String id) {
		String url = "https://graph.facebook.com/" + id + "/picture?type=normal";
		return url;
	}
}
