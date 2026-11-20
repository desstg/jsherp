package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-批次库存
 * 对应 App 页面 pages_report/plugins/batchStock.vue
 */
public class ReportBatchStockVo {

    private Long materialId;
    /** 商品名称 */
    private String materialName;
    /** 条码 */
    private String barCode;
    /** 规格型号 */
    private String materialModel;
    /** 仓库名称 */
    private String depotName;
    /** 批号 */
    private String batchNumber;
    /** 到期日 */
    private String expirationDate;
    /** 数量 */
    private BigDecimal operNumber;
    /** 金额 */
    private BigDecimal allPrice;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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
}
