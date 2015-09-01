package com.bali.nusadua.productmonitor.businessObject;

import android.content.Context;

import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.repo.OrderHeaderRepo;
import com.bali.nusadua.productmonitor.repo.OrderRepo;

import java.util.ArrayList;

/**
 * Created by desu on 8/23/15.
 */
public class OrderBO {

    private OrderRepo orderRepo;
    private OrderHeaderRepo orderHeaderRepo;

    public OrderBO(Context context) {
        this.orderRepo = new OrderRepo(context);
        this.orderHeaderRepo = new OrderHeaderRepo(context);
    }

    public void insertAll(String customerID, ArrayList<OrderItem> orderItems) {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setKodeOutlet(customerID);
        int orderHeaderId = orderHeaderRepo.insert(orderHeader);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderHeaderId(orderHeaderId);
            orderRepo.insert(orderItem);
        }
    }

    public Integer getOrderHeaderCountByCustomer(String customerID) {
        return orderHeaderRepo.getCountByCustomer(customerID);
    }

    public Integer getOrderItemCountByCustomer(String customerID) {
        return orderRepo.getCountByCustomer(customerID);
    }
}
