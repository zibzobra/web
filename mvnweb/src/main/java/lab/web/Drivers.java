package lab.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/drivers")
public class Drivers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String BUNDLE_NAME = "lab.web.Drivers$Res";

    // Русские ресурсы
    public static class Res extends ListResourceBundle {
        @Override
        protected Object[][] getContents() {
            return new Object[][] {
                {"title", "Список пилотов Формулы-1"},
                {"filter", "фильтр"},
                {"language", "Язык"},
                {"driver", "Пилот"},
                {"team", "Команда"},
                {"number", "Номер"},
                {"titles", "Титулы"},
                {"empty", "Пилоты не найдены"},
                {"driver.verstappen", "Макс Ферстаппен"},
                {"driver.hamilton", "Льюис Хэмилтон"},
                {"driver.leclerc", "Шарль Леклер"},
                {"driver.norris", "Ландо Норрис"}
            };
        }
    }

    public static class Res_ru extends Res {
    }

    // Английские ресурсы
    public static class Res_en extends ListResourceBundle {
        @Override
        protected Object[][] getContents() {
            return new Object[][] {
                {"title", "Formula 1 Drivers List"},
                {"filter", "filter"},
                {"language", "Language"},
                {"driver", "Driver"},
                {"team", "Team"},
                {"number", "Number"},
                {"titles", "Titles"},
                {"empty", "No drivers found"},
                {"driver.verstappen", "Max Verstappen"},
                {"driver.hamilton", "Lewis Hamilton"},
                {"driver.leclerc", "Charles Leclerc"},
                {"driver.norris", "Lando Norris"}
            };
        }
    }

    public Drivers() {
        super();
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
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
        request.setCharacterEncoding("utf-8");

        String lang = request.getParameter("lang");
        String teamFilter = request.getParameter("team");

        if (lang == null) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "Ожидался параметр lang / Parameter lang expected");
            return;
        }
        if (!"en".equalsIgnoreCase(lang) && !"ru".equalsIgnoreCase(lang)) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "Параметр lang может принимать значение en или ru / lang must be en or ru");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        Locale locale = "en".equalsIgnoreCase(lang) ? Locale.ENGLISH : new Locale("ru", "RU");
        ResourceBundle res = ResourceBundle.getBundle(BUNDLE_NAME, locale);

        boolean hasFilter = teamFilter != null && !teamFilter.isEmpty();

        PrintWriter out = response.getWriter();
        try {
            String title = res.getString("title");

            out.println("<html>");
            out.println("<head><title>" + title + "</title></head>");
            out.println("<body>");
            out.println("<h1>" + title
                    + (hasFilter ? " (" + res.getString("filter") + ": " + escape(teamFilter) + ")" : "")
                    + "</h1>");

            String teamParam = hasFilter ? "&amp;team=" + escape(teamFilter) : "";
            out.println("<p>" + res.getString("language") + ": "
                    + "<a href='drivers?lang=ru" + teamParam + "'>RU</a> | "
                    + "<a href='drivers?lang=en" + teamParam + "'>EN</a></p>");

            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr><td><b>" + res.getString("driver") + "</b></td>"
                    + "<td><b>" + res.getString("team") + "</b></td>"
                    + "<td><b>" + res.getString("number") + "</b></td>"
                    + "<td><b>" + res.getString("titles") + "</b></td></tr>");

            String[][] driversData = {
                {"driver.verstappen", "Red Bull Racing", "3", "4"},
                {"driver.hamilton", "Ferrari", "44", "7"},
                {"driver.leclerc", "Ferrari", "16", "0"},
                {"driver.norris", "McLaren", "4", "1"}
            };

            int shown = 0;
            for (String[] driver : driversData) {
                String team = driver[1];
                if (hasFilter && !team.equalsIgnoreCase(teamFilter)) {
                    continue;
                }
                shown++;
                out.println("<tr><td>" + res.getString(driver[0]) + "</td><td>" + team
                        + "</td><td>" + driver[2] + "</td><td>" + driver[3] + "</td></tr>");
            }

            if (shown == 0) {
                out.println("<tr><td colspan='4'>" + res.getString("empty") + "</td></tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}