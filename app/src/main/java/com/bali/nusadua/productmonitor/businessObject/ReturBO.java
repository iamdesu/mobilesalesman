package com.bali.nusadua.productmonitor.businessObject;

import android.content.Context;

import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.repo.ReturHeaderRepo;
import com.bali.nusadua.productmonitor.repo.ReturRepo;

import java.util.ArrayList;

/**
 * Created by desu on 8/29/15.
 */
public class ReturBO {
    private ReturRepo returRepo;
    private ReturHeaderRepo returHeaderRepo;

    public ReturBO(Context context) {
        this.returRepo = new ReturRepo(context);
        this.returHeaderRepo = new ReturHeaderRepo(context);
    }

    public void insertAll(String customerID, ArrayList<ReturItem> returItems) {
        ReturHeader orderHeader = new ReturHeader();
        orderHeader.setKodeOutlet(customerID);
        int orderHeaderId = returHeaderRepo.insert(orderHeader);

        for (ReturItem returItem : returItems) {
            returItem.setReturHeaderId(orderHeaderId);
            returRepo.insert(returItem);
        }
    }

    public Integer getReturHeaderCountByCustomer(String customerID) {
        return returHeaderRepo.getCountByCustomer(customerID);
    }

    public Integer getReturItemCountByCustomer(String customerID) {
        return returRepo.getCountByCustomer(customerID);
    }
}
