
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StandardPeriodVariant.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StandardPeriodVariant">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Custom"/>
 *     &lt;enumeration value="Today"/>
 *     &lt;enumeration value="ThisWeek"/>
 *     &lt;enumeration value="ThisTenDays"/>
 *     &lt;enumeration value="ThisMonth"/>
 *     &lt;enumeration value="ThisQuarter"/>
 *     &lt;enumeration value="ThisHalfYear"/>
 *     &lt;enumeration value="ThisYear"/>
 *     &lt;enumeration value="FromBeginningOfThisWeek"/>
 *     &lt;enumeration value="FromBeginningOfThisTenDays"/>
 *     &lt;enumeration value="FromBeginningOfThisMonth"/>
 *     &lt;enumeration value="FromBeginningOfThisQuarter"/>
 *     &lt;enumeration value="FromBeginningOfThisHalfYear"/>
 *     &lt;enumeration value="FromBeginningOfThisYear"/>
 *     &lt;enumeration value="Yesterday"/>
 *     &lt;enumeration value="LastWeek"/>
 *     &lt;enumeration value="LastTenDays"/>
 *     &lt;enumeration value="LastMonth"/>
 *     &lt;enumeration value="LastQuarter"/>
 *     &lt;enumeration value="LastHalfYear"/>
 *     &lt;enumeration value="LastYear"/>
 *     &lt;enumeration value="LastWeekTillSameWeekDay"/>
 *     &lt;enumeration value="LastTenDaysTillSameDayNumber"/>
 *     &lt;enumeration value="LastMonthTillSameDate"/>
 *     &lt;enumeration value="LastQuarterTillSameDate"/>
 *     &lt;enumeration value="LastHalfYearTillSameDate"/>
 *     &lt;enumeration value="LastYearTillSameDate"/>
 *     &lt;enumeration value="Tomorrow"/>
 *     &lt;enumeration value="NextWeek"/>
 *     &lt;enumeration value="NextTenDays"/>
 *     &lt;enumeration value="NextMonth"/>
 *     &lt;enumeration value="NextQuarter"/>
 *     &lt;enumeration value="NextHalfYear"/>
 *     &lt;enumeration value="NextYear"/>
 *     &lt;enumeration value="NextWeekTillSameWeekDay"/>
 *     &lt;enumeration value="NextTenDaysTillSameDayNumber"/>
 *     &lt;enumeration value="NextMonthTillSameDate"/>
 *     &lt;enumeration value="NextQuarterTillSameDate"/>
 *     &lt;enumeration value="NextHalfYearTillSameDate"/>
 *     &lt;enumeration value="NextYearTillSameDate"/>
 *     &lt;enumeration value="TillEndOfThisWeek"/>
 *     &lt;enumeration value="TillEndOfThisTenDays"/>
 *     &lt;enumeration value="TillEndOfThisMonth"/>
 *     &lt;enumeration value="TillEndOfThisQuarter"/>
 *     &lt;enumeration value="TillEndOfThisHalfYear"/>
 *     &lt;enumeration value="TillEndOfThisYear"/>
 *     &lt;enumeration value="Last7Days"/>
 *     &lt;enumeration value="Next7Days"/>
 *     &lt;enumeration value="Month"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StandardPeriodVariant")
@XmlEnum
public enum StandardPeriodVariant {

