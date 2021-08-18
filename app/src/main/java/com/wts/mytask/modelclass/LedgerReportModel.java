package com.wts.mytask.modelclass;

public class LedgerReportModel {

    public LedgerReportModel() { }

    String oldBal,newBal,amount,balanceId,userId,PaymentType,remarks,transactionDate,idAddress,crDrType,payRefId,targetUser,whiteLabelId,updateBy,MobileNo;

    public LedgerReportModel(String oldBal, String newBal, String amount, String balanceId, String userId, String paymentType, String remarks, String transactionDate, String idAddress, String crDrType, String payRefId, String targetUser, String whiteLabelId, String updateBy, String mobileNo) {
        this.oldBal = oldBal;
        this.newBal = newBal;
        this.amount = amount;
        this.balanceId = balanceId;
        this.userId = userId;
        PaymentType = paymentType;
        this.remarks = remarks;
        this.transactionDate = transactionDate;
        this.idAddress = idAddress;
        this.crDrType = crDrType;
        this.payRefId = payRefId;
        this.targetUser = targetUser;
        this.whiteLabelId = whiteLabelId;
        this.updateBy = updateBy;
        MobileNo = mobileNo;

    }

    public String getOldBal() {
        return oldBal;
    }

    public void setOldBal(String oldBal) {
        this.oldBal = oldBal;
    }

    public String getNewBal() {
        return newBal;
    }

    public void setNewBal(String newBal) {
        this.newBal = newBal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getCrDrType() {
        return crDrType;
    }

    public void setCrDrType(String crDrType) {
        this.crDrType = crDrType;
    }

    public String getPayRefId() {
        return payRefId;
    }

    public void setPayRefId(String payRefId) {
        this.payRefId = payRefId;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

}
