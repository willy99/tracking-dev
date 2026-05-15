package com.tmw.tracking.entity.support;

import com.tmw.tracking.entity.Company;
import org.hibernate.search.bridge.StringBridge;

public class TenantBridge implements StringBridge {

    @Override
    public String objectToString(Object object) {
        if (object != null){
            return ( (Company) object ).getId().toString();
        }
        return null;
    }
}



