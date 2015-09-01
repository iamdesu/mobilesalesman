package com.bali.nusadua.productmonitor.businessObject;

import android.content.Context;

import com.bali.nusadua.productmonitor.model.SettlementHeader;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.repo.SettlementHeaderRepo;
import com.bali.nusadua.productmonitor.repo.SettlementItemRepo;

import java.util.ArrayList;

/**
 * Created by desu on 8/30/15.
 */
public class SettlementBO {
    private SettlementItemRepo settlementItemRepo;
    private SettlementHeaderRepo settlementHeaderRepo;

    public SettlementBO(Context context) {
        this.settlementItemRepo = new SettlementItemRepo(context);
        this.settlementHeaderRepo = new SettlementHeaderRepo(context);
    }

    public void insertAll(String customerID, ArrayList<SettlementItem> orderItems) {
        SettlementHeader settlementHeader = new SettlementHeader();
        settlementHeader.setKodeOutlet(customerID);
        int orderHeaderId = settlementHeaderRepo.insert(settlementHeader);

        for (SettlementItem settlementItem : orderItems) {
            settlementItem.setSettlementHeaderId(orderHeaderId);
            settlementItemRepo.insert(settlementItem);
        }
    }

    public Integer getSettlementHeaderCountByCustomer(String customerID) {
        return settlementHeaderRepo.getCountByCustomer(customerID);
    }

    public Integer getSettlementItemCountByCustomer(String customerID) {
        return settlementHeaderRepo.getCountByCustomer(customerID);
    }

}
