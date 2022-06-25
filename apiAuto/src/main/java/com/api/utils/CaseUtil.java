package com.api.utils;

import com.alibaba.fastjson.JSONObject;
import com.api.entity.Case;
import com.api.entity.Interfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaseUtil {
    // 保存所有的用例对象
    public static List<Case> cases = new ArrayList<Case>();

    static {
        //将所有用例数据解析封装到cases
        List<Case> list = ExcelUtil.load(PropertiesUtil.getExcelPath(), "case", Case.class);
        cases.addAll(list);
    }

    /**
     * @param apiId     指定的接口编号
     * @param cellNames 要获取的数据对应的列名
     * @return 根据接口编号获取对应的用例数据
     */
    public static Object[][] getCaseDataByApiId(String apiId, String[] cellNames) {
        Class<Case> caseClass = Case.class;
        //保存指定接口编号的对象集合
        ArrayList<Case> caseArrayList = new ArrayList<Case>();
        //通过循环找出指定接口编号对应的用例数据
        for (Case cs : CaseUtil.cases) {
            if (cs.getApiId().equals(apiId)) {
                caseArrayList.add(cs);
            }
        }
        Object[][] data = new Object[caseArrayList.size()][cellNames.length];
        for (int i = 0; i < caseArrayList.size(); i++) {
            //循环获取case对象
            Case aCase = caseArrayList.get(i);
            for (int j = 0; j < cellNames.length; j++) {
                String methodName = "get" + cellNames[j];
                //获取要反射的方法对象
                Method methods = null;
                try {
                    methods = caseClass.getMethod(methodName);
                    String value = (String) methods.invoke(aCase);
                    data[i][j] = value;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 处理依赖的接口及返回值
     *
     * @param apiId     依赖的caseId
     * @param cellNames 参数列所在的列名
     * @return 接口有依赖就返回含有依赖的请求参数，没有依赖就直接返回参数
     */
    public static Map<String, String> getCaseDependDataByApiId(String apiId, String[] cellNames) {
        List<Interfaces> interfaces = InterfaceUtil.interfacesList;
        Map<String, String> requestParam = null;
        for (Interfaces face : interfaces) {
            //dependApiId:依赖于哪个接口
            String dependApiId = InterfaceUtil.getInterfaceDataByApiId(apiId, "DependApiId");
            //参数有依赖的情况
            if (dependApiId != null) {
                //获取被依赖的url
                String url = InterfaceUtil.getUrlByApiId(dependApiId);
                //获取被依赖的type
                String type = InterfaceUtil.getTypeByApiId(dependApiId);
                // dependField:依赖的字段值
                String dependField = face.getDependField();
                //获取被依赖的值,为了之后给该字段赋值
                String beDependField = face.getBeDependField();
                //获取被依赖的接口的请求参数
                Object[][] data = CaseUtil.getCaseDataByApiId(dependApiId, cellNames);
                for (Object[] objects : data) {
                    Map<String, String> param = (Map<String, String>) JSONObject.parse((String) (objects[1]));
                    //调用接口,返回数据,返回的数据为string类型
                    String actualResponseData = HttpUtil.doService(url, type, param);
                    //将响应数据转换成json格式
                    JSONObject jsonObject = JSONObject.parseObject(actualResponseData);
                    //取出响应数据中依赖的字段和值
                    String dependValue = jsonObject.getString(dependField);
                    requestParam = (Map<String, String>) JSONObject.parse((String) (objects[1]));
                    //将依赖的字段和值添加到参数中
                    requestParam.put(beDependField, dependValue);
                }
            }
            // 参数没有依赖的情况
            else {
                //获取没有依赖的接口的请求参数
                Object[][] data = CaseUtil.getCaseDataByApiId(apiId, cellNames);
                for (Object[] objects : data) {
                    requestParam = (Map<String, String>) JSONObject.parse((String) (objects[1]));
                }
            }
        }
        return requestParam;
    }
}
