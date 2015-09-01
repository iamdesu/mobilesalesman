package com.bali.nusadua.productmonitor.modelView;

import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;

/**
 * Created by desu on 8/23/15.
 */
public class OrderHeaderItemView {
    private OrderHeader orderHeader;
    private OrderItem orderItem;

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
