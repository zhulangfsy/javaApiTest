// package com.api.casesList;
//
//
// import com.alibaba.fastjson.JSONObject;
// import com.api.entity.Case;
// import com.api.entity.WriteBackData;
// import com.api.utils.CaseUtil;
// import com.api.utils.ExcelUtil;
// import com.api.utils.HttpUtil;
// import com.api.utils.InterfaceUtil;
// import org.testng.annotations.DataProvider;
// import org.testng.annotations.Test;
//
// import java.util.List;
// import java.util.Map;
//
//
// public class RegisterCase extends TestCase {
//     @Test(dataProvider = "paramData")
//     public void test1(String caseId, String apiId, String parameter) {
//         //获取url
//         String url = InterfaceUtil.getUrlByApiId(apiId);
//         //获取type
//         String type = InterfaceUtil.getTypeByApiId(apiId);
//         //请求参数params
//         Map<String, String> params = (Map<String, String>) JSONObject.parse(parameter);
//         //调用接口,返回数据
//         String actualResponseData = HttpUtil.doService(url, type, params);
//         String sheetName = "case";
//         WriteBackData backData = new WriteBackData(sheetName, caseId, "ActualData", actualResponseData);
//         //保存返回的数据对象
//         ExcelUtil.backData.add(backData);
//         System.out.println(actualResponseData);
//     }
//     @DataProvider
//     public static Object[][] paramData() {
//         String[] cellNames = {"CaseId", "ApiId", "Params"};
//         Object[][] data = null;
//
//         List<Case> cases = CaseUtil.cases;
//         for (Case cs : cases) {
//             String apiId = cs.getApiId();
//             data = CaseUtil.getCaseDataByApiId(apiId, cellNames);
//         }
//         return data;
//     }
//
// }
//
