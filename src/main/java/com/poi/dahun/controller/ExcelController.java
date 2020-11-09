package com.poi.dahun.controller;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.poi.dahun.service.ExcelService;
import com.poi.dahun.vo.Fruit;

@Controller
public class ExcelController {

	@Autowired
	private ExcelService service;
	
	@PostMapping("/downloadExcelFile.do")
	public String downloadExcepFile(Model model) {
		 String[] names = {"자몽", "애플망고", "멜론", "오렌지"};
	        long[] prices = {5000, 10000, 7000, 6000};
	        int[] quantities = {50, 50, 40, 40};
	        List<Fruit> list = service.makeFruitList(names, prices, quantities);
	        
	        SXSSFWorkbook workbook = service.excelFileDownloadProcess(list);
	        
	        System.out.println("action");
	        model.addAttribute("workbook", workbook);
	        model.addAttribute("workbookName", "과일표");
		
		return "excelDownloadView";
	}
	@RequestMapping(value = "/uploadExcelFile.do", method = RequestMethod.POST)
    public String uploadExcelFile(MultipartHttpServletRequest request, Model model) {
		System.out.println("action");
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if(iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        List<Fruit> list = service.uploadExcelFile(file);
        
        model.addAttribute("list", list);
        return "jsonView";
    }

	
}
