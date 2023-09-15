package com.upc.saveup.service;

import com.upc.saveup.dto.PayDto;
import com.upc.saveup.model.Pay;

import java.util.List;

public interface PayService {
    public abstract Pay createPay(PayDto payDto);
    public abstract void updatePay(PayDto payDto);
    public abstract void updateAmountPay(Pay pay);
    public abstract void deletePay(int id);
    public abstract Pay getPay(int id);
    public abstract List<Pay> getAllPays();
    public abstract boolean isPayExist(int id);
}
