<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%

    String savedName = "";
    String savedColor = "white";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("username".equals(c.getName())) {
                savedName = java.net.URLDecoder.decode(c.getValue(), "UTF-8");
            }
            if ("color".equals(c.getName())) {
                savedColor = c.getValue();
            }
        }
    }
    String[][] colors = {
        {"white", "Белый"}, {"PowderBlue", "Голубой"}, {"HoneyDew", "Зелёный"},
        {"LemonChiffon", "Жёлтый"}, {"LightPink", "Розовый"}
    };
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Настройки пользователя</title>
</head>
<body>
    <h1>Настройки пользователя</h1>
    <form method="post" action="ProfileProcessor">
        Имя пользователя:<br>
        <input type="text" name="username" value="<%= escape(savedName) %>"><br><br>
        Цвет страницы:<br>
        <select name="color">
            <% for (String[] c : colors) { %>
            <option value="<%= c[0] %>" <%= c[0].equals(savedColor) ? "selected" : "" %>><%= c[1] %></option>
            <% } %>
        </select><br><br>
        <input type="submit" value="Ввод">
    </form>
</body>
</html>