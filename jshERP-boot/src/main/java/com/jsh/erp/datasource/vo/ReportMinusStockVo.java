package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-负库存统计
 * 对应 App 页面 pages_report/plugins/minusStockStatistics.vue
 */
public class ReportMinusStockVo {

    private Long materialId;
    /** 仓库名称 */
    private String depotName;
    /** 商品名称 */
    private String materialName;
    /** 规格型号 */
    private String materialModel;
    /** 条码 */
    private String barCode;
    /** 单位 */
    private String materialUnit;
    /** 库存数量（负数） */
    private BigDecimal currentNumber;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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

    public BigDecimal getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(BigDecimal currentNumber) {
        this.currentNumber = currentNumber;
    }
}
