package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-销售占比
 * 对应 App 页面 pages_report/plugins/saleRatioStatistics.vue
 */
public class ReportSaleRatioVo {

    /** 商品名称 */
    private String materialName;
    /** 规格型号 */
    private String materialModel;
    /** 条码 */
    private String barCode;
    /** 单位 */
    private String materialUnit;
    /** 数量 */
    private BigDecimal operNumber;
    /** 金额 */
    private BigDecimal allPrice;
    /** 占比（百分比，保留两位） */
    private BigDecimal ratio;
    /** 单据编号（跳转用） */
    private String number;
    /** 单据类型（跳转用） */
    private String newType;

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

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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
}
