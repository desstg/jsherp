package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 高级报表数据访问层。
 * 说明：租户隔离由 TenantConfig 全局 SQL 重写注入，此处不重复手写 tenant_id 条件，
 * 但表别名必须以 t/a/dh 等标准前缀声明，保证拦截器能正确解析。
 */
public interface ReportMapperEx {

    // ==================== 收支项明细 ====================

    List<ReportItemDetailVo> selectItemDetailList(@Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("billNo") String billNo,
                                                  @Param("organId") Long organId,
                                                  @Param("creator") Long creator,
                                                  @Param("handsPersonId") Long handsPersonId,
                                                  @Param("accountId") Long accountId,
                                                  @Param("type") String type,
                                                  @Param("inOutItemId") Long inOutItemId,
                                                  @Param("remark") String remark,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int countItemDetail(@Param("beginTime") String beginTime,
                        @Param("endTime") String endTime,
                        @Param("billNo") String billNo,
                        @Param("organId") Long organId,
                        @Param("creator") Long creator,
                        @Param("handsPersonId") Long handsPersonId,
                        @Param("accountId") Long accountId,
                        @Param("type") String type,
                        @Param("inOutItemId") Long inOutItemId,
                        @Param("remark") String remark);

    // ==================== 收付款明细 ====================

    List<ReportMoneyDetailVo> selectMoneyDetailList(@Param("beginTime") String beginTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("billNo") String billNo,
                                                    @Param("billNumber") String billNumber,
                                                    @Param("organId") Long organId,
                                                    @Param("creator") Long creator,
                                                    @Param("handsPersonId") Long handsPersonId,
                                                    @Param("accountId") Long accountId,
                                                    @Param("type") String type,
                                                    @Param("remark") String remark,
                                                    @Param("offset") Integer offset,
                                                    @Param("rows") Integer rows);

    int countMoneyDetail(@Param("beginTime") String beginTime,
                         @Param("endTime") String endTime,
                         @Param("billNo") String billNo,
                         @Param("billNumber") String billNumber,
                         @Param("organId") Long organId,
                         @Param("creator") Long creator,
                         @Param("handsPersonId") Long handsPersonId,
                         @Param("accountId") Long accountId,
                         @Param("type") String type,
                         @Param("remark") String remark);

    // ==================== 销售占比 ====================

    List<ReportSaleRatioVo> selectSaleRatioList(@Param("beginTime") String beginTime,
                                                @Param("endTime") String endTime,
                                                @Param("organizationId") Long organizationId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("offset") Integer offset,
                                                @Param("rows") Integer rows);

    int countSaleRatio(@Param("beginTime") String beginTime,
                       @Param("endTime") String endTime,
                       @Param("organizationId") Long organizationId,
                       @Param("categoryId") Long categoryId);

    /** 销售占比分母：区间内销售总额 */
    BigDecimal sumSaleAmount(@Param("beginTime") String beginTime,
                             @Param("endTime") String endTime,
                             @Param("organizationId") Long organizationId,
                             @Param("categoryId") Long categoryId);

    // ==================== 销售排行 ====================

    List<ReportSaleSortVo> selectSaleSortList(@Param("beginTime") String beginTime,
                                              @Param("endTime") String endTime,
                                              @Param("depotId") Long depotId,
                                              @Param("organizationId") Long organizationId,
                                              @Param("categoryId") Long categoryId,
                                              @Param("creator") Long creator,
                                              @Param("sortType") String sortType,
                                              @Param("offset") Integer offset,
                                              @Param("rows") Integer rows);

    int countSaleSort(@Param("beginTime") String beginTime,
                      @Param("endTime") String endTime,
                      @Param("depotId") Long depotId,
                      @Param("organizationId") Long organizationId,
                      @Param("categoryId") Long categoryId,
                      @Param("creator") Long creator);

    // ==================== 销售利润 ====================

    List<ReportSaleProfitVo> selectSaleProfitList(@Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("categoryId") Long categoryId,
                                                  @Param("organId") Long organId,
                                                  @Param("depotId") Long depotId,
                                                  @Param("organizationId") Long organizationId,
                                                  @Param("number") String number,
                                                  @Param("linkNumber") String linkNumber,
                                                  @Param("materialParam") String materialParam,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int countSaleProfit(@Param("beginTime") String beginTime,
                        @Param("endTime") String endTime,
                        @Param("categoryId") Long categoryId,
                        @Param("organId") Long organId,
                        @Param("depotId") Long depotId,
                        @Param("organizationId") Long organizationId,
                        @Param("number") String number,
                        @Param("linkNumber") String linkNumber,
                        @Param("materialParam") String materialParam);

    /** 销售利润合计（数量、利润） */
    ReportSaleProfitVo sumSaleProfit(@Param("beginTime") String beginTime,
                                     @Param("endTime") String endTime,
                                     @Param("categoryId") Long categoryId,
                                     @Param("organId") Long organId,
                                     @Param("depotId") Long depotId,
                                     @Param("organizationId") Long organizationId,
                                     @Param("number") String number,
                                     @Param("linkNumber") String linkNumber,
                                     @Param("materialParam") String materialParam);

    // ==================== 利润报表 ====================

    /** 按 type/sub_type/pay_type 汇总单据金额 */
    List<ReportProfitGroupVo> selectProfitGroup(@Param("beginTime") String beginTime,
                                                @Param("endTime") String endTime,
                                                @Param("organizationId") Long organizationId);

    /** 按财务单据类型汇总（收款/付款） */
    List<ReportProfitGroupVo> selectProfitMoneyGroup(@Param("beginTime") String beginTime,
                                                     @Param("endTime") String endTime,
                                                     @Param("organizationId") Long organizationId);

    // ==================== 负库存统计 ====================

    List<ReportMinusStockVo> selectMinusStockList(@Param("depotId") Long depotId,
                                                  @Param("categoryId") Long categoryId,
                                                  @Param("barCode") String barCode,
                                                  @Param("name") String name,
                                                  @Param("standard") String standard,
                                                  @Param("model") String model,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int countMinusStock(@Param("depotId") Long depotId,
                        @Param("categoryId") Long categoryId,
                        @Param("barCode") String barCode,
                        @Param("name") String name,
                        @Param("standard") String standard,
                        @Param("model") String model);

    // ==================== 多属性库存 ====================

    List<ReportAttributeStockVo> selectAttributeStockList(@Param("depotId") Long depotId,
                                                          @Param("categoryId") Long categoryId,
                                                          @Param("materialParam") String materialParam,
                                                          @Param("brand") String brand,
                                                          @Param("mfrs") String mfrs,
                                                          @Param("offset") Integer offset,
                                                          @Param("rows") Integer rows);

    int countAttributeStock(@Param("depotId") Long depotId,
                            @Param("categoryId") Long categoryId,
                            @Param("materialParam") String materialParam,
                            @Param("brand") String brand,
                            @Param("mfrs") String mfrs);

    /** 多属性库存汇总 */
    ReportAttributeStockVo sumAttributeStock(@Param("depotId") Long depotId,
                                             @Param("categoryId") Long categoryId,
                                             @Param("materialParam") String materialParam,
                                             @Param("brand") String brand,
                                             @Param("mfrs") String mfrs);

    // ==================== 序列号统计 ====================

    List<ReportSerialNumberVo> selectSerialNumberList(@Param("categoryId") Long categoryId,
                                                      @Param("serialNumber") String serialNumber,
                                                      @Param("materialParam") String materialParam,
                                                      @Param("isSell") String isSell,
                                                      @Param("depotId") Long depotId,
                                                      @Param("billNo") String billNo,
                                                      @Param("offset") Integer offset,
                                                      @Param("rows") Integer rows);

    int countSerialNumber(@Param("categoryId") Long categoryId,
                          @Param("serialNumber") String serialNumber,
                          @Param("materialParam") String materialParam,
                          @Param("isSell") String isSell,
                          @Param("depotId") Long depotId,
                          @Param("billNo") String billNo);

    BigDecimal sumSerialNumberPrice(@Param("categoryId") Long categoryId,
                                    @Param("serialNumber") String serialNumber,
                                    @Param("materialParam") String materialParam,
                                    @Param("isSell") String isSell,
                                    @Param("depotId") Long depotId,
                                    @Param("billNo") String billNo);

    // ==================== 批次库存 ====================

    List<ReportBatchStockVo> selectBatchStockList(@Param("categoryId") Long categoryId,
                                                  @Param("materialParam") String materialParam,
                                                  @Param("depotId") Long depotId,
                                                  @Param("expireFlag") Integer expireFlag,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int countBatchStock(@Param("categoryId") Long categoryId,
                        @Param("materialParam") String materialParam,
                        @Param("depotId") Long depotId,
                        @Param("expireFlag") Integer expireFlag);

    ReportBatchStockVo sumBatchStock(@Param("categoryId") Long categoryId,
                                     @Param("materialParam") String materialParam,
                                     @Param("depotId") Long depotId,
                                     @Param("expireFlag") Integer expireFlag);

    // ==================== 业绩提成 ====================

    List<ReportCommisionVo> selectCommisionList(@Param("salesMan") String salesMan,
                                                @Param("beginTime") String beginTime,
                                                @Param("endTime") String endTime,
                                                @Param("offset") Integer offset,
                                                @Param("rows") Integer rows);

    int countCommision(@Param("salesMan") String salesMan,
                       @Param("beginTime") String beginTime,
                       @Param("endTime") String endTime);
}
