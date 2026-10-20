package lab.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// библиотечный класс для работы с потоками вывода
import java.io.PrintWriter;

/**
 * Servlet implementation class Drivers
 */
@WebServlet("/drivers")
public class Drivers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Drivers() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Задание типа кодировки для параметров запроса
        request.setCharacterEncoding("utf-8");
        
        // Чтение параметра team из запроса (опциональный фильтр)
        String teamFilter = request.getParameter("team");
        
        // Задание типа содержимого для ответа (в том числе кодировки)
        response.setContentType("text/html;charset=UTF-8");
        
        // Получение потока для вывода ответа
        PrintWriter out = response.getWriter();
        
        try {
            // Создание HTML-страницы
            out.println("<html>");
            out.println("<head><title>Список гонщиков F1</title></head>");
            out.println("<body>");
            out.println("<h1>Список пилотов Формулы-1" + (teamFilter != null && !teamFilter.isEmpty() ? " (фильтр: " + teamFilter + ")" : "") + "</h1>");
            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr><td><b>Пилот</b></td><td><b>Команда</b></td><td><b>Номер</b></td><td><b>Титулы</b></td></tr>");
            
            // Массив данных о гонщиках: {Имя, Команда, Номер, Титулы}
            String[][] driversData = {
                {"Макс Ферстаппен", "Red Bull Racing", "1", "3"},
                {"Льюис Хэмилтон", "Ferrari", "44", "7"},
                {"Шарль Леклер", "Ferrari", "16", "0"},
                {"Ландо Норрис", "McLaren", "4", "0"}
            };
            
            // Перебираем массив и выводим только тех, кто подходит под фильтр
            for (String[] driver : driversData) {
                String name = driver[0];
                String team = driver[1];
                String number = driver[2];
                String titles = driver[3];
                
                // Если фильтр передан, проверяем совпадение команды (игнорируя регистр)
                if (teamFilter != null && !teamFilter.isEmpty() && !team.equalsIgnoreCase(teamFilter)) {
                    continue; // Пропускаем строку, если она не подходит под фильтр
                }
                
                out.println("<tr><td>" + name + "</td><td>" + team + "</td><td>" + number + "</td><td>" + titles + "</td></tr>");
            }
            
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            // Закрытие потока вывода
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}