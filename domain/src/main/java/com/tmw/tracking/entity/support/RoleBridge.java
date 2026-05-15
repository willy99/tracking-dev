package com.tmw.tracking.entity.support;

import com.tmw.tracking.entity.Role;
import org.hibernate.search.bridge.StringBridge;

public class RoleBridge implements StringBridge{
    @Override
    public String objectToString(Object object) {
        if (object != null){
            return ( (Role) object ).getRoleName();
        }
        return "";
    }
}
