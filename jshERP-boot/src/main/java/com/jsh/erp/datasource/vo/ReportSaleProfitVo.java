package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-销售利润
 * 对应 App 页面 pages_report/plugins/saleProfit.vue
 * 利润 = 销售金额 - 成本金额
 */
public class ReportSaleProfitVo {

    private Long id;
    /** 单据编号 */
    private String number;
    /** 单据类型 */
    private String newType;
    /** 商品名称 */
    private String materialName;
    /** 规格型号 */
    private String materialModel;
    /** 条码 */
    private String barCode;
    /** 单位 */
    private String materialUnit;
    /** 客户 */
    private String organName;
    /** 数量 */
    private BigDecimal operNumber;
    /** 销售金额 */
    private BigDecimal allPrice;
    /** 成本金额 */
    private BigDecimal costPrice;
    /** 利润 */
    private BigDecimal profitPrice;
    /** 单据日期 */
    private String operTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public BigDecimal getOperNumber() {
        return operNumber;
    }

    public void setOperNumber(BigDecimal operNumber) {
        this.operNumber = operNumber;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getProfitPrice() {
        return profitPrice;
    }

    public void setProfitPrice(BigDecimal profitPrice) {
        this.profitPrice = profitPrice;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }
}
