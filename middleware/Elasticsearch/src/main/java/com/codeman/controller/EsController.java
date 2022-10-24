package com.codeman.controller;

import com.codeman.common.util.CommonResult;
import com.codeman.entity.EsProduct;
import com.codeman.service.EsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2021/12/06 21:15:56
 */
@Api(tags = "查询所有商品")
@RestController
@RequestMapping("/esProduct")
public class EsController {
    @Resource
    private EsService esService;

    @ApiOperation("导入")
    @GetMapping("/importAll")
    public CommonResult<String> importAll() {
        int count = esService.importAll();
        return CommonResult.success("导入数量为：" + count);
    }

    @ApiOperation("ik分词器查询")
    @GetMapping("/selectMatch") // 张
    public CommonResult<List<EsProduct>> selectMatch(@RequestParam String value) throws IOException {
        List<EsProduct> esProducts = esService.selectMatch("name", value);
        return CommonResult.success(esProducts);
    }

    @ApiOperation("search")
    @GetMapping("/search")
    public CommonResult<String> selectMatch() throws IOException {
        esService.search();
        return CommonResult.success("successful");
    }
}
