package com.alaryani.aamrny.network;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 */
public class AsyncHttpGet extends AsyncHttpBase {
	public AsyncHttpGet(Context context, AsyncHttpResponseProcess process,
			List<NameValuePair> parameters) {
		super(context, process, parameters);
	}

	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters, boolean isShowWaitingDialog) {
		super(context, listener, parameters, isShowWaitingDialog);
	}

	public AsyncHttpGet(Context context, AsyncHttpResponseProcess process) {
		super(context, process);
	}

	@Override
	protected String request(String url) {
		try {
			this.url = url;
			HttpParams params = new BasicHttpParams();
			String combinedParams = "";
			if (!parameters.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : parameters) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpConnectionParams.setConnectionTimeout(params,
					Config.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, Config.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);
			HttpGet httpget1 = new HttpGet(url + combinedParams);
			Log.e("url", url + combinedParams);
			response = EntityUtils.toString(httpclient.execute(httpget1)
					.getEntity(), HTTP.UTF_8);
			statusCode = NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
