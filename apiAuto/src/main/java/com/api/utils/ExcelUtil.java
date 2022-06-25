package com.api.utils;


import com.api.entity.Case;
import com.api.entity.Interfaces;
import com.api.entity.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelUtil {
    //保存caseId和行号的对应关系
    public static Map<String, Integer> caseIdAndRowNum = new HashMap<String, Integer>();
    //保存列名和列号的对应关系
    public static Map<String, Integer> cellNameAndCellNum = new HashMap<String, Integer>();
    //定义集合,将接口返回的数据保存到集合,再一次性回写到excel中
    public static List<WriteBackData> backData = new ArrayList<WriteBackData>();

    static {
        loadRowNumAndCellNum(PropertiesUtil.getExcelPath(), "case");
    }

    /**
     * 获取caseId以及他所对应的行索引，获取cellName以及他对应的列索引
     *
     * @param excelPath 文件路径
     * @param sheetName sheet页签
     */
    public static void loadRowNumAndCellNum(String excelPath, String sheetName) {
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(excelPath));
            Sheet sheet = workbook.getSheet(sheetName);
            //获取标题行
            Row titleRow = sheet.getRow(0);
            short lastCellIndex = titleRow.getLastCellNum();
            //循环处理标题行，处理每一列,即标题行的每一个单元格
            for (int i = 0; i < lastCellIndex; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                // title表示标题行的每一个单元格的值
                String title = new DataFormatter().formatCellValue(cell);
                int column = cell.getAddress().getColumn();
                cellNameAndCellNum.put(title, column);
            }
            //从第二行开始，循环获取所有的数据行
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                Row dataRow = sheet.getRow(i);
                Cell firstCell = dataRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String caseId = new DataFormatter().formatCellValue(firstCell);
                int rowNum = dataRow.getRowNum();
                caseIdAndRowNum.put(caseId, rowNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析指定的excel用例，封装为对象
     * @param excelPath 文件路径
     * @param sheetName sheet页签
     * @param classType 类型字节码
     */
    public static <T> List<T> load(String excelPath, String sheetName, Class<T> classType) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            //创建一个workbook对象
            Workbook workbook = WorkbookFactory.create(new File(excelPath));
            //获取对应的sheet页签
            Sheet sheet = workbook.getSheet(sheetName);
            //获取case页签标题行
            Row titleRow = sheet.getRow(0);
            //获取最后一列的列号
            short lastCellNum = titleRow.getLastCellNum();
            String[] fields = new String[lastCellNum];
            //循环处理标题行的每一列,保存到数据
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //获取标题行的每一列的值
                String title = new DataFormatter().formatCellValue(cell);
                //每一列的值保存到数组
                fields[i] = title;
            }

            int lastRowIndex = sheet.getLastRowNum();
            //循环取出每一行数据,除标题行外
            for (int i = 1; i <= lastRowIndex; i++) {
                //创建实例对象
                T obj = classType.newInstance();
                Row dataRow = sheet.getRow(i);
                if (dataRow == null || isEmptyRow(dataRow)) {
                    continue;
                }
                //取出每一行中的每一列的值，封装成对象
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = new DataFormatter().formatCellValue(cell);
                    // //获取反射的方法名
                    String methodName = "set" + fields[j];
                    Method method = classType.getMethod(methodName, String.class);
                    method.invoke(obj, value);
                }
                list.add(obj);
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean isEmptyRow(Row dataRow) {
        short lastCellNum = dataRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = dataRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String value = new DataFormatter().formatCellValue(cell);
            if (value != null && value.trim().length() > 0) {
                return false;
            }
        }
        return true;

    }

    //批量回写数据
    public static void batchWaiteBackData(String excelPath) {
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(excelPath));
            //遍历要回写的数据对象集合
            for (WriteBackData writeBackData : backData) {
                //获取sheetName
                String sheetName = writeBackData.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);
                String caseId = writeBackData.getCaseId();
                Integer rowNum = caseIdAndRowNum.get(caseId);
                //获取要写入的数据行
                Row row = sheet.getRow(rowNum);
                String cellName = writeBackData.getCellName();
                //获取要写入的数据列
                Integer cellNum = cellNameAndCellNum.get(cellName);
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String result = writeBackData.getResult();
                cell.setCellValue(result);
            }
            //写入数据
            workbook.write(new FileOutputStream(excelPath));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    //     String excelPath = "D:\\javaApi\\apiAuto\\src\\main\\resources\\cases.xlsx";
    //     load(excelPath, "case", Interfaces.class);
    //     for (Case aCase : CaseUtil.cases) {
    //         System.out.println(aCase);
    //     }
    // }

}
