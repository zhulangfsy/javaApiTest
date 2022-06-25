package com.api.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ParameterUtil {
    /**
     * 获取参数化函数的集合
     *
     * @return list集合中包含参数化的函数，如{'${getName}'...}
     */
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
     * @param methodName 方法名称,如“${getName}”
     * @return 通过输入方法名，返回该方法得随机结果
     */
    public static String getParam(String methodName) {
        for (String method : paramList()) {
            int len = method.length();
            //去掉方法前后得符号，例，将${getName}变成getName
            String newMethod = method.substring(2, len - 1);
            if (method.equals(methodName)) {
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

    /**
     * 参数替换
     * @param parameter 需要替换的map
     * @return 返回替换后的map
     */
    public static Map<String,String> replaceVariable(Map<String,String> parameter) {
        //拿到参数key集合
        Set<String> keySet = parameter.keySet();
        for (String nameSet : keySet) {
            //变量key集合拿到value
            String paramMethod = parameter.get(nameSet);
            for (String method : paramList()) {
                //如果测试数据中出现了变量名
                if (paramMethod.contains(method)) {
                    String replaceParameter = paramMethod.replace(method, getParam(method));
                    parameter.put(nameSet,replaceParameter);
                }
            }
        }
        return parameter;
    }

}
