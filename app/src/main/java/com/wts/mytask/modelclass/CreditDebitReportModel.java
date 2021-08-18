package com.wts.mytask.modelclass;

public class CreditDebitReportModel {


    String drUser,crUser,amount,ID,paymentType,paymentDate,remarks;

    public CreditDebitReportModel() {
    }

    public CreditDebitReportModel(String drUser, String crUser, String amount, String ID, String paymentType, String paymentDate, String remarks) {
        this.drUser = drUser;
        this.crUser = crUser;
        this.amount = amount;
        this.ID = ID;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
        this.remarks = remarks;

    }

    public String getDrUser() {
        return drUser;
    }

    public void setDrUser(String drUser) {
        this.drUser = drUser;
    }

    public String getCrUser() {
        return crUser;
    }

    public void setCrUser(String crUser) {
        this.crUser = crUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
