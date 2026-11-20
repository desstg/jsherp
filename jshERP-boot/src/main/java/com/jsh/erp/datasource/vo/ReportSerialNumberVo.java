package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-序列号统计
 * 对应 App 页面 pages_report/plugins/serialNumberStatistics.vue
 */
public class ReportSerialNumberVo {

    private Long id;
    private Long materialId;
    /** 商品名称 */
    private String materialName;
    /** 条码 */
    private String barCode;
    /** 规格型号 */
    private String materialModel;
    /** 仓库名称 */
    private String depotName;
    /** 序列号 */
    private String serialNumber;
    /** 是否卖出 */
    private String isSell;
    /** 入库价 */
    private BigDecimal inPrice;
    /** 入库单据号 */
    private String inBillNo;
    /** 出库单据号 */
    private String outBillNo;
    /** 备注 */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public String getInBillNo() {
        return inBillNo;
    }

    public void setInBillNo(String inBillNo) {
        this.inBillNo = inBillNo;
    }

    public String getOutBillNo() {
        return outBillNo;
    }

    public void setOutBillNo(String outBillNo) {
        this.outBillNo = outBillNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
