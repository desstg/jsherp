package com.jsh.erp.controller;

import com.jsh.erp.datasource.vo.*;
import com.jsh.erp.service.DepotService;
import com.jsh.erp.service.ReportService;
import com.jsh.erp.utils.BaseResponseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级报表接口。
 * <p>
 * 路径严格对齐 App 端 pages_report/plugins/* 的调用契约（/api/plugin/...），
 * 使手机端无需改动即可取到数据。返回结构与 App 解析逻辑一致：
 * { code:200, data:{ total, rows|..., 汇总字段 } }
 */
@RestController
@RequestMapping(value = "/api/plugin")
@Api(tags = {"高级报表"})
public class ReportPluginController {

    private Logger logger = LoggerFactory.getLogger(ReportPluginController.class);

    @Resource
    private ReportService reportService;

    // ==================== 收支项明细 ====================

    @GetMapping(value = "/item-detail/itemDetail/getList")
    @ApiOperation(value = "收支项明细")
    public BaseResponseInfo getItemDetailList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "billNo", required = false) String billNo,
            @RequestParam(value = "organId", required = false) Long organId,
            @RequestParam(value = "creator", required = false) Long creator,
            @RequestParam(value = "handsPersonId", required = false) Long handsPersonId,
            @RequestParam(value = "accountId", required = false) Long accountId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "inOutItemId", required = false) Long inOutItemId,
            @RequestParam(value = "remark", required = false) String remark,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            int total = reportService.countItemDetail(beginTime, endTime, billNo, organId, creator,
                    handsPersonId, accountId, type, inOutItemId, remark);
            int offset = (currentPage - 1) * pageSize;
            List<ReportItemDetailVo> rows = reportService.getItemDetailList(beginTime, endTime, billNo,
                    organId, creator, handsPersonId, accountId, type, inOutItemId, remark, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            logger.error("收支项明细查询失败", e);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 收付款明细 ====================

    @GetMapping(value = "/money-detail/moneyDetail/getList")
    @ApiOperation(value = "收付款明细")
    public BaseResponseInfo getMoneyDetailList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "billNo", required = false) String billNo,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "organId", required = false) Long organId,
            @RequestParam(value = "creator", required = false) Long creator,
            @RequestParam(value = "handsPersonId", required = false) Long handsPersonId,
            @RequestParam(value = "accountId", required = false) Long accountId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "remark", required = false) String remark,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            int total = reportService.countMoneyDetail(beginTime, endTime, billNo, billNumber, organId,
                    creator, handsPersonId, accountId, type, remark);
            int offset = (currentPage - 1) * pageSize;
            List<ReportMoneyDetailVo> rows = reportService.getMoneyDetailList(beginTime, endTime, billNo,
                    billNumber, organId, creator, handsPersonId, accountId, type, remark, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            logger.error("收付款明细查询失败", e);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 销售占比 ====================

    @GetMapping(value = "/sale-ratio-statistics/saleRatioStatistics/getList")
    @ApiOperation(value = "销售占比")
    public BaseResponseInfo getSaleRatioList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "search[beginTime]", required = false) String searchBeginTime,
            @RequestParam(value = "search[endTime]", required = false) String searchEndTime,
            @RequestParam(value = "search[organizationId]", required = false) String searchOrganizationId,
            @RequestParam(value = "search[categoryId]", required = false) String searchCategoryId,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "organizationId", required = false) Long organizationId,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            String b = firstNonEmpty(searchBeginTime, beginTime);
            String e = firstNonEmpty(searchEndTime, endTime);
            Long orgId = firstLong(searchOrganizationId, organizationId);
            Long catId = firstLong(searchCategoryId, categoryId);
            int total = reportService.countSaleRatio(b, e, orgId, catId);
            int offset = (currentPage - 1) * pageSize;
            List<ReportSaleRatioVo> rows = reportService.getSaleRatioList(b, e, orgId, catId, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("销售占比查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 销售排行 ====================

    @GetMapping(value = "/sale-sort/saleSort/getList")
    @ApiOperation(value = "销售排行")
    public BaseResponseInfo getSaleSortList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "search[depotId]", required = false) String searchDepotId,
            @RequestParam(value = "search[type]", required = false) String searchType,
            @RequestParam(value = "search[categoryId]", required = false) String searchCategoryId,
            @RequestParam(value = "search[creator]", required = false) String searchCreator,
            @RequestParam(value = "search[sortType]", required = false) String searchSortType,
            @RequestParam(value = "search[userId]", required = false) String searchUserId,
            @RequestParam(value = "search[organizationId]", required = false) String searchOrganizationId,
            @RequestParam(value = "search[beginTime]", required = false) String searchBeginTime,
            @RequestParam(value = "search[endTime]", required = false) String searchEndTime) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            Long depotId = toLong(searchDepotId);
            Long catId = toLong(searchCategoryId);
            Long creator = toLong(searchCreator);
            Long orgId = toLong(searchOrganizationId);
            int total = reportService.countSaleSort(searchBeginTime, searchEndTime, depotId, orgId, catId, creator);
            int offset = (currentPage - 1) * pageSize;
            List<ReportSaleSortVo> rows = reportService.getSaleSortList(searchBeginTime, searchEndTime,
                    depotId, orgId, catId, creator, searchSortType, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("销售排行查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 销售利润 ====================

    @GetMapping(value = "/sale-profit/saleProfit/getList")
    @ApiOperation(value = "销售利润")
    public BaseResponseInfo getSaleProfitList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "organId", required = false) String organId,
            @RequestParam(value = "depotId", required = false) String depotId,
            @RequestParam(value = "organizationId", required = false) String organizationId,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "linkNumber", required = false) String linkNumber,
            @RequestParam(value = "materialParam", required = false) String materialParam,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            Long catId = toLong(categoryId);
            Long orgId = toLong(organId);
            Long depId = toLong(depotId);
            Long orgzId = toLong(organizationId);
            int total = reportService.countSaleProfit(beginTime, endTime, catId, orgId, depId, orgzId,
                    number, linkNumber, materialParam);
            int offset = (currentPage - 1) * pageSize;
            List<ReportSaleProfitVo> rows = reportService.getSaleProfitList(beginTime, endTime, catId,
                    orgId, depId, orgzId, number, linkNumber, materialParam, offset, pageSize);
            ReportSaleProfitVo sum = reportService.sumSaleProfit(beginTime, endTime, catId, orgId,
                    depId, orgzId, number, linkNumber, materialParam);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            map.put("operNumber", sum.getOperNumber());
            map.put("profitPrice", sum.getProfitPrice());
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("销售利润查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 利润报表 ====================

    @GetMapping(value = "/profit-report/profitReport/getDetail")
    @ApiOperation(value = "利润报表")
    public BaseResponseInfo getProfitReport(
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "organizationId", required = false) Long organizationId) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            ReportProfitVo vo = reportService.getProfitReport(beginTime, endTime, organizationId);
            res.code = 200;
            res.data = vo;
        } catch (Exception e) {
            logger.error("利润报表查询失败", e);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 负库存统计 ====================

    @GetMapping(value = "/minus-stock-statistics/minusStockStatistics/getList")
    @ApiOperation(value = "负库存统计")
    public BaseResponseInfo getMinusStockList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "search[depotId]", required = false) String searchDepotId,
            @RequestParam(value = "search[categoryId]", required = false) String searchCategoryId,
            @RequestParam(value = "search[barCode]", required = false) String searchBarCode,
            @RequestParam(value = "search[name]", required = false) String searchName,
            @RequestParam(value = "search[standard]", required = false) String searchStandard,
            @RequestParam(value = "search[model]", required = false) String searchModel,
            @RequestParam(value = "depotId", required = false) String depotId,
            @RequestParam(value = "categoryId", required = false) String categoryId) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            Long depId = firstLong(searchDepotId, toLong(depotId));
            Long catId = firstLong(searchCategoryId, toLong(categoryId));
            int total = reportService.countMinusStock(depId, catId, searchBarCode, searchName,
                    searchStandard, searchModel);
            int offset = (currentPage - 1) * pageSize;
            List<ReportMinusStockVo> rows = reportService.getMinusStockList(depId, catId, searchBarCode,
                    searchName, searchStandard, searchModel, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("负库存统计查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 多属性库存 ====================

    @GetMapping(value = "/attribute-stock/attributeStock/getListWithAttributeStock")
    @ApiOperation(value = "多属性库存")
    public BaseResponseInfo getAttributeStockList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "depotId", required = false) Long depotId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "materialParam", required = false) String materialParam,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "mfrs", required = false) String mfrs) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            int total = reportService.countAttributeStock(depotId, categoryId, materialParam, brand, mfrs);
            int offset = (currentPage - 1) * pageSize;
            List<ReportAttributeStockVo> rows = reportService.getAttributeStockList(depotId, categoryId,
                    materialParam, brand, mfrs, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("多属性库存查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    @GetMapping(value = "/attribute-stock/attributeStock/totalCountMoney")
    @ApiOperation(value = "多属性库存合计")
    public BaseResponseInfo getAttributeStockTotal(
            @RequestParam(value = "depotId", required = false) Long depotId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "materialParam", required = false) String materialParam,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "mfrs", required = false) String mfrs) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            ReportAttributeStockVo vo = reportService.sumAttributeStock(depotId, categoryId,
                    materialParam, brand, mfrs);
            map.put("totalSkuStock", vo.getCurrentNumber());
            map.put("totalSkuPrice", vo.getCurrentStockPrice());
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("多属性库存合计查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 序列号统计 ====================

    @GetMapping(value = "/serial-number-statistics/serialNumberStatistics/getList")
    @ApiOperation(value = "序列号统计")
    public BaseResponseInfo getSerialNumberList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "serialNumber", required = false) String serialNumber,
            @RequestParam(value = "materialParam", required = false) String materialParam,
            @RequestParam(value = "isSell", required = false) String isSell,
            @RequestParam(value = "depotId", required = false) Long depotId,
            @RequestParam(value = "billNo", required = false) String billNo) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            String sell = (isSell == null || isSell.trim().isEmpty()) ? null : isSell.trim();
            int total = reportService.countSerialNumber(categoryId, serialNumber, materialParam,
                    sell, depotId, billNo);
            int offset = (currentPage - 1) * pageSize;
            List<ReportSerialNumberVo> rows = reportService.getSerialNumberList(categoryId, serialNumber,
                    materialParam, sell, depotId, billNo, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            map.put("inPriceTotal", reportService.sumSerialNumberPrice(categoryId, serialNumber,
                    materialParam, sell, depotId, billNo));
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("序列号统计查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 批次库存 ====================

    @GetMapping(value = "/batch-stock/batchStock/getList")
    @ApiOperation(value = "批次库存")
    public BaseResponseInfo getBatchStockList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "materialParam", required = false) String materialParam,
            @RequestParam(value = "depotId", required = false) Long depotId,
            @RequestParam(value = "expireFlag", required = false) Integer expireFlag) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            int total = reportService.countBatchStock(categoryId, materialParam, depotId, expireFlag);
            int offset = (currentPage - 1) * pageSize;
            List<ReportBatchStockVo> rows = reportService.getBatchStockList(categoryId, materialParam,
                    depotId, expireFlag, offset, pageSize);
            ReportBatchStockVo sum = reportService.sumBatchStock(categoryId, materialParam, depotId, expireFlag);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            map.put("totalNum", sum.getOperNumber());
            map.put("totalPrice", sum.getAllPrice());
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("批次库存查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 业绩提成 ====================

    @GetMapping(value = "/commision/commision/getSaleBill")
    @ApiOperation(value = "业绩提成")
    public BaseResponseInfo getCommisionList(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "search[salesMan]", required = false) String searchSalesMan,
            @RequestParam(value = "search[beginTime]", required = false) String searchBeginTime,
            @RequestParam(value = "search[endTime]", required = false) String searchEndTime) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            int total = reportService.countCommision(searchSalesMan, searchBeginTime, searchEndTime);
            int offset = (currentPage - 1) * pageSize;
            List<ReportCommisionVo> rows = reportService.getCommisionList(searchSalesMan,
                    searchBeginTime, searchEndTime, offset, pageSize);
            map.put("total", total);
            map.put("rows", rows == null ? new ArrayList<>() : rows);
            res.code = 200;
            res.data = map;
        } catch (Exception ex) {
            logger.error("业绩提成查询失败", ex);
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    // ==================== 参数工具 ====================

    private String firstNonEmpty(String a, String b) {
        if (a != null && !a.trim().isEmpty()) {
            return a.trim();
        }
        return b;
    }

    private Long firstLong(String s, Long fallback) {
        Long v = toLong(s);
        return v != null ? v : fallback;
    }

    private Long toLong(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
