package com.api.utils;

import com.api.entity.Case;
import com.api.entity.Interfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InterfaceUtil {
    //把interface页签中的每一行对象保存到interfaceList集合中
    public static List<Interfaces> interfacesList = new ArrayList<Interfaces>();

    static {
        ExcelUtil.load("D:\\javaApi\\apiAuto\\src\\main\\resources\\cases.xlsx", "interface", Interfaces.class);
    }

    //通过apiId获取接口的请求url
    public static String getUrlByApiId(String apiId) {
        for (Interfaces interfaces : interfacesList) {
            if (interfaces.getApiId().equals(apiId)) {
                return interfaces.getUrl();
            }
        }
        return "";
    }

    //通过apiId获取接口的请求类型
    public static String getTypeByApiId(String apiId) {
        for (Interfaces interfaces : interfacesList) {
            if (interfaces.getApiId().equals(apiId)) {
                return interfaces.getType();
            }
        }
        return "";
    }

    /**
     * @param apiId 指定的接口编号
     * @param name  传入的列名
     * @return 根据接口编号获取对应的接口依赖字段和依赖值，被依赖字段
     */
    public static String getInterfaceDataByApiId(String apiId, String name) {
        Class<Interfaces> interfacesClass = Interfaces.class;
        //保存指定接口编号的对象集合
        ArrayList<Interfaces> interfacesArrayList = new ArrayList<Interfaces>();
        //通过循环找出指定接口编号对应的用例数据
        for (Interfaces ifs : InterfaceUtil.interfacesList) {
            if (ifs.getApiId().equals(apiId)) {
                interfacesArrayList.add(ifs);
            }
        }
        for (Interfaces interfaces : interfacesArrayList) {
            //循环获取interface对象
            Method methods = null;
            Method[] declaredMethods = interfacesClass.getDeclaredMethods();
            //通过列名获取方法名
            String methodName = "get" + name;
            for (Method declaredMethod : declaredMethods) {
                //通过反射拿到的方法名
                String refName = declaredMethod.getName();
                if (refName.equals(methodName)) {
                    try {
                        methods = interfacesClass.getMethod(declaredMethod.getName());
                        return (String) methods.invoke(interfaces);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


}
