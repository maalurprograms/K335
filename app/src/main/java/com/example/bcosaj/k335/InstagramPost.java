package com.example.bcosaj.k335;

import android.net.Uri;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class InstagramPost extends Post {
    public final int CONTENT;

    public InstagramPost(String CREATOR, int CONTENT, int SOURCE, String DATE) {
        super(CREATOR, SOURCE, DATE);
        this.CONTENT = CONTENT;
    }
}
