package com.alaryani.aamrny.social.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

/**
 * Created by Trang PV
 * Release 06/17/16 v1.0
 */
public class FaceBookManager implements IFacebookConfig {

    public static final String TAG = "FaceBookManager";
    private static final String GRAPH_POST_NEW_FEED = "me/feed";
    private static final String KEY_FIELDS = "fields";
    private static final String GET_AVARTAR = "https://graph.facebook.com/%s/picture?type=large";

    public static CallbackManager callbackManager;
    static boolean isCliked = false;

    /**
     * interface callback where called
     */
    public interface ICallbackLoginFacebook {
        void onLoginFbSuccess(FbUser user);

        void onLoginFbError();

        void onLoginFBnoEmailPublic();

        void onLoginFBLoginOrtherUser();
    }

    /**
     * initialize sdk facebook
     *
     * @param context
     */
    public static void initSdk(Context context) {
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

    }

    /**
     * Call this when click login button
     *
     * @param activity       using when call from activity.
     * @param mCallbackLogin responde to activity.onActivityResult()
     */
    public static void login(Activity activity, ICallbackLoginFacebook mCallbackLogin) {
        isCliked = true;
        LoginManager.getInstance().logInWithReadPermissions(activity, LOGIN_PERMISSIONS);
        registerCallback(mCallbackLogin);
    }

    /**
     * Call this when click login button
     *
     * @param fragment       using when call from activity.
     * @param mCallbackLogin responde to fragment.onActivityResult()
     */
    public static void login(Fragment fragment, ICallbackLoginFacebook mCallbackLogin) {
        isCliked = true;
        LoginManager.getInstance().logInWithReadPermissions(fragment, LOGIN_PERMISSIONS);
        registerCallback(mCallbackLogin);
    }

    /**
     * Login and listener respond
     */
    private static void registerCallback(final ICallbackLoginFacebook callbackLogin) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("facebook", "success access token: " + loginResult.getAccessToken().getToken());
                        getProfileFacebook(callbackLogin);
                        isCliked = false;

                    }

                    @Override
                    public void onCancel() {
                        Log.d("facebook", "onCancel");
                        callbackLogin.onLoginFbError();
                        isCliked = false;
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("facebook", "onError " + exception.getLocalizedMessage());
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        if (exception.getMessage().equals("User logged in as different Facebook user.")) {
                            callbackLogin.onLoginFBLoginOrtherUser();
                        } else {
                            callbackLogin.onLoginFbError();
                        }

                        isCliked = false;
                        // App code
                    }
                });
    }

    /**
     * get user's profile
     */
    private static void getProfileFacebook(final ICallbackLoginFacebook callbackLogin) {
        Bundle paramsInfor = new Bundle();
        paramsInfor.putString(KEY_FIELDS, KEY_VALUES_PROFILE);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me", paramsInfor, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("facebook", "Information: " + response.getRawResponse());
                        Gson gson = new Gson();
                        FbUser user = gson.fromJson(response.getRawResponse().toString(), FbUser.class);
                        user.setAvatar(String.format(GET_AVARTAR, user.getId()));
                        if (user.getEmail() != null && user.getEmail().length() > 0) {
                            callbackLogin.onLoginFbSuccess(user);
                        } else {
                            callbackLogin.onLoginFBnoEmailPublic();
                        }

                    }
                }
        ).executeAsync();
    }

    /**
     * Call this in activity.onActivityResult
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isCliked)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * sharing url
     */
    public static void share(Activity activity, String contentUrl) {
        if (!FacebookSdk.isInitialized())
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
        ShareDialog shareDialog = new ShareDialog(activity);
        // ShareDialog shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(contentUrl))
                    .setContentTitle("")
                        /*.setContentDescription(
                                song.getArtist())*/
                            // .setImageUrl(Uri.parse(""))
                    .build();

            shareDialog.show(linkContent);
        }
    }


}
