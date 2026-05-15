
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllowedSign.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AllowedSign">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Any"/>
 *     &lt;enumeration value="Nonnegative"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AllowedSign")
@XmlEnum
public enum AllowedSign {

    @XmlEnumValue("Any")
    ANY("Any"),
    @XmlEnumValue("Nonnegative")
    NONNEGATIVE("Nonnegative");
    private final String value;

    AllowedSign(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AllowedSign fromValue(String v) {
        for (AllowedSign c: AllowedSign.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
