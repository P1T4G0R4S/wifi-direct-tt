package com.ipn.tt.homescreen.db;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iguanna on 23/07/2017.
 */
public enum UserType {
    MYSELF(0),
    CONTACT(1),
    FIND(2),
    USER(3);

    public int id;

    UserType(int id) {
        this.id = id;
    }

    public int getCode() { return id; }

    public static UserType get(int id) {
        return lookup.get(id);
    }

    private static final Map<Integer,UserType> lookup
            = new HashMap<>();

    static {
        for(UserType s : EnumSet.allOf(UserType.class))
            lookup.put(s.getCode(), s);
    }
}
