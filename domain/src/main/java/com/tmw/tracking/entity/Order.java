package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.enums.OrderStatus;
import com.tmw.tracking.entity.enums.Trend;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="tr_order")
@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
public class Order extends UpdatableEntity {

    private static final long serialVersionUID = -6886848877574564547L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="terminal", nullable=false, updatable = true)
    @IndexedEmbedded
    private Terminal terminal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tracking_line", nullable=false, updatable = true)
    @IndexedEmbedded
    private TrackingLine trackingLine;

    @Column(nullable = true, name = "order_1c")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private String order1c;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @IndexedEmbedded
    private List<OrderDetails> orderDetails;

    @Column(nullable = false, name = "order_date")
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client", nullable=false, updatable = true)
    @IndexedEmbedded
    private User client;

    @Column(nullable = false,name = "content_name")
    private String contentName;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="trend")
    private  Trend trend;

    @Transient
    private Integer containerQty;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderWorkflow> workflow;


    public Terminal getTerminal() {
        return terminal;
    }

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public String getOrder1c() {
        return order1c;
    }

    public void setOrder1c(String order1c) {
        this.order1c = order1c;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public TrackingLine getTrackingLine() {
        return trackingLine;
    }

    public void setTrackingLine(TrackingLine trackingLine) {
        this.trackingLine = trackingLine;
    }

    public Integer getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(Integer containerQty) {
        this.containerQty = containerQty;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public List<OrderWorkflow> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<OrderWorkflow> workflow) {
        this.workflow = workflow;
    }

    public OrderStatus getCurrentStatus() {
        if (getWorkflow() != null && getWorkflow().size()>0) {
            return getWorkflow().get(0).getStatus();
        }
        return OrderStatus.REGISTERED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        if (!super.equals(o)) return false;

        Order order = (Order) o;

        if (getTerminal() != null ? !getTerminal().equals(order.getTerminal()) : order.getTerminal() != null)
            return false;
        if (getTrackingLine() != null ? !getTrackingLine().equals(order.getTrackingLine()) : order.getTrackingLine() != null)
            return false;
        if (getOrder1c() != null ? !getOrder1c().equals(order.getOrder1c()) : order.getOrder1c() != null) return false;
        if (getOrderDetails() != null ? !getOrderDetails().equals(order.getOrderDetails()) : order.getOrderDetails() != null)
            return false;
        if (getOrderDate() != null ? !getOrderDate().equals(order.getOrderDate()) : order.getOrderDate() != null)
            return false;
        if (getClient() != null ? !getClient().equals(order.getClient()) : order.getClient() != null) return false;
        if (getContentName() != null ? !getContentName().equals(order.getContentName()) : order.getContentName() != null)
            return false;
        if (getTrend() != order.getTrend()) return false;
        if (getContainerQty() != null ? !getContainerQty().equals(order.getContainerQty()) : order.getContainerQty() != null)
            return false;
        return getWorkflow() != null ? getWorkflow().equals(order.getWorkflow()) : order.getWorkflow() == null;
    }

    @Override
    public int hashCode() {
        int result = getTerminal() != null ? getTerminal().hashCode() : 0;
        result = 31 * result + (getTrackingLine() != null ? getTrackingLine().hashCode() : 0);
        result = 31 * result + (getOrder1c() != null ? getOrder1c().hashCode() : 0);
        result = 31 * result + (getOrderDetails() != null ? getOrderDetails().hashCode() : 0);
        result = 31 * result + (getOrderDate() != null ? getOrderDate().hashCode() : 0);
        result = 31 * result + (getClient() != null ? getClient().hashCode() : 0);
        result = 31 * result + (getContentName() != null ? getContentName().hashCode() : 0);
        result = 31 * result + (getTrend() != null ? getTrend().hashCode() : 0);
        result = 31 * result + (getContainerQty() != null ? getContainerQty().hashCode() : 0);
        result = 31 * result + (getWorkflow() != null ? getWorkflow().hashCode() : 0);
        return result;
    }
}
