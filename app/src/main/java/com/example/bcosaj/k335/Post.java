package com.example.bcosaj.k335;

import android.widget.ImageView;

/**
 * Created by bcosaj on 23.03.2016.
 */
public abstract class Post {
    public final String DATE;
    public int SOURCE;

    public Post(String DATE) {
        this.DATE = DATE;
    }
}
