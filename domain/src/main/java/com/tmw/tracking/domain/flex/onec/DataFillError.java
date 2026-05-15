
package com.tmw.tracking.domain.flex.onec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataFillError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataFillError">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataDescr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Status" type="{http://v8.1c.ru/8.1/data/core}FillCheckErrorStatus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataFillError", propOrder = {
    "data",
    "dataDescr",
    "text",
    "status"
})
public class DataFillError {

    @XmlElement(name = "Data", required = true)
    protected String data;
    @XmlElement(name = "DataDescr", required = true)
    protected String dataDescr;
    @XmlElement(name = "Text", required = true)
    protected String text;
    @XmlElement(name = "Status", required = true)
    @XmlSchemaType(name = "string")
    protected FillCheckErrorStatus status;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * Gets the value of the dataDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataDescr() {
        return dataDescr;
    }

    /**
     * Sets the value of the dataDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataDescr(String value) {
        this.dataDescr = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link FillCheckErrorStatus }
     *     
     */
    public FillCheckErrorStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link FillCheckErrorStatus }
     *     
     */
    public void setStatus(FillCheckErrorStatus value) {
        this.status = value;
    }

}
