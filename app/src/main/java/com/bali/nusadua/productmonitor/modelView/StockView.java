package com.bali.nusadua.productmonitor.modelView;

import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;

/**
 * Created by desu on 6/26/15.
 */
public class StockView {
    private StockBilling stockBilling = null;
    private StockPrice stockPrice = null;

    public StockBilling getStockBilling() {
        return stockBilling;
    }

    public void setStockBilling(StockBilling stockBilling) {
        this.stockBilling = stockBilling;
    }

    public StockPrice getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(StockPrice stockPrice) {
        this.stockPrice = stockPrice;
    }
}
