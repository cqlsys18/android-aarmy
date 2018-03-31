package com.alaryani.aamrny.network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 */
public class AsyncHttpPost extends AsyncHttpBase {
	public AsyncHttpPost(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters, boolean isShowDilog) {
		super(context, listener, parameters, isShowDilog);
	}

	public AsyncHttpPost(Context context, AsyncHttpResponseListener listener,
			String json, boolean isShowDilog) {
		super(context, listener, json, isShowDilog);
	}

	public AsyncHttpPost(Context context, AsyncHttpResponseProcess process,
			List<NameValuePair> parameters, boolean isShowDilag) {
		super(context, process, parameters, isShowDilag);
	}

	@Override
	protected String request(String url) {
		try {
			this.url = url;
			Log.i(this.getClass().getSimpleName(), url);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					Config.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, Config.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);

			HttpPost httppost = new HttpPost(url);

			if (!isExternalParam) {
				String temp = "";
				for (NameValuePair pair : parameters) {
					temp += "&" + pair.getName() + "=" + pair.getValue();
				}
				Log.i(this.getClass().getSimpleName(),
						url + "?" + temp.substring(1));
				httppost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			} else {
				StringEntity se;
				se = new StringEntity(jsonString, HTTP.UTF_8);
				httppost.setEntity(se);

				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type",
						"application/json;charset=UTF-8");
			}

			response = EntityUtils.toString(httpclient.execute(httppost)
					.getEntity(), HTTP.UTF_8);
			Log.i(this.getClass().getSimpleName(), response);
			statusCode = NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
