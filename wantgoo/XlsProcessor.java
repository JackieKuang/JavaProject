package wantgoo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public final class XlsProcessor {
 
 private static final XlsProcessor processor = new XlsProcessor();
 
 private XlsProcessor(){
 }
 
 public static XlsProcessor getInstance(){
  return processor;
 }
 
 public File convert(File cvsFile, String path) throws IOException, RowsExceededException, WriteException{
  FileReader fileReader = new FileReader(cvsFile);
  BufferedReader bufferedReader = new BufferedReader(fileReader);
  String xlsFileName = getFileName(cvsFile.getName());
  File xls = new File(path+xlsFileName);
  WritableWorkbook workbook = Workbook.createWorkbook(xls);
  WritableSheet sheet = workbook.createSheet("Wantgoo", 0);
  
  //設定字型
  WritableFont myFont = new WritableFont(WritableFont.createFont("新細明體"), 12); //新細明體、標楷體
  myFont.setColour(Colour.BLACK);
  
  WritableCellFormat cellFormat = new WritableCellFormat();
  cellFormat.setFont(myFont); // 指定字型
  //cellFormat.setBackground(Colour.DEFAULT_BACKGROUND); // 背景顏色
  //cellFormat.setAlignment(Alignment.CENTRE); // 對齊方式
  
  int rowNumber = 0;
  String row = bufferedReader.readLine();
  while(row != null){
	   String[] cells = row.split(",");
	   for(int i=0; i<cells.length; i++) {
		   sheet.addCell(new Label(i,rowNumber,cells[i],cellFormat));
	   }
	   rowNumber++;
	   row = bufferedReader.readLine();
  }
  workbook.write();
  workbook.close();
  return xls;
 }
 
 public boolean compare(File a, File b) throws BiffException, IOException{
  Workbook aa = Workbook.getWorkbook(a);
  Workbook bb = Workbook.getWorkbook(b);
  Sheet aaSheet = aa.getSheet(0);
  Sheet bbSheet = bb.getSheet(0);
  int aaSheetRows = aaSheet.getRows();
  int bbSheetRows = bbSheet.getRows();
  int aaSheetCols = aaSheet.getColumns();
  int bbSheetCols = bbSheet.getColumns();
  boolean result = true;
  if(aaSheetRows == bbSheetRows && aaSheetCols == bbSheetCols){
   for(int i=0;i<aaSheetRows;i++){
    for(int j=0;j<aaSheetCols;j++){
     String aaCell = aaSheet.getCell(j, i).getContents();
     String bbCell = bbSheet.getCell(j, i).getContents();
     if(!aaCell.equals(bbCell)){
      //System.out.println("Row:"+(i+1)+",Column:"+(j+1)+",one is "+aaCell+",the other is "+bbCell);
      result = false;
      break;
     }
    }
    if(result){
     continue;
    }else{
     break;
    }
   }
  }
  //System.out.println("Compare result : "+result);
  return result;
 }
 
 private String getFileName(String csvFileName){
  return csvFileName.substring(0, csvFileName.length()-4)+".xls";
 }
 
 private List<String> format(String[] cells){
  List<String> cellList = new ArrayList<String>();
  StringBuffer buffer = new StringBuffer();
  for(String cell : cells){
   boolean isHead = cell.startsWith("\"");
   boolean isTail = cell.endsWith("\"");
   if(isHead){
    if(isTail){
     cellList.add(cell.substring(1, cell.length()-1));
    }else{
     buffer.append(cell.substring(1)).append(',');
    }
   }else{
    if(isTail){
     buffer.append(cell.substring(0, cell.length()-1));
     cellList.add(buffer.toString());
     buffer.delete(0, buffer.length());
    }else if(buffer.length()>0){
     buffer.append(cell).append(',');
    }else{
     cellList.add(cell);
    }
   }
  }
  return cellList;
 }
 
 
 public static void main(String[] args) throws RowsExceededException, WriteException {
	  try {
	   XlsProcessor processor = XlsProcessor.getInstance();
	   String csv = "C:/wantgoo_app/exp_file/wantgoo_2011-6-2-519.csv";
	   processor.convert(new File(csv),"C:/wantgoo_app/exp_file/");
	  } catch (IOException e) {
	   e.printStackTrace();
	  }

 }


} 
