package com.poi.dahun.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poi.dahun.vo.Fruit;

@Service
public class ExcelService {
	/**
     *업로드한 엑셀파일을 과일 리스트로 만들기
     * @param excelFile
     * @return 생성한 과일 리스트
     */
    public List<Fruit> uploadExcelFile(MultipartFile excelFile){
        List<Fruit> list = new ArrayList<Fruit>();
        try {
            OPCPackage opcPackage = OPCPackage.open(excelFile.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            
            // 첫번째 시트 불러오기
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for(int i=1; i<sheet.getLastRowNum() + 1; i++) {
                Fruit fruit = new Fruit();
                XSSFRow row = sheet.getRow(i);
                
                // 행이 존재하기 않으면 패스
                if(null == row) {
                    continue;
                }
                
                // 행의 두번째 열(이름부터 받아오기) 
                XSSFCell cell = row.getCell(1);
                if(null != cell) fruit.setName(cell.getStringCellValue());
                // 행의 세번째 열 받아오기
                cell = row.getCell(2);
                if(null != cell) fruit.setPrice((long)cell.getNumericCellValue());
                // 행의 네번째 열 받아오기
                cell = row.getCell(3);
                if(null != cell) fruit.setQuantity((int)cell.getNumericCellValue());
                
                list.add(fruit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	/**
     * 엑셀파일로 만들 리스트 생성
     * @param names
     * @param prices
     * @param quantities
     * @return 엑셀파일 리스트
     */
    public ArrayList<Fruit> makeFruitList(String[] names, long[] prices, int[] quantities){
        ArrayList<Fruit> list = new ArrayList<Fruit>();
        for(int i=0; i< names.length; i++) {
            Fruit fruit = new Fruit(names[i], prices[i], quantities[i]);
            list.add(fruit);
        }
        return list;
    }
    
    /**
     * 과일 리스트를 간단한 엑셀 워크북 객체로 생성
     * @param list
     * @return 생성된 워크북
     */
    public SXSSFWorkbook makeSimpleFruitExcelWorkbook(List<Fruit> list) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        
        // 시트 생성
        SXSSFSheet sheet = workbook.createSheet("과일표");
        
        //시트 열 너비 설정
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(0, 1500);
        
        // 헤더 행 생
        Row headerRow = sheet.createRow(0);
        // 해당 행의 첫번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("번호");
        // 해당 행의 두번째 열 셀 생성
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("과일이름");
        // 해당 행의 세번째 열 셀 생성
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("가격");
        // 해당 행의 네번째 열 셀 생성
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("수량");
        
        // 과일표 내용 행 및 셀 생성
        Row bodyRow = null;
        Cell bodyCell = null;
        for(int i=0; i<list.size(); i++) {
            Fruit fruit = list.get(i);
            
            // 행 생성
            bodyRow = sheet.createRow(i+1);
            // 데이터 번호 표시
            bodyCell = bodyRow.createCell(0);
            bodyCell.setCellValue(i + 1);
            // 데이터 이름 표시
            bodyCell = bodyRow.createCell(1);
            bodyCell.setCellValue(fruit.getName());
            // 데이터 가격 표시
            bodyCell = bodyRow.createCell(2);
            bodyCell.setCellValue(fruit.getPrice());
            // 데이터 수량 표시
            bodyCell = bodyRow.createCell(3);
            bodyCell.setCellValue(fruit.getQuantity());
        }
        
        return workbook;
    }
    
    /**
     * 생성한 엑셀 워크북을 컨트롤레에서 받게해줄 메소
     * @param list
     * @return
     */
    public SXSSFWorkbook excelFileDownloadProcess(List<Fruit> list) {
        return this.makeSimpleFruitExcelWorkbook(list);
    }

	
}
