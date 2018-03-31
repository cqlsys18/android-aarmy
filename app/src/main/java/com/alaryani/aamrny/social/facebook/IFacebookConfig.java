package com.alaryani.aamrny.social.facebook;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Trang on 6/16/2016.
 */
public interface IFacebookConfig {

    /** login permission */
    public static final List<String> LOGIN_PERMISSIONS = Arrays.asList("public_profile", "email", "user_photos", "user_birthday");
    public static final String KEY_VALUES_PROFILE = "id,name,email,picture,birthday,gender";

}
