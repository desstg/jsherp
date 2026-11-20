package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-业绩提成
 * 对应 App 页面 pages_report/plugins/modules/commisionBill.vue
 */
public class ReportCommisionVo {

    /** 单据主键 */
    private Long billId;
    /** 单据编号 */
    private String number;
    /** 单据类型：bill=业务单据，financial=财务单据 */
    private String fromType;
    /** 业务类型（收款/付款/销售等） */
    private String billType;
    /** 销售员 */
    private String salesMan;
    /** 客户/供应商 */
    private String organName;
    /** 单据金额 */
    private BigDecimal allPrice;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(String salesMan) {
        this.salesMan = salesMan;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }
}
