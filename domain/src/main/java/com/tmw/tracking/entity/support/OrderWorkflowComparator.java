package com.tmw.tracking.entity.support;

import com.tmw.tracking.entity.OrderWorkflow;

import java.util.Comparator;

/**
 * Created by pzhelnov on 5/26/2017.
 */
public class OrderWorkflowComparator implements Comparator<OrderWorkflow> {

    public int compare(OrderWorkflow ow1, OrderWorkflow ow2){
        return ow1.getDealDate().compareTo(ow2.getDealDate());
    }
}
