<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<sql:setDataSource
		driver="com.mysql.cj.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/forjavaee?serverTimezone=Asia/Taipei"
		user="root"
		password="Blackhurricane02"
/>
<sql:query var="result">
	SELECT * FROM secondproject
</sql:query>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FileList</title>
</head>
<body>
	<h1>File   List</h1>
	<hr>
	<h3><a href="fileUploadPage.html">Upload Somethings</a></h3>
	<table border="1" width="100%">
		<tr>
			<th>File Name</th>
			<th>File Size</th>
			<th>Upload Date</th>
			<th>For Download</th>
		</tr>
		<c:forEach items="${result.rows }" var="row">
			<tr>
				<td>${row.filename }</td>
				<td>${row.filesize } Byte</td>
				<td>${row.uploaddate }</td>
				<td><a download href="${row.fordown }">DOWNLOAD</a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>