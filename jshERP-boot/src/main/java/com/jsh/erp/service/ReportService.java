package com.jsh.erp.service;

import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.mappers.ReportMapperEx;
import com.jsh.erp.datasource.vo.*;
import com.jsh.erp.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 高级报表服务。
 * 负责把 Mapper 的原始聚合结果加工成 App 端约定的结构。
 */
@Service
public class ReportService {

    private Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Resource
    private ReportMapperEx reportMapperEx;

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private String begin(String t) {
        return Tools.parseDayToTime(t, BusinessConstants.DAY_FIRST_TIME);
    }

    private String end(String t) {
        return Tools.parseDayToTime(t, BusinessConstants.DAY_LAST_TIME);
    }

    // ==================== 收支项明细 ====================

    public List<ReportItemDetailVo> getItemDetailList(String beginTime, String endTime, String billNo,
                                                     Long organId, Long creator, Long handsPersonId,
                                                     Long accountId, String type, Long inOutItemId,
                                                     String remark, Integer offset, Integer rows) {
        return reportMapperEx.selectItemDetailList(begin(beginTime), end(endTime),
                emptyToNull(billNo), organId, creator, handsPersonId, accountId,
                emptyToNull(type), inOutItemId, emptyToNull(remark), offset, rows);
    }

    public int countItemDetail(String beginTime, String endTime, String billNo, Long organId,
                               Long creator, Long handsPersonId, Long accountId, String type,
                               Long inOutItemId, String remark) {
        return reportMapperEx.countItemDetail(begin(beginTime), end(endTime),
                emptyToNull(billNo), organId, creator, handsPersonId, accountId,
                emptyToNull(type), inOutItemId, emptyToNull(remark));
    }

    // ==================== 收付款明细 ====================

    public List<ReportMoneyDetailVo> getMoneyDetailList(String beginTime, String endTime, String billNo,
                                                       String billNumber, Long organId, Long creator,
                                                       Long handsPersonId, Long accountId, String type,
                                                       String remark, Integer offset, Integer rows) {
        return reportMapperEx.selectMoneyDetailList(begin(beginTime), end(endTime),
                emptyToNull(billNo), emptyToNull(billNumber), organId, creator, handsPersonId,
                accountId, emptyToNull(type), emptyToNull(remark), offset, rows);
    }

    public int countMoneyDetail(String beginTime, String endTime, String billNo, String billNumber,
                                Long organId, Long creator, Long handsPersonId, Long accountId,
                                String type, String remark) {
        return reportMapperEx.countMoneyDetail(begin(beginTime), end(endTime),
                emptyToNull(billNo), emptyToNull(billNumber), organId, creator, handsPersonId,
                accountId, emptyToNull(type), emptyToNull(remark));
    }

    // ==================== 销售占比 ====================

    /**
     * 销售占比：每个商品的销售额占区间内总销售额的百分比。
     * 分母为 0 时占比按 0 处理，避免除零。
     */
    public List<ReportSaleRatioVo> getSaleRatioList(String beginTime, String endTime,
                                                    Long organizationId, Long categoryId,
                                                    Integer offset, Integer rows) {
        String b = begin(beginTime);
        String e = end(endTime);
        List<ReportSaleRatioVo> list = reportMapperEx.selectSaleRatioList(b, e, organizationId, categoryId, offset, rows);
        BigDecimal total = reportMapperEx.sumSaleAmount(b, e, organizationId, categoryId);
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        for (ReportSaleRatioVo vo : list) {
            BigDecimal price = vo.getAllPrice() == null ? BigDecimal.ZERO : vo.getAllPrice();
            if (total.compareTo(BigDecimal.ZERO) == 0) {
                vo.setRatio(BigDecimal.ZERO);
            } else {
                vo.setRatio(price.multiply(HUNDRED).divide(total, 2, RoundingMode.HALF_UP));
            }
            if (vo.getOperNumber() == null) {
                vo.setOperNumber(BigDecimal.ZERO);
            }
            if (vo.getAllPrice() == null) {
                vo.setAllPrice(BigDecimal.ZERO);
            }
        }
        return list;
    }

    public int countSaleRatio(String beginTime, String endTime, Long organizationId, Long categoryId) {
        return reportMapperEx.countSaleRatio(begin(beginTime), end(endTime), organizationId, categoryId);
    }

    // ==================== 销售排行 ====================

    public List<ReportSaleSortVo> getSaleSortList(String beginTime, String endTime, Long depotId,
                                                  Long organizationId, Long categoryId, Long creator,
                                                  String sortType, Integer offset, Integer rows) {
        return reportMapperEx.selectSaleSortList(begin(beginTime), end(endTime), depotId,
                organizationId, categoryId, creator, emptyToNull(sortType), offset, rows);
    }

    public int countSaleSort(String beginTime, String endTime, Long depotId, Long organizationId,
                             Long categoryId, Long creator) {
        return reportMapperEx.countSaleSort(begin(beginTime), end(endTime), depotId,
                organizationId, categoryId, creator);
    }

    // ==================== 销售利润 ====================

    public List<ReportSaleProfitVo> getSaleProfitList(String beginTime, String endTime, Long categoryId,
                                                      Long organId, Long depotId, Long organizationId,
                                                      String number, String linkNumber, String materialParam,
                                                      Integer offset, Integer rows) {
        return reportMapperEx.selectSaleProfitList(begin(beginTime), end(endTime), categoryId,
                organId, depotId, organizationId, emptyToNull(number), emptyToNull(linkNumber),
                emptyToNull(materialParam), offset, rows);
    }

    public int countSaleProfit(String beginTime, String endTime, Long categoryId, Long organId,
                               Long depotId, Long organizationId, String number, String linkNumber,
                               String materialParam) {
        return reportMapperEx.countSaleProfit(begin(beginTime), end(endTime), categoryId, organId,
                depotId, organizationId, emptyToNull(number), emptyToNull(linkNumber),
                emptyToNull(materialParam));
    }

    /** 利润合计：数量、利润 */
    public ReportSaleProfitVo sumSaleProfit(String beginTime, String endTime, Long categoryId,
                                            Long organId, Long depotId, Long organizationId,
                                            String number, String linkNumber, String materialParam) {
        ReportSaleProfitVo vo = reportMapperEx.sumSaleProfit(begin(beginTime), end(endTime),
                categoryId, organId, depotId, organizationId, emptyToNull(number),
                emptyToNull(linkNumber), emptyToNull(materialParam));
        if (vo == null) {
            vo = new ReportSaleProfitVo();
        }
        if (vo.getOperNumber() == null) {
            vo.setOperNumber(BigDecimal.ZERO);
        }
        if (vo.getProfitPrice() == null) {
            vo.setProfitPrice(BigDecimal.ZERO);
        }
        return vo;
    }

    // ==================== 利润报表 ====================

