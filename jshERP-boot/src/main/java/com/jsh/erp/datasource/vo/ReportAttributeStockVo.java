package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-多属性库存
 * 对应 App 页面 pages_report/plugins/attributeStock.vue
 */
public class ReportAttributeStockVo {

    private Long materialId;
    private Long materialExtendId;
    /** 仓库名称 */
    private String depotName;
    /** 商品名称 */
    private String materialName;
    /** 条码 */
    private String barCode;
    /** 规格型号 */
    private String materialModel;
    /** 单位 */
    private String materialUnit;
    /** SKU 组合 */
    private String sku;
    /** 属性1 */
    private String attribute1;
    /** 属性2 */
    private String attribute2;
    /** 属性3 */
    private String attribute3;
    /** 当前库存 */
    private BigDecimal currentNumber;
    /** 库存金额 */
    private BigDecimal currentStockPrice;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialExtendId() {
        return materialExtendId;
    }

    public void setMaterialExtendId(Long materialExtendId) {
        this.materialExtendId = materialExtendId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public BigDecimal getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(BigDecimal currentNumber) {
        this.currentNumber = currentNumber;
    }

    public BigDecimal getCurrentStockPrice() {
        return currentStockPrice;
    }

    public void setCurrentStockPrice(BigDecimal currentStockPrice) {
        this.currentStockPrice = currentStockPrice;
    }
}
