package com.example.bcosaj.k335;

import android.widget.ImageView;

/**
 * Created by bcosaj on 23.03.2016.
 */
public class Post {
    public final String CREATOR;
    public final String CONTENT;
    public final int SOURCE;
    public final String DATE;

    public Post(String CREATOR, String CONTENT, int SOURCE, String DATE) {
        this.CREATOR = CREATOR;
        this.CONTENT = CONTENT;
        this.SOURCE = SOURCE;
        this.DATE = DATE;
    }
}
