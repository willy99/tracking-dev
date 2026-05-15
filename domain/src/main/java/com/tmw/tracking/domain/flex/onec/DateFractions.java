
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DateFractions.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DateFractions">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Date"/>
 *     &lt;enumeration value="Time"/>
 *     &lt;enumeration value="DateTime"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DateFractions")
@XmlEnum
public enum DateFractions {

    @XmlEnumValue("Date")
    DATE("Date"),
    @XmlEnumValue("Time")
    TIME("Time"),
    @XmlEnumValue("DateTime")
    DATE_TIME("DateTime");
    private final String value;

    DateFractions(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DateFractions fromValue(String v) {
        for (DateFractions c: DateFractions.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