    /**
     * 利润报表：把分组小计映射成 App 需要的 income / payment 结构。
     * 收入侧 = 销售/零售/采购退货/其它入库/收款/预收款
     * 支出侧 = 采购/零售退货/销售退货/其它出库/付款/预付款
     */
    public ReportProfitVo getProfitReport(String beginTime, String endTime, Long organizationId) {
        String b = begin(beginTime);
        String e = end(endTime);
        ReportProfitVo vo = new ReportProfitVo();
        ReportProfitVo.Income income = vo.getIncome();
        ReportProfitVo.Payment payment = vo.getPayment();

        List<ReportProfitGroupVo> billGroups = reportMapperEx.selectProfitGroup(b, e, organizationId);
        for (ReportProfitGroupVo g : billGroups) {
            BigDecimal amt = g.getAmount() == null ? BigDecimal.ZERO : g.getAmount();
            String type = g.getGrpType() == null ? "" : g.getGrpType();
            String sub = g.getGrpSubType() == null ? "" : g.getGrpSubType();
            boolean prepaid = BusinessConstants.PAY_TYPE_PREPAID.equals(g.getGrpPayType());

            if (BusinessConstants.DEPOTHEAD_TYPE_IN.equals(type)) {
                // 入库侧：采购是支出，采购退货是收入，其它入库是收入
                if (BusinessConstants.SUB_TYPE_PURCHASE.equals(sub)) {
                    if (prepaid) {
                        payment.setAdvanceOut(payment.getAdvanceOut().add(amt));
                    } else {
                        payment.setPurchaseIn(payment.getPurchaseIn().add(amt));
                    }
                } else if (BusinessConstants.SUB_TYPE_PURCHASE_RETURN.equals(sub)) {
                    income.setPurchaseBack(income.getPurchaseBack().add(amt));
                } else if (BusinessConstants.SUB_TYPE_PURCHASE_ORDER.equals(sub)) {
                    payment.setPurchaseOrder(payment.getPurchaseOrder().add(amt));
                } else if (BusinessConstants.SUB_TYPE_OTHER.equals(sub)) {
                    income.setItemIn(income.getItemIn().add(amt));
                }
            } else if (BusinessConstants.DEPOTHEAD_TYPE_OUT.equals(type)) {
                // 出库侧：销售是收入，销售退货是支出，其它出库是支出
                if (BusinessConstants.SUB_TYPE_SALES.equals(sub)) {
                    income.setSaleOut(income.getSaleOut().add(amt));
                } else if (BusinessConstants.SUB_TYPE_SALES_ORDER.equals(sub)) {
                    income.setSaleOrder(income.getSaleOrder().add(amt));
                } else if (BusinessConstants.SUB_TYPE_SALES_RETURN.equals(sub)) {
                    payment.setSaleBack(payment.getSaleBack().add(amt));
                } else if (BusinessConstants.SUB_TYPE_RETAIL.equals(sub)) {
                    income.setRetailOut(income.getRetailOut().add(amt));
                } else if (BusinessConstants.SUB_TYPE_RETAIL_RETURN.equals(sub)) {
                    payment.setRetailBack(payment.getRetailBack().add(amt));
                } else if (BusinessConstants.SUB_TYPE_OTHER.equals(sub)) {
                    payment.setItemOut(payment.getItemOut().add(amt));
                }
            }
        }

        // 财务单据：收款计入收入，付款计入支出
        List<ReportProfitGroupVo> moneyGroups = reportMapperEx.selectProfitMoneyGroup(b, e, organizationId);
        for (ReportProfitGroupVo g : moneyGroups) {
            BigDecimal amt = g.getAmount() == null ? BigDecimal.ZERO : g.getAmount();
            String type = g.getGrpType() == null ? "" : g.getGrpType();
            if (BusinessConstants.TYPE_MONEY_IN.equals(type)) {
                income.setMoneyIn(income.getMoneyIn().add(amt));
            } else if (BusinessConstants.TYPE_MONEY_OUT.equals(type)) {
                payment.setMoneyOut(payment.getMoneyOut().add(amt));
            }
        }

        // 合计
        income.setTotal(income.getRetailOut()
                .add(income.getSaleOrder())
                .add(income.getSaleOut())
                .add(income.getPurchaseBack())
                .add(income.getStockCheckProfit())
                .add(income.getItemIn())
                .add(income.getMoneyIn())
                .add(income.getAdvanceIn()));

        payment.setTotal(payment.getRetailBack()
                .add(payment.getSaleBack())
                .add(payment.getPurchaseOrder())
                .add(payment.getPurchaseIn())
                .add(payment.getStockCheckLoss())
                .add(payment.getItemOut())
                .add(payment.getMoneyOut())
                .add(payment.getAdvanceOut()));

        vo.setProfit(income.getTotal().subtract(payment.getTotal()));
        return vo;
    }

    // ==================== 负库存统计 ====================

    public List<ReportMinusStockVo> getMinusStockList(Long depotId, Long categoryId, String barCode,
                                                      String name, String standard, String model,
                                                      Integer offset, Integer rows) {
        return reportMapperEx.selectMinusStockList(depotId, categoryId, emptyToNull(barCode),
                emptyToNull(name), emptyToNull(standard), emptyToNull(model), offset, rows);
    }

    public int countMinusStock(Long depotId, Long categoryId, String barCode, String name,
                               String standard, String model) {
        return reportMapperEx.countMinusStock(depotId, categoryId, emptyToNull(barCode),
                emptyToNull(name), emptyToNull(standard), emptyToNull(model));
    }

    // ==================== 多属性库存 ====================

