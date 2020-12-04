package org.techtown.ssubook;

import android.widget.ImageView;

public class User {
    public String username;
    //public ImageView image;
    public String image;
    public String title;
    public String main_text;

    public User(String username, String image, String title, String main_text) {
        this.username = username;
        this.image=image;
        this.title = title;
        this.main_text = main_text;
    }

    public User() {
    }
}
