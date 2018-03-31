package com.alaryani.aamrny.social.twitter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Trang on 1/22/2016.
 */
public class TwitterUtil {

    public static void share(Context context, String url , String message){
        String tweetUrl = "https://twitter.com/intent/tweet?text="+message
                +"&url="+url;
        Uri uri = Uri.parse(tweetUrl);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