    @XmlEnumValue("Custom")
    CUSTOM("Custom"),
    @XmlEnumValue("Today")
    TODAY("Today"),
    @XmlEnumValue("ThisWeek")
    THIS_WEEK("ThisWeek"),
    @XmlEnumValue("ThisTenDays")
    THIS_TEN_DAYS("ThisTenDays"),
    @XmlEnumValue("ThisMonth")
    THIS_MONTH("ThisMonth"),
    @XmlEnumValue("ThisQuarter")
    THIS_QUARTER("ThisQuarter"),
    @XmlEnumValue("ThisHalfYear")
    THIS_HALF_YEAR("ThisHalfYear"),
    @XmlEnumValue("ThisYear")
    THIS_YEAR("ThisYear"),
    @XmlEnumValue("FromBeginningOfThisWeek")
    FROM_BEGINNING_OF_THIS_WEEK("FromBeginningOfThisWeek"),
    @XmlEnumValue("FromBeginningOfThisTenDays")
    FROM_BEGINNING_OF_THIS_TEN_DAYS("FromBeginningOfThisTenDays"),
    @XmlEnumValue("FromBeginningOfThisMonth")
    FROM_BEGINNING_OF_THIS_MONTH("FromBeginningOfThisMonth"),
    @XmlEnumValue("FromBeginningOfThisQuarter")
    FROM_BEGINNING_OF_THIS_QUARTER("FromBeginningOfThisQuarter"),
    @XmlEnumValue("FromBeginningOfThisHalfYear")
    FROM_BEGINNING_OF_THIS_HALF_YEAR("FromBeginningOfThisHalfYear"),
    @XmlEnumValue("FromBeginningOfThisYear")
    FROM_BEGINNING_OF_THIS_YEAR("FromBeginningOfThisYear"),
    @XmlEnumValue("Yesterday")
    YESTERDAY("Yesterday"),
    @XmlEnumValue("LastWeek")
    LAST_WEEK("LastWeek"),
    @XmlEnumValue("LastTenDays")
    LAST_TEN_DAYS("LastTenDays"),
    @XmlEnumValue("LastMonth")
    LAST_MONTH("LastMonth"),
    @XmlEnumValue("LastQuarter")
    LAST_QUARTER("LastQuarter"),
    @XmlEnumValue("LastHalfYear")
    LAST_HALF_YEAR("LastHalfYear"),
    @XmlEnumValue("LastYear")
    LAST_YEAR("LastYear"),
    @XmlEnumValue("LastWeekTillSameWeekDay")
    LAST_WEEK_TILL_SAME_WEEK_DAY("LastWeekTillSameWeekDay"),
    @XmlEnumValue("LastTenDaysTillSameDayNumber")
    LAST_TEN_DAYS_TILL_SAME_DAY_NUMBER("LastTenDaysTillSameDayNumber"),
    @XmlEnumValue("LastMonthTillSameDate")
    LAST_MONTH_TILL_SAME_DATE("LastMonthTillSameDate"),
    @XmlEnumValue("LastQuarterTillSameDate")
    LAST_QUARTER_TILL_SAME_DATE("LastQuarterTillSameDate"),
    @XmlEnumValue("LastHalfYearTillSameDate")
    LAST_HALF_YEAR_TILL_SAME_DATE("LastHalfYearTillSameDate"),
    @XmlEnumValue("LastYearTillSameDate")
    LAST_YEAR_TILL_SAME_DATE("LastYearTillSameDate"),
    @XmlEnumValue("Tomorrow")
    TOMORROW("Tomorrow"),
    @XmlEnumValue("NextWeek")
    NEXT_WEEK("NextWeek"),
    @XmlEnumValue("NextTenDays")
    NEXT_TEN_DAYS("NextTenDays"),
    @XmlEnumValue("NextMonth")
    NEXT_MONTH("NextMonth"),
    @XmlEnumValue("NextQuarter")
    NEXT_QUARTER("NextQuarter"),
    @XmlEnumValue("NextHalfYear")
    NEXT_HALF_YEAR("NextHalfYear"),
    @XmlEnumValue("NextYear")
    NEXT_YEAR("NextYear"),
    @XmlEnumValue("NextWeekTillSameWeekDay")
    NEXT_WEEK_TILL_SAME_WEEK_DAY("NextWeekTillSameWeekDay"),
    @XmlEnumValue("NextTenDaysTillSameDayNumber")
    NEXT_TEN_DAYS_TILL_SAME_DAY_NUMBER("NextTenDaysTillSameDayNumber"),
    @XmlEnumValue("NextMonthTillSameDate")
    NEXT_MONTH_TILL_SAME_DATE("NextMonthTillSameDate"),
    @XmlEnumValue("NextQuarterTillSameDate")
    NEXT_QUARTER_TILL_SAME_DATE("NextQuarterTillSameDate"),
    @XmlEnumValue("NextHalfYearTillSameDate")
    NEXT_HALF_YEAR_TILL_SAME_DATE("NextHalfYearTillSameDate"),
    @XmlEnumValue("NextYearTillSameDate")
    NEXT_YEAR_TILL_SAME_DATE("NextYearTillSameDate"),
    @XmlEnumValue("TillEndOfThisWeek")
    TILL_END_OF_THIS_WEEK("TillEndOfThisWeek"),
    @XmlEnumValue("TillEndOfThisTenDays")
    TILL_END_OF_THIS_TEN_DAYS("TillEndOfThisTenDays"),
    @XmlEnumValue("TillEndOfThisMonth")
    TILL_END_OF_THIS_MONTH("TillEndOfThisMonth"),
    @XmlEnumValue("TillEndOfThisQuarter")
    TILL_END_OF_THIS_QUARTER("TillEndOfThisQuarter"),
    @XmlEnumValue("TillEndOfThisHalfYear")
    TILL_END_OF_THIS_HALF_YEAR("TillEndOfThisHalfYear"),
    @XmlEnumValue("TillEndOfThisYear")
    TILL_END_OF_THIS_YEAR("TillEndOfThisYear"),
    @XmlEnumValue("Last7Days")
    LAST_7_DAYS("Last7Days"),
    @XmlEnumValue("Next7Days")
    NEXT_7_DAYS("Next7Days"),
    @XmlEnumValue("Month")
    MONTH("Month");
    private final String value;

    StandardPeriodVariant(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StandardPeriodVariant fromValue(String v) {
        for (StandardPeriodVariant c: StandardPeriodVariant.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
