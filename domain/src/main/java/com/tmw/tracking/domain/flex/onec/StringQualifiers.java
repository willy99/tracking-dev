
package com.tmw.tracking.domain.flex.onec;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StringQualifiers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StringQualifiers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AllowedLength" type="{http://v8.1c.ru/8.1/data/core}AllowedLength"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StringQualifiers", propOrder = {
    "length",
    "allowedLength"
})
public class StringQualifiers {

    @XmlElement(name = "Length", required = true)
    protected BigDecimal length;
    @XmlElement(name = "AllowedLength", required = true)
    @XmlSchemaType(name = "string")
    protected AllowedLength allowedLength;

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLength(BigDecimal value) {
        this.length = value;
    }

    /**
     * Gets the value of the allowedLength property.
     * 
     * @return
     *     possible object is
     *     {@link AllowedLength }
     *     
     */
    public AllowedLength getAllowedLength() {
        return allowedLength;
    }

    /**
     * Sets the value of the allowedLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllowedLength }
     *     
     */
    public void setAllowedLength(AllowedLength value) {
        this.allowedLength = value;
    }

}
