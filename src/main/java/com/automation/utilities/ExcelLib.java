package com.automation.utilities;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class ExcelLib {
    //public String path = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "TestData.xlsx";
	//public String path = "C:\\Users\\E001987\\Git\\IBO\\Data_for_Request_for_Candidacy.xlsx";
    public  FileInputStream fis = null;
    public  FileOutputStream fileOut =null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row   =null;
    private XSSFCell cell = null;
    String path="";


    public ExcelLib(String path) {

        try {
        	this.path = path;
        	//System.out.println("Path"+ path);
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // returns the row count in a sheet

    public int getRowCount(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if(index==-1)
            return 0;
        else{
            sheet = workbook.getSheetAt(index);
            int number=sheet.getLastRowNum()+1;
            return number;
        }

    }


    public String getCellData(String sheetName,String colName,int rowNum){
        try{
            if(rowNum <=0)
                return "";

            int index = workbook.getSheetIndex(sheetName);
            int col_Num=-1;
            if(index==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    col_Num=i;
            }
            if(col_Num==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(col_Num);

            if(cell==null)
                return "";
            //System.out.println(cell.getCellType());
            if(cell.getCellType()==Cell.CELL_TYPE_STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cal.get(Calendar.MONTH)+1 + "/" +
                            cellText;

                    //System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());

        }
        catch(Exception e){

            e.printStackTrace();
            return "row "+rowNum+" or column "+colName +" does not exist in xls";
        }
    }


    // returns the data from a cell
    public String getCellData(String sheetName,int colNum,int rowNum){
        try{
            if(rowNum <=0)
                return "";

            int index = workbook.getSheetIndex(sheetName);

            if(index==-1)
                return "";


            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(colNum);
            if(cell==null)
                return "";

            if(cell.getCellType()==Cell.CELL_TYPE_STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH)+1 + "/" +
                            cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cellText;

                    // System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());
        }
        catch(Exception e){

            e.printStackTrace();
            return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
        }
    }


    // returns true if data is set successfully else false
    public boolean setCellData(String sheetName,String colName,int rowNum, String data){
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);


            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            // cell style
            //CellStyle cs = workbook.createCellStyle();
            //cs.setWrapText(true);
            //cell.setCellStyle(cs);
            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // returns true if sheet is created successfully else false
    public boolean addSheet(String  sheetname){

        FileOutputStream fileOut;
        try {
            workbook.createSheet(sheetname);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if sheet is removed successfully else false if sheet does not exist
    public boolean removeSheet(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if(index==-1)
            return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // returns true if column is created successfully
    // removes a column and all the contents
    // find whether sheets exists
    public boolean isSheetExist(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if(index==-1){
            index=workbook.getSheetIndex(sheetName.toUpperCase());
            if(index==-1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    // returns number of columns in a sheet
    public int getColumnCount(String sheetName){
        // check if sheet exists
        if(!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if(row==null)
            return -1;

        return row.getLastCellNum();



    }
    public int getCellRowNum(String sheetName,String colName,String cellValue){

        for(int i=2;i<=getRowCount(sheetName);i++){
            if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
                return i;
            }
        }
        return -1;

    }

    
/*    public HashMap<String,String> getTestCaseData(String testCase){
    	HashMap map = new HashMap<String,String>();
    	int getRownumber = getCellRowNum("TestData", "TestCase", testCase);
    	for(int i =1;i<=getColumnCount("TestData");i++ ) {
    		map.put(getCellData("TestData", i, 1).toString(),getCellData("TestData", i, getRownumber).toString());
    	}
		return map;
    }*/
    
    public HashMap<String,String> getTestCaseData(String testCase){
        HashMap<String, String> map = new HashMap<String,String>();
        int getRownumber = getCellRowNum(testCase, "TestCase", testCase);
        for(int i =1;i<=getColumnCount(testCase);i++ ) {
               map.put(getCellData(testCase, i, 1).toString(),getCellData(testCase, i, getRownumber).toString());
        }
               return map;
     }
    
    
    public HashMap<String,String> getTestCaseDataMultipleSheets() throws IOException{
    	HashMap<String,String> map = new HashMap<String,String>();
    	//int sheetIndex = 0;
//    	int getRownumber = getCellRowNum(testCase, "TestCase", testCase);
//    	sheet = workbook.getSheetAt(index);
    	int sheetCount = workbook.getNumberOfSheets();
    	for(int iSheet=0; iSheet<sheetCount; iSheet++) {
    		//sheetIndex = iSheet-1;
    		for(int i =0;i<=getColumnCountIndex(iSheet);i++ ) {
        		map.put(getCellDataSheetIndex(iSheet, i, 1).toString(),getCellDataSheetIndex(iSheet, i, 2).toString());
        		//String key = getCellDataSheetIndex(iSheet, i, 1).toString() ;
        		//String Value = getCellDataSheetIndex(iSheet, i, 2).toString();
        		//System.out.println("Key: "+key+" # Value:"+Value);
        	}
    	}
		return map;
    }
    
    // returns number of columns in a sheet
    public int getColumnCountIndex(int sheetIndex){
        // check if sheet exists
//        if(!isSheetExist(sheetName))
//            return -1;
    	
    	if(sheetIndex < 0)
            return -1;
    	
//        sheet = workbook.getSheet(sheetIndex);
    	sheet = workbook.getSheetAt(sheetIndex);
        row = sheet.getRow(0);

        if(row==null)
            return -1;

        return row.getLastCellNum();
    }
    
    // returns the data from a cell
    public String getCellDataSheetIndex(int sheetIndex,int colNum,int rowNum){
        try{
            if(rowNum <=0)
                return "";

//            int index = workbook.getSheetIndex(sheetName);
            int index = sheetIndex;

            if(index==-1)
                return "";


            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(colNum);
            if(cell==null)
                return "";

            if(cell.getCellType()==Cell.CELL_TYPE_STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH)+1 + "/" +
                            cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cellText;

                    // System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());
        }
        catch(Exception e){

            e.printStackTrace();
            return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
        }
    }
    // to run this on stand alone
    public static void main(String arg[]) throws IOException{
  	ExcelLib obj = new ExcelLib("");
  	obj.getTestCaseDataMultipleSheets();
  	//HashMap<String,String> map = obj.getTestCaseData("US10976");
   //System.out.println( map.get("Program"));
        
	} 
    
    
}
