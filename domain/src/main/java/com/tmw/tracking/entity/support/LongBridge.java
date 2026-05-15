package com.tmw.tracking.entity.support;

import org.hibernate.search.bridge.StringBridge;

import java.util.Date;

public class LongBridge implements StringBridge {
    @Override
    public String objectToString(Object object) {
        if (object != null){
            return String.valueOf( ((Date) object ).getTime());
        }
        return "";
    }
}
