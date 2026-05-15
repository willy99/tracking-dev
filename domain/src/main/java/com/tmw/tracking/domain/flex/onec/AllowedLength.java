
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllowedLength.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AllowedLength">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Fixed"/>
 *     &lt;enumeration value="Variable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AllowedLength")
@XmlEnum
public enum AllowedLength {

    @XmlEnumValue("Fixed")
    FIXED("Fixed"),
    @XmlEnumValue("Variable")
    VARIABLE("Variable");
    private final String value;

    AllowedLength(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AllowedLength fromValue(String v) {
        for (AllowedLength c: AllowedLength.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
