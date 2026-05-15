
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paramload" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "paramload"
})
@XmlRootElement(name = "GetOrders")
public class GetOrders {

    @XmlElement(required = true)
    protected String paramload;

    /**
     * Gets the value of the paramload property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParamload() {
        return paramload;
    }

    /**
     * Sets the value of the paramload property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParamload(String value) {
        this.paramload = value;
    }

}
