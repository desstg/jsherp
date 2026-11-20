package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 利润报表中间结果：按 类型/子类型/付款方式 分组的小计。
 * 由 ReportService 汇总到 ReportProfitVo 的 income / payment 结构。
 */
public class ReportProfitGroupVo {

    /** 主类型：入库/出库/收款/付款 */
    private String grpType;
    /** 子类型：采购/销售/采购退货/销售退货/零售/其它 等 */
    private String grpSubType;
    /** 付款方式：现付/预付款/空 */
    private String grpPayType;
    /** 小计金额 */
    private BigDecimal amount;

    public String getGrpType() {
        return grpType;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }

    public String getGrpSubType() {
        return grpSubType;
    }

    public void setGrpSubType(String grpSubType) {
        this.grpSubType = grpSubType;
    }

    public String getGrpPayType() {
        return grpPayType;
    }

    public void setGrpPayType(String grpPayType) {
        this.grpPayType = grpPayType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
