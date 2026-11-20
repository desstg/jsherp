package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;

/**
 * 高级报表-利润报表
 * 对应 App 页面 pages_report/plugins/profitReport.vue
 * 结构：{ profit, income:{...}, payment:{...} }
 */
public class ReportProfitVo {

    /** 利润 = 收入合计 - 支出合计 */
    private BigDecimal profit;
    private Income income = new Income();
    private Payment payment = new Payment();

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /** 收入项 */
    public static class Income {
        /** 收入合计 */
        private BigDecimal total = BigDecimal.ZERO;
        /** 零售 */
        private BigDecimal retailOut = BigDecimal.ZERO;
        /** 销售订单 */
        private BigDecimal saleOrder = BigDecimal.ZERO;
        /** 销售 */
        private BigDecimal saleOut = BigDecimal.ZERO;
        /** 采购退货 */
        private BigDecimal purchaseBack = BigDecimal.ZERO;
        /** 盘点盈亏（盈） */
        private BigDecimal stockCheckProfit = BigDecimal.ZERO;
        /** 其它入库 */
        private BigDecimal itemIn = BigDecimal.ZERO;
        /** 收款 */
        private BigDecimal moneyIn = BigDecimal.ZERO;
        /** 预收款 */
        private BigDecimal advanceIn = BigDecimal.ZERO;

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getRetailOut() {
            return retailOut;
        }

        public void setRetailOut(BigDecimal retailOut) {
            this.retailOut = retailOut;
        }

        public BigDecimal getSaleOrder() {
            return saleOrder;
        }

        public void setSaleOrder(BigDecimal saleOrder) {
            this.saleOrder = saleOrder;
        }

        public BigDecimal getSaleOut() {
            return saleOut;
        }

        public void setSaleOut(BigDecimal saleOut) {
            this.saleOut = saleOut;
        }

        public BigDecimal getPurchaseBack() {
            return purchaseBack;
        }

        public void setPurchaseBack(BigDecimal purchaseBack) {
            this.purchaseBack = purchaseBack;
        }

        public BigDecimal getStockCheckProfit() {
            return stockCheckProfit;
        }

        public void setStockCheckProfit(BigDecimal stockCheckProfit) {
            this.stockCheckProfit = stockCheckProfit;
        }

        public BigDecimal getItemIn() {
            return itemIn;
        }

        public void setItemIn(BigDecimal itemIn) {
            this.itemIn = itemIn;
        }

        public BigDecimal getMoneyIn() {
            return moneyIn;
        }

        public void setMoneyIn(BigDecimal moneyIn) {
            this.moneyIn = moneyIn;
        }

        public BigDecimal getAdvanceIn() {
            return advanceIn;
        }

        public void setAdvanceIn(BigDecimal advanceIn) {
            this.advanceIn = advanceIn;
        }
    }

    /** 支出项 */
    public static class Payment {
        /** 支出合计 */
        private BigDecimal total = BigDecimal.ZERO;
        /** 零售退货 */
        private BigDecimal retailBack = BigDecimal.ZERO;
        /** 销售退货 */
        private BigDecimal saleBack = BigDecimal.ZERO;
        /** 采购订单 */
        private BigDecimal purchaseOrder = BigDecimal.ZERO;
        /** 采购 */
        private BigDecimal purchaseIn = BigDecimal.ZERO;
        /** 盘点盈亏（亏） */
        private BigDecimal stockCheckLoss = BigDecimal.ZERO;
        /** 其它出库 */
        private BigDecimal itemOut = BigDecimal.ZERO;
        /** 付款 */
        private BigDecimal moneyOut = BigDecimal.ZERO;
        /** 预付款 */
        private BigDecimal advanceOut = BigDecimal.ZERO;

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getRetailBack() {
            return retailBack;
        }

        public void setRetailBack(BigDecimal retailBack) {
            this.retailBack = retailBack;
        }

        public BigDecimal getSaleBack() {
            return saleBack;
        }

        public void setSaleBack(BigDecimal saleBack) {
            this.saleBack = saleBack;
        }

        public BigDecimal getPurchaseOrder() {
            return purchaseOrder;
        }

        public void setPurchaseOrder(BigDecimal purchaseOrder) {
            this.purchaseOrder = purchaseOrder;
        }

        public BigDecimal getPurchaseIn() {
            return purchaseIn;
        }

        public void setPurchaseIn(BigDecimal purchaseIn) {
            this.purchaseIn = purchaseIn;
        }

        public BigDecimal getStockCheckLoss() {
            return stockCheckLoss;
        }

        public void setStockCheckLoss(BigDecimal stockCheckLoss) {
            this.stockCheckLoss = stockCheckLoss;
        }

        public BigDecimal getItemOut() {
            return itemOut;
        }

        public void setItemOut(BigDecimal itemOut) {
            this.itemOut = itemOut;
        }

        public BigDecimal getMoneyOut() {
            return moneyOut;
        }

        public void setMoneyOut(BigDecimal moneyOut) {
            this.moneyOut = moneyOut;
        }

        public BigDecimal getAdvanceOut() {
            return advanceOut;
        }

        public void setAdvanceOut(BigDecimal advanceOut) {
            this.advanceOut = advanceOut;
        }
    }
}
