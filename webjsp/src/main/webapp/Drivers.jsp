<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список пилотов</title>
</head>
<body>
    <% request.setCharacterEncoding("UTF-8");
       String team = request.getParameter("team");
       if (team == null) {
           RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ErrorManager.jsp");
           dispatcher.forward(request, response);
           return;
       }
    %>
    <h1>Пилоты команды <%=team%></h1>
    <%@include file="DriversData.jsp"%>
</body>
</html>