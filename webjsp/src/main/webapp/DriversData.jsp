<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%
    String teamName = request.getParameter("team");
    String[][] drivers = {
        {"Макс Ферстаппен", "Red Bull Racing", "3", "4"},
        {"Льюис Хэмилтон", "Ferrari", "44", "7"},
        {"Шарль Леклер", "Ferrari", "16", "0"},
        {"Ландо Норрис", "McLaren", "1", "1"}
    };
%>
<table border='1'>
    <tr>
        <td><b>Пилот</b></td>
        <td><b>Команда</b></td>
        <td><b>Номер</b></td>
        <td><b>Титулы</b></td>
    </tr>
    <% for (String[] d : drivers) {
           if (d[1].equalsIgnoreCase(teamName)) { %>
    <tr>
        <td><%=d[0]%></td>
        <td><%=d[1]%></td>
        <td><%=d[2]%></td>
        <td><%=d[3]%></td>
    </tr>
    <%     }
       } %>
</table>