<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>사진 확인</title>
</head>
<body>
	<div>${img.file_oriname}</div>
	<div>${img.file_name}</div>
	<div>${img.user_id }</div>
	<div>
		<!-- 경로 설정....실패 -->
		<img src="src/main/resources/static/images/${img.file_name}" style="width:300px;height:auto;">
	</div>
</body>
</html>