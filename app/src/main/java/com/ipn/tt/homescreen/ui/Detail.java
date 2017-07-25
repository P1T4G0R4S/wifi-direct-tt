package com.ipn.tt.homescreen.ui;

import com.ipn.tt.homescreen.db.User;

/**
 * Created by Iguanna on 02/07/2017.
 */

public class Detail {
    final String mName;
    int mPosition;
    int id;

    public Detail(User usr) {
        this.mName = usr.name;
        this.id = usr.id_user;
        //this.mPosition = mPosition;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        mName = mName;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int number) {
        mPosition = mPosition;
    }

    public int getId(int id){
        return id;
    }

}
