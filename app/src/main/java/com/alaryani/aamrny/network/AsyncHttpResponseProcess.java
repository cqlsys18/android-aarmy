package com.alaryani.aamrny.network;

import org.json.JSONException;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	@Override
	public void before() {
	}

	@Override
	public void after(int statusCode, String response) {
		switch (statusCode) {
		case AsyncHttpBase.NETWORK_STATUS_OFF:
			break;

		case AsyncHttpBase.NETWORK_STATUS_OK:
			processHttpResponse(response);
			break;
		}
	}

	/**
	 * Process HttpResponse
	 * 
	 * @param response
	 */
	public void processHttpResponse(String response) {
		try {
			String json = response;
			if (json == null) {
				return;
			}
			processIfResponseSuccess(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Interface function
	 * 
	 * @throws JSONException
	 */
	public void processIfResponseSuccess(String response) {
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
	}
}
