package com.tmw.tracking.utils;

import com.tmw.tracking.web.service.exception.ValidationException;

public class ValidationUtils {


    //Flex Area

    public static String validateFlexSerialNum(String serialNum) {
        if (serialNum == null) {
            throw new ValidationException("Serial Num is empty! ");
        }

        serialNum = serialNum.toUpperCase().trim();
        return serialNum;
    }

    public static String validateFlexContainerNum(String containerNum) {
        if (containerNum == null) {
            throw new ValidationException("Container Number is empty! ");
        }
        containerNum = containerNum.toUpperCase().trim();
        return containerNum;
    }

    public static String validateFlexOrderNum(String orderNum) {
        if (orderNum == null) {
            throw new ValidationException("Order Number is empty! ");
        }
        orderNum = orderNum.toUpperCase().trim();
        return orderNum;
    }

}
