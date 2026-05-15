package com.tmw.tracking.utils;

import java.util.HashMap;

public interface I18NService {

    String getValue(String key);

    String getValue(Enum<?> enumVal);

    HashMap getBundle();
}
