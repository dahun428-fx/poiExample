package com.poi.dahun.util;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

public class ExcelDownloadView extends AbstractView{
	
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//excel file 이름을 넘겨받는다. //filename 유닉스 타임으로 중복방지 // 뒤에 확장자 .xlsx 추가
		String fileName = this.fileNameToUnixTime((String) model.get("fileName"));
		 // 여기서부터는 각 브라우저에 따른 파일이름 인코딩작업
        fileName = this.urlEncode(fileName, request);
        
        //http header setting
        response.setContentType("application/download;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        
		OutputStream outputStream = null;
		SXSSFWorkbook workbook = null;
		
		try {
			//workbook 객체에 map 으로 가져온 객체 넣기
			workbook = (SXSSFWorkbook) model.get("workbook");
			//outputstream 객체 열기
			outputStream = response.getOutputStream();
			//다운로드 실행
			workbook.write(outputStream);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				} catch (Exception e2) {
					System.out.println("workbook 에러");
					e2.printStackTrace();
				}
			}
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e2) {
					System.out.println("다운로드 에러");
					e2.printStackTrace();
				}
			}
		}
        
        
	}

	private String fileNameToUnixTime(String fileName) {
		long unixTime = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append(unixTime).append(fileName).append(".xlsx");
		return sb.toString();
	}
	private String urlEncode(String fileName, HttpServletRequest request) throws Exception {
		String browser = request.getHeader("User-Agent");
        if (browser.indexOf("MSIE") > -1) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Trident") > -1) {       // IE11
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Firefox") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Opera") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Chrome") > -1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
               char c = fileName.charAt(i);
               if (c > '~') {
                     sb.append(URLEncoder.encode("" + c, "UTF-8"));
                       } else {
                             sb.append(c);
                       }
                }
                fileName = sb.toString();
        } else if (browser.indexOf("Safari") > -1){
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        } else {
             fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        }
		return fileName;
	};
}
