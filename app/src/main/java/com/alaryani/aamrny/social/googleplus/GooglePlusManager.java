package com.alaryani.aamrny.social.googleplus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.alaryani.aamrny.utility.AppUtil;
import com.alaryani.aamrny.widget.dialog.MyProgressDialog;

/**
 * Created by Trang PV
 * Release 06/17/16 v1.0
 */
public class GooglePlusManager implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "GooglePlusManager";
    public static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity mActivity;
    private MyProgressDialog mDialog;

    private ICallbackGoogleLogin mCallbackGoogleLogin;

    /**
     * interface callback where called
     */
    public interface ICallbackGoogleLogin {
        void onLoginGgSuccess(GUser user);

        void onLoginGgError();
    }

    /**
     * Constructor
     *
     * @param activity
     * @param iCallbackGoogleLogin callback
     */
    public GooglePlusManager(FragmentActivity activity, ICallbackGoogleLogin iCallbackGoogleLogin) {
        this.mActivity = activity;
        mCallbackGoogleLogin = iCallbackGoogleLogin;
        mDialog = new MyProgressDialog(mActivity);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        /*if (result.hasResolution()) {
            try {
                // !!!
                result.startResolutionForResult(mActivity, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }*/

        Log.d(TAG, "onConnectionFailed:" + result);
    }

    // [START signIn]
    public void login() {
        mDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]


    /**
     * Call this #activity.onActivityResult()
     */
    public void onActivityResult(int requestCode, Intent data) {
        // Result returaned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            GUser user = new GUser();
            user.setFullname(acct.getDisplayName());
            user.setEmail(acct.getEmail());

            if (acct.getPhotoUrl() == null) {
                user.setAvatar("http://wiseheartdesign.com/images/articles/default-avatar.png");
            } else {
                user.setAvatar(acct.getPhotoUrl().toString());
            }
            user.setId(acct.getId());
            user.setGender("");
            // by default the profile url gives 50x50 px image only
            // we can replace the value with whatever dimension we want by
            // replacing sz=X
            mDialog.dismiss();
            mCallbackGoogleLogin.onLoginGgSuccess(user);

        } else {
            mDialog.dismiss();
            signOut();
            // Signed out, show unauthenticated UI. false
            mCallbackGoogleLogin.onLoginGgError();

        }
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        AppUtil.showToast(mActivity, "Signed out");
                    }
                });
    }

    public void onStart() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            mDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    mDialog.dismiss();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

}