    public List<ReportAttributeStockVo> getAttributeStockList(Long depotId, Long categoryId,
                                                              String materialParam, String brand, String mfrs,
                                                              Integer offset, Integer rows) {
        return reportMapperEx.selectAttributeStockList(depotId, categoryId,
                emptyToNull(materialParam), emptyToNull(brand), emptyToNull(mfrs), offset, rows);
    }

    public int countAttributeStock(Long depotId, Long categoryId, String materialParam,
                                   String brand, String mfrs) {
        return reportMapperEx.countAttributeStock(depotId, categoryId,
                emptyToNull(materialParam), emptyToNull(brand), emptyToNull(mfrs));
    }

    public ReportAttributeStockVo sumAttributeStock(Long depotId, Long categoryId, String materialParam,
                                                     String brand, String mfrs) {
        ReportAttributeStockVo vo = reportMapperEx.sumAttributeStock(depotId, categoryId,
                emptyToNull(materialParam), emptyToNull(brand), emptyToNull(mfrs));
        if (vo == null) {
            vo = new ReportAttributeStockVo();
        }
        if (vo.getCurrentNumber() == null) {
            vo.setCurrentNumber(BigDecimal.ZERO);
        }
        if (vo.getCurrentStockPrice() == null) {
            vo.setCurrentStockPrice(BigDecimal.ZERO);
        }
        return vo;
    }

    // ==================== 序列号统计 ====================

    public List<ReportSerialNumberVo> getSerialNumberList(Long categoryId, String serialNumber,
                                                          String materialParam, String isSell, Long depotId,
                                                          String billNo, Integer offset, Integer rows) {
        return reportMapperEx.selectSerialNumberList(categoryId, emptyToNull(serialNumber),
                emptyToNull(materialParam), isSell == null ? null : String.valueOf(isSell),
                depotId, emptyToNull(billNo), offset, rows);
    }

    public int countSerialNumber(Long categoryId, String serialNumber, String materialParam,
                                 String isSell, Long depotId, String billNo) {
        return reportMapperEx.countSerialNumber(categoryId, emptyToNull(serialNumber),
                emptyToNull(materialParam), isSell, depotId, emptyToNull(billNo));
    }

    public BigDecimal sumSerialNumberPrice(Long categoryId, String serialNumber, String materialParam,
                                           String isSell, Long depotId, String billNo) {
        BigDecimal v = reportMapperEx.sumSerialNumberPrice(categoryId, emptyToNull(serialNumber),
                emptyToNull(materialParam), isSell, depotId, emptyToNull(billNo));
        return v == null ? BigDecimal.ZERO : v;
    }

    // ==================== 批次库存 ====================

    public List<ReportBatchStockVo> getBatchStockList(Long categoryId, String materialParam, Long depotId,
                                                       Integer expireFlag, Integer offset, Integer rows) {
        return reportMapperEx.selectBatchStockList(categoryId, emptyToNull(materialParam),
                depotId, expireFlag, offset, rows);
    }

    public int countBatchStock(Long categoryId, String materialParam, Long depotId, Integer expireFlag) {
        return reportMapperEx.countBatchStock(categoryId, emptyToNull(materialParam), depotId, expireFlag);
    }

    public ReportBatchStockVo sumBatchStock(Long categoryId, String materialParam, Long depotId,
                                            Integer expireFlag) {
        ReportBatchStockVo vo = reportMapperEx.sumBatchStock(categoryId, emptyToNull(materialParam),
                depotId, expireFlag);
        if (vo == null) {
            vo = new ReportBatchStockVo();
        }
        if (vo.getOperNumber() == null) {
            vo.setOperNumber(BigDecimal.ZERO);
        }
        if (vo.getAllPrice() == null) {
            vo.setAllPrice(BigDecimal.ZERO);
        }
        return vo;
    }

    // ==================== 业绩提成 ====================

    public List<ReportCommisionVo> getCommisionList(String salesMan, String beginTime, String endTime,
                                                    Integer offset, Integer rows) {
        return reportMapperEx.selectCommisionList(emptyToNull(salesMan),
                begin(beginTime), end(endTime), offset, rows);
    }

    public int countCommision(String salesMan, String beginTime, String endTime) {
        return reportMapperEx.countCommision(emptyToNull(salesMan), begin(beginTime), end(endTime));
    }

    // ==================== 工具方法 ====================

    private String emptyToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
