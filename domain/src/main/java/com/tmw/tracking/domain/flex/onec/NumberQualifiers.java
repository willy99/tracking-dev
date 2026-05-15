
package com.tmw.tracking.domain.flex.onec;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NumberQualifiers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NumberQualifiers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Digits" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="FractionDigits" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AllowedSign" type="{http://v8.1c.ru/8.1/data/core}AllowedSign"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumberQualifiers", propOrder = {
    "digits",
    "fractionDigits",
    "allowedSign"
})
public class NumberQualifiers {

    @XmlElement(name = "Digits", required = true)
    protected BigDecimal digits;
    @XmlElement(name = "FractionDigits", required = true)
    protected BigDecimal fractionDigits;
    @XmlElement(name = "AllowedSign", required = true)
    @XmlSchemaType(name = "string")
    protected AllowedSign allowedSign;

    /**
     * Gets the value of the digits property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDigits() {
        return digits;
    }

    /**
     * Sets the value of the digits property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDigits(BigDecimal value) {
        this.digits = value;
    }

    /**
     * Gets the value of the fractionDigits property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFractionDigits() {
        return fractionDigits;
    }

    /**
     * Sets the value of the fractionDigits property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFractionDigits(BigDecimal value) {
        this.fractionDigits = value;
    }

    /**
     * Gets the value of the allowedSign property.
     * 
     * @return
     *     possible object is
     *     {@link AllowedSign }
     *     
     */
    public AllowedSign getAllowedSign() {
        return allowedSign;
    }

    /**
     * Sets the value of the allowedSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllowedSign }
     *     
     */
    public void setAllowedSign(AllowedSign value) {
        this.allowedSign = value;
    }

}
