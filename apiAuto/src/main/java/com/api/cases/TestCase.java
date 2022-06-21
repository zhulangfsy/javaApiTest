package com.api.cases;


import com.api.entity.Interfaces;
import com.api.entity.WriteBackData;
import com.api.utils.CaseUtil;
import com.api.utils.ExcelUtil;
import com.api.utils.HttpUtil;
import com.api.utils.InterfaceUtil;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestCase {
    @Test
    public static void test() {
        String[] cellNames = {"CaseId", "Params"};
        List<Interfaces> interfacesList = InterfaceUtil.interfacesList;
        for (Interfaces ifs : interfacesList) {
            //从interface中获取apiId
            String apiId = ifs.getApiId();
            //获取url
            String url = InterfaceUtil.getUrlByApiId(apiId);
            //获取type
            String type = InterfaceUtil.getTypeByApiId(apiId);
            //获取请求参数
            Object[][] data = CaseUtil.getCaseDataByApiId(apiId, cellNames);
            // 循环处理参数
            for (Object[] paramIndex : data) {
                //获取caseId
                String caseId = (String) paramIndex[0];
                Map<String, String> param = CaseUtil.getCaseDependDataByApiId(apiId,cellNames);
                System.out.println("map格式的字符串：" + param);
                //调用接口,返回数据
                String actualResponseData = HttpUtil.doService(url, type, param);
                String sheetName = "case";
                WriteBackData backData = new WriteBackData(sheetName, caseId, "ActualData", actualResponseData);
                //保存返回的数据对象
                ExcelUtil.backData.add(backData);
            }
        }
    }

    @AfterSuite
    public void batchWriteData() {
        String excelPath = "D:\\javaApi\\apiAuto\\src\\main\\resources\\cases.xlsx";
        ExcelUtil.batchWaiteBackData(excelPath);
    }
}
