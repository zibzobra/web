<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    if (session.getAttribute("visits") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    String name = "";
    String color = "white";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("username".equals(c.getName())) {
                name = java.net.URLDecoder.decode(c.getValue(), "UTF-8");
            }
            if ("color".equals(c.getName())) {
                color = c.getValue();
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Результат</title>
</head>
<body style="background-color: <%= escape(color) %>;">
    <h1>Здравствуйте, <%= escape(name) %>!</h1>

    <h3>Значения Cookie</h3>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr><td><b>Имя</b></td><td><b>Значение</b></td></tr>
        <tr><td>username</td><td><%= escape(name) %></td></tr>
        <tr><td>color</td><td><%= escape(color) %></td></tr>
    </table>

    <h3>Переменные сессии</h3>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr><td><b>Имя</b></td><td><b>Значение</b></td></tr>
        <tr><td>Количество обращений</td><td><%= session.getAttribute("visits") %></td></tr>
        <tr><td>Дата последнего обращения</td><td><%= session.getAttribute("lastVisit") %></td></tr>
    </table>

    <p><a href="index.jsp">Изменить настройки</a></p>
</body>
</html>