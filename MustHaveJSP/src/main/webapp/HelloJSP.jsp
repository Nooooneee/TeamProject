<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!             // 선언부
	String str1 = "JSP;";
	String str2 = "안녕하세요";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello JSP...!!</title>
</head>
<body>
	<h2>처음 만들어보는 <%=str1 %></h2>		<!-- 표현식 -->
	<li>집에서 클론 연습</li>
	<p>
	<%
	out.println(str2 + str1 + " 입니다. 열공합시다.^^*"); 	// 스크립터 요소
	%>
	</p>
</body>
</html>