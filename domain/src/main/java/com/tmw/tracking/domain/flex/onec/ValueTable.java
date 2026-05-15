
package com.tmw.tracking.domain.flex.onec;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValueTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValueTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="column" type="{http://v8.1c.ru/8.1/data/core}ValueTableColumn" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="index" type="{http://v8.1c.ru/8.1/data/core}ValueTableIndex" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="row" type="{http://v8.1c.ru/8.1/data/core}ValueTableRow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueTable", propOrder = {
    "column",
    "index",
    "row"
})
public class ValueTable {

    protected List<ValueTableColumn> column;
    protected List<ValueTableIndex> index;
    protected List<ValueTableRow> row;

    /**
     * Gets the value of the column property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the column property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueTableColumn }
     * 
     * 
     */
    public List<ValueTableColumn> getColumn() {
        if (column == null) {
            column = new ArrayList<ValueTableColumn>();
        }
        return this.column;
    }

    /**
     * Gets the value of the index property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the index property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueTableIndex }
     * 
     * 
     */
    public List<ValueTableIndex> getIndex() {
        if (index == null) {
            index = new ArrayList<ValueTableIndex>();
        }
        return this.index;
    }

    /**
     * Gets the value of the row property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the row property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueTableRow }
     * 
     * 
     */
    public List<ValueTableRow> getRow() {
        if (row == null) {
            row = new ArrayList<ValueTableRow>();
        }
        return this.row;
    }

}
