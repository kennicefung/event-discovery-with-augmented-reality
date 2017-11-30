package com.goer.helper;

/**
 * Created by kennicefung on 1/5/2017.
 */

import android.text.Spanned;
import android.text.Html;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import android.util.Base64;

public class GlobalHelper {
    //private static String _API_URL = "http://192.168.0.101:8888";
    private static String _API_URL = "http://goer-project.tk";
    private static String _USER_AVATAR_IMG_PATH = "/upload/avatar/";
    private static String _EVENT_IMG_PATH = "/upload/event_image/";
    private static String _ARCHITECT_WORLD_PATH = "/plugin/wikitude/index.html";
    private static final String _WIKITUDE_SDK_KEY = "IyCtFdBH5fvUAxr5BOwW9xs4JWPC1R6opnakqwmXyZ0mydfPFg1bcs8SF+mhAEaFZITA2x2eDg3JX7uf+8iZTcfmmmxSZEw3jc+Jau8j5lPQUXrJ8j6lxdILg4HM5NqQ5/6cH8eftMGlYB42MphvteA2mVwfEENbhejQNaOURKdTYWx0ZWRfX6j2a2XuDPAdHlnKAvWeIFoOBq3RKnMjcYaAAuTweQGjrdY8ORBRsI6VwKUex6bh2l8sXm0M2h9S400SW7kKKiv+Lw43S8bh7BtETQ2eKH55vv9x6oR6IvRykTtsWyXAmPnyAa3JnarlP+gKu6bISFtBlDbZhsai0OUTmrh14ac/pSsJKLug1vTf6gwOtugwFw7hN4TF+RElzwRU7tLAGj7fkZ2eRGJLRikS6kxQazkErll9u8yZrCUyJcSVphsDck+UlyEOvtZnOPRbD5bfFXK0D2ZMUxf/BBJnwFkE957kfFTCTMD6IDRpsIY4eHjkgNglbr0pKkZ0IpqOnsBYEI3jgHhCgKbR4g3yZW552M+8qXj+psFyibfoR4baRdM9ZSOzbGakUdtBwoJ1XniblAs1vBLgbpqw2/CNUcGfzOwFd0wx4GyMCUnyYd5VQxG6DTws6DTuvB7JgLa4wfY8kZHU64xBAiWPaeyHN7g8FdhFBbD0bvTSFKE=";

    public static String APIUrl(){return _API_URL + "/api/";}
    public static String UserAvatarPath(){return _API_URL+_USER_AVATAR_IMG_PATH;}
    public static String EventImgPath(){return _API_URL+_EVENT_IMG_PATH;}
    public static String ArchitectWorldPath(){return _API_URL + _ARCHITECT_WORLD_PATH;}
    public static String ARKey(){return _WIKITUDE_SDK_KEY;}
    public static void setAPIUrl(String url){_API_URL=url;}

    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
