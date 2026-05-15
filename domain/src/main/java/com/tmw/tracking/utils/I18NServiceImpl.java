package com.tmw.tracking.utils;

import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.tmw.tracking.entity.User;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Singleton
public class I18NServiceImpl implements I18NService {
    @Inject
    @Named(I18NModule.LANG_BUNDLE)
    private Map<Locale, LangProvider> langBundleMap;

    @Override
    public String getValue(String key) {
        return langBundleMap.get(getLocale()).get().get(key.toLowerCase());
    }

    @Override
    public String getValue(Enum<?> enumVal) {
        return langBundleMap.get(getLocale()).get().get(enumVal.getClass().getSimpleName() + '_' + enumVal.toString().toLowerCase());
    }

    @Override
    public HashMap<String, String> getBundle() {
        return langBundleMap.get(getLocale()).get();
    }

    private Locale getLocale() {
        if (DomainUtils.requestLocale.get() != null) {
            return DomainUtils.requestLocale.get();
        }
        User authUser = DomainUtils.getCurrentUser();
        return authUser != null ? authUser.getLocale() : Locale.US;
    }


}
