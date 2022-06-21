package com.api.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ParameterUtil {
    public static List<String> paramList() {
        Class<FakerUtil> parameterUtilClass = FakerUtil.class;
        Method[] methods = parameterUtilClass.getDeclaredMethods();
        List<String> methodList = new ArrayList<String>();
        for (Method method : methods) {
            String strMethod = "${" + method.getName() + "}";
            methodList.add(strMethod);
        }
        return methodList;
    }

    /**
     * @param methodName 方法名称,如“getName”
     * @return 通过输入方法名，返回该方法得随机结果
     */
    public static String getParam(String methodName) {
        for (String method : paramList()) {
            int len = method.length();
            //去掉方法前后得符号，例，将${getName}变成getName
            String newMethod = method.substring(2, len - 1);
            if (newMethod.equals(methodName)) {
                Class<FakerUtil> fakerUtilClass = FakerUtil.class;
                //通过传入的方法获取方法名称
                try {
                    Method classMethod = fakerUtilClass.getMethod(newMethod);
                    //通过反射调用
                    Object invoke = classMethod.invoke(fakerUtilClass);
                    return (String) invoke;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "该参数化函数未找到";
    }

}
