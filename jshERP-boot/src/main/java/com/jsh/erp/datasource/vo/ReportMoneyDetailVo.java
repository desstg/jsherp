package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-收付款明细
 * 对应 App 页面 pages_report/plugins/moneyDetail.vue
 */
public class ReportMoneyDetailVo {

    private Long id;
    private Long billId;
    /** 单据编号 */
    private String billNo;
    /** 关联单据编号 */
    private String billNumber;
    /** 类型：收款/付款 */
    private String type;
    /** 往来单位 */
    private String organName;
    /** 经手人 */
    private String handsPersonName;
    /** 制单人 */
    private String userName;
    /** 账户名称 */
    private String accountName;
    /** 金额 */
    private BigDecimal changeAmount;
    /** 备注 */
    private String remark;
    /** 单据日期 */
    private String billTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getHandsPersonName() {
        return handsPersonName;
    }

    public void setHandsPersonName(String handsPersonName) {
        this.handsPersonName = handsPersonName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }
}
