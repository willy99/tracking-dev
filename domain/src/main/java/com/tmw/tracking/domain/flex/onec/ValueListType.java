
package com.tmw.tracking.domain.flex.onec;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValueListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValueListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="valueType" type="{http://v8.1c.ru/8.1/data/core}TypeDescription"/>
 *         &lt;element name="availableValues" type="{http://v8.1c.ru/8.1/data/core}ValueListType" minOccurs="0"/>
 *         &lt;element name="item" type="{http://v8.1c.ru/8.1/data/core}ValueListItemType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lastId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueListType", propOrder = {
    "valueType",
    "availableValues",
    "item",
    "lastId"
})
public class ValueListType {

    @XmlElement(required = true)
    protected TypeDescription valueType;
    protected ValueListType availableValues;
    protected List<ValueListItemType> item;
    protected Long lastId;

    /**
     * Gets the value of the valueType property.
     * 
     * @return
     *     possible object is
     *     {@link TypeDescription }
     *     
     */
    public TypeDescription getValueType() {
        return valueType;
    }

    /**
     * Sets the value of the valueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeDescription }
     *     
     */
    public void setValueType(TypeDescription value) {
        this.valueType = value;
    }

    /**
     * Gets the value of the availableValues property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListType }
     *     
     */
    public ValueListType getAvailableValues() {
        return availableValues;
    }

    /**
     * Sets the value of the availableValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListType }
     *     
     */
    public void setAvailableValues(ValueListType value) {
        this.availableValues = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the item property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueListItemType }
     * 
     * 
     */
    public List<ValueListItemType> getItem() {
        if (item == null) {
            item = new ArrayList<ValueListItemType>();
        }
        return this.item;
    }

    /**
     * Gets the value of the lastId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLastId() {
        return lastId;
    }

    /**
     * Sets the value of the lastId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLastId(Long value) {
        this.lastId = value;
    }

}
