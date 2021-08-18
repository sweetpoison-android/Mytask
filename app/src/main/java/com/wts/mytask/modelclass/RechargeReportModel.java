package com.wts.mytask.modelclass;

public class RechargeReportModel {

    String sType,number,amount,commission,surcharge,cost,balance,date,status, transactionId,brid,operatorName,imgUrl;

    public RechargeReportModel() {
    }

    public RechargeReportModel(String sType, String number, String amount, String commission, String surcharge, String cost, String balance, String date, String status, String transactionId, String brid, String operatorName, String imgUrl) {
        this.sType = sType;
        this.number = number;
        this.amount = amount;
        this.commission = commission;
        this.surcharge = surcharge;
        this.cost = cost;
        this.balance = balance;
        this.date = date;
        this.status = status;
        this.transactionId = transactionId;
        this.brid = brid;
        this.operatorName = operatorName;
        this.imgUrl = imgUrl;

    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBrid() {
        return brid;
    }

    public void setBrid(String brid) {
        this.brid = brid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
