package lab.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ProfileProcessor")
public class ProfileProcessor extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] COLORS = {"white", "PowderBlue", "HoneyDew", "LemonChiffon", "LightPink"};

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name = request.getParameter("username");
        String color = request.getParameter("color");

        if (name == null || name.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не задано имя пользователя");
            return;
        }
        if (color == null || !Arrays.asList(COLORS).contains(color)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Недопустимый цвет");
            return;
        }
        name = name.trim();

        HttpSession session = request.getSession(true);
        Integer visits = (Integer) session.getAttribute("visits");
        visits = (visits == null) ? 1 : visits + 1;
        session.setAttribute("visits", visits);
        session.setAttribute("lastVisit", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));

        String path = request.getContextPath() + "/";

        Cookie nameCookie = new Cookie("username", URLEncoder.encode(name, "UTF-8"));
        nameCookie.setMaxAge(3600);
        nameCookie.setPath(path);
        response.addCookie(nameCookie);

        Cookie colorCookie = new Cookie("color", color);
        colorCookie.setMaxAge(3600);
        colorCookie.setPath(path);
        response.addCookie(colorCookie);

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/Result.jsp"));
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