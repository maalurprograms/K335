package com.example.bcosaj.k335;

import android.widget.ImageView;

/**
 * Created by bcosaj on 23.03.2016.
 */
public abstract class Post {
    public final String CREATOR;
    public final String DATE;
    public int SOURCE;

    public Post(String CREATOR, String DATE) {
        this.CREATOR = CREATOR;
        this.DATE = DATE;
    }
}
