<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
</head>
<body>
<form id="example-1" name="example1" method="post" enctype="multipart/form-data">
	 <input type="file" id="fileInput" name="fileInput">
    <button type="button" onclick="doExcelUploadProcess()">엑셀업로드 작업</button>
    <button type="button" onclick="doExcelDownloadProcess()">엑셀다운로드 작업</button>
</form>
<div id="result">
</div>
<script type="text/javascript">
	$(function(){
		
		
	})
	
		 function doExcelUploadProcess(){
			 var data = new FormData(document.getElementById('example-1'));
			 $.ajax({
				 url:"/uploadExcelFile.do",
				 data: data,
				 //파일 전송시 쿼리스트링을 만들지 않음
				 processData: false,
				 //파일 전송시 multipart/form-data를 위해 contentType을 false 처리함
				 contentType: false,
				 type:"POST",
				 success:function(result){
					 console.log(result);
				 }
				 
			 })
		 }
		 function doExcelDownloadProcess(){
			 var form = document.example1;
			 form.action = "/downloadExcelFile.do";
			 form.submit();
		 }
		
	
	

</script>
</body>
</html>