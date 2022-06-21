package com.api.entity;

/**
 * 回写数据对象
 */
public class WriteBackData {
    private String sheetName;
    private String caseId;
    private String cellName;
    private String result;

    public WriteBackData() {
    }

    public WriteBackData(String sheetName, String caseId, String cellName, String result) {
        this.sheetName = sheetName;
        this.caseId = caseId;
        this.cellName = cellName;
        this.result = result;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
