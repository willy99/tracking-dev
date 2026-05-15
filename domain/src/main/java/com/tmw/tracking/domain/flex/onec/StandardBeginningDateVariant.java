
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StandardBeginningDateVariant.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StandardBeginningDateVariant">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Custom"/>
 *     &lt;enumeration value="BeginningOfThisDay"/>
 *     &lt;enumeration value="BeginningOfThisWeek"/>
 *     &lt;enumeration value="BeginningOfThisTenDays"/>
 *     &lt;enumeration value="BeginningOfThisMonth"/>
 *     &lt;enumeration value="BeginningOfThisQuarter"/>
 *     &lt;enumeration value="BeginningOfThisHalfYear"/>
 *     &lt;enumeration value="BeginningOfThisYear"/>
 *     &lt;enumeration value="BeginningOfLastDay"/>
 *     &lt;enumeration value="BeginningOfLastWeek"/>
 *     &lt;enumeration value="BeginningOfLastTenDays"/>
 *     &lt;enumeration value="BeginningOfLastMonth"/>
 *     &lt;enumeration value="BeginningOfLastQuarter"/>
 *     &lt;enumeration value="BeginningOfLastHalfYear"/>
 *     &lt;enumeration value="BeginningOfLastYear"/>
 *     &lt;enumeration value="BeginningOfNextDay"/>
 *     &lt;enumeration value="BeginningOfNextWeek"/>
 *     &lt;enumeration value="BeginningOfNextTenDays"/>
 *     &lt;enumeration value="BeginningOfNextMonth"/>
 *     &lt;enumeration value="BeginningOfNextQuarter"/>
 *     &lt;enumeration value="BeginningOfNextHalfYear"/>
 *     &lt;enumeration value="BeginningOfNextYear"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StandardBeginningDateVariant")
@XmlEnum
public enum StandardBeginningDateVariant {

    @XmlEnumValue("Custom")
    CUSTOM("Custom"),
    @XmlEnumValue("BeginningOfThisDay")
    BEGINNING_OF_THIS_DAY("BeginningOfThisDay"),
    @XmlEnumValue("BeginningOfThisWeek")
    BEGINNING_OF_THIS_WEEK("BeginningOfThisWeek"),
    @XmlEnumValue("BeginningOfThisTenDays")
    BEGINNING_OF_THIS_TEN_DAYS("BeginningOfThisTenDays"),
    @XmlEnumValue("BeginningOfThisMonth")
    BEGINNING_OF_THIS_MONTH("BeginningOfThisMonth"),
    @XmlEnumValue("BeginningOfThisQuarter")
    BEGINNING_OF_THIS_QUARTER("BeginningOfThisQuarter"),
    @XmlEnumValue("BeginningOfThisHalfYear")
    BEGINNING_OF_THIS_HALF_YEAR("BeginningOfThisHalfYear"),
    @XmlEnumValue("BeginningOfThisYear")
    BEGINNING_OF_THIS_YEAR("BeginningOfThisYear"),
    @XmlEnumValue("BeginningOfLastDay")
    BEGINNING_OF_LAST_DAY("BeginningOfLastDay"),
    @XmlEnumValue("BeginningOfLastWeek")
    BEGINNING_OF_LAST_WEEK("BeginningOfLastWeek"),
    @XmlEnumValue("BeginningOfLastTenDays")
    BEGINNING_OF_LAST_TEN_DAYS("BeginningOfLastTenDays"),
    @XmlEnumValue("BeginningOfLastMonth")
    BEGINNING_OF_LAST_MONTH("BeginningOfLastMonth"),
    @XmlEnumValue("BeginningOfLastQuarter")
    BEGINNING_OF_LAST_QUARTER("BeginningOfLastQuarter"),
    @XmlEnumValue("BeginningOfLastHalfYear")
    BEGINNING_OF_LAST_HALF_YEAR("BeginningOfLastHalfYear"),
    @XmlEnumValue("BeginningOfLastYear")
    BEGINNING_OF_LAST_YEAR("BeginningOfLastYear"),
    @XmlEnumValue("BeginningOfNextDay")
    BEGINNING_OF_NEXT_DAY("BeginningOfNextDay"),
    @XmlEnumValue("BeginningOfNextWeek")
    BEGINNING_OF_NEXT_WEEK("BeginningOfNextWeek"),
    @XmlEnumValue("BeginningOfNextTenDays")
    BEGINNING_OF_NEXT_TEN_DAYS("BeginningOfNextTenDays"),
    @XmlEnumValue("BeginningOfNextMonth")
    BEGINNING_OF_NEXT_MONTH("BeginningOfNextMonth"),
    @XmlEnumValue("BeginningOfNextQuarter")
    BEGINNING_OF_NEXT_QUARTER("BeginningOfNextQuarter"),
    @XmlEnumValue("BeginningOfNextHalfYear")
    BEGINNING_OF_NEXT_HALF_YEAR("BeginningOfNextHalfYear"),
    @XmlEnumValue("BeginningOfNextYear")
    BEGINNING_OF_NEXT_YEAR("BeginningOfNextYear");
    private final String value;

    StandardBeginningDateVariant(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StandardBeginningDateVariant fromValue(String v) {
        for (StandardBeginningDateVariant c: StandardBeginningDateVariant.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
