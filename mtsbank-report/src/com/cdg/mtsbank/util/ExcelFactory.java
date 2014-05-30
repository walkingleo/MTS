package com.cdg.mtsbank.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * @author wangtongmin
 *
 */
public class ExcelFactory {
	
	public static boolean getExcelReport(ExcelConfBean bean,List<Map<String,String>> list){
		HSSFWorkbook wb = null; 
		try{
			String fileName = bean.getFileName();
			File targetFile = new File(fileName);
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			wb = new HSSFWorkbook();
			HSSFSheet targetSheet = wb.createSheet(bean.getSheetName());
			
			HSSFRow headerRow = targetSheet.createRow(1);
			Map<String, String> headers = bean.getHeaderIndex();
			
			HSSFCellStyle s = wb.createCellStyle();
			s.setBorderLeft((short)1);
			s.setBorderBottom((short)1);
			s.setBorderRight((short)1);
			s.setBorderTop((short)1);
			
			List<String> dataIndex = bean.getDataIndex();
			int i =1;//表头起始index
			for(String index : dataIndex){
				HSSFCell cell = headerRow.createCell( i++);
				cell.setCellValue(headers.get(index));
				cell.setCellStyle(s);
			}

			int y = 2;
			for(Map<String, String> map : list){
				HSSFRow headerRow1 = targetSheet.createRow(y++);
				int k=1;
				for(String index : dataIndex){
					HSSFCell cell = headerRow1.createCell(k++);
					cell.setCellValue(map.get(index));
					cell.setCellStyle(s);
				}
			}
			/**自动列宽**/
			for(int d = 1;d<=dataIndex.size();d++){
				targetSheet.autoSizeColumn(d, true);
			}
			
			FileOutputStream fileOut = new FileOutputStream(targetFile);  
            wb.write(fileOut);  
            fileOut.flush();  
            fileOut.close();  
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
