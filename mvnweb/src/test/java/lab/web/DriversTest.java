package lab.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DriversTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    private AutoCloseable mocks;
    private StringWriter sw;
    private Drivers servlet;

    @Before
    public void setUp() throws Exception {
        // Инициализация объектов, помеченных @Mock
        mocks = MockitoAnnotations.openMocks(this);
        // Ответ сервлета записывается в StringWriter, чтобы потом проверить текст страницы
        sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        servlet = new Drivers();
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    private String run(String lang, String team) throws Exception {
        when(request.getParameter("lang")).thenReturn(lang);
        when(request.getParameter("team")).thenReturn(team);
        servlet.doGet(request, response);
        return sw.toString();
    }

    @Test
    public void testRussianAllDrivers() throws Exception {
        String html = run("ru", null);

        verify(request).setCharacterEncoding("utf-8");
        verify(response).setContentType("text/html;charset=UTF-8");
        assertTrue(html.contains("Список пилотов Формулы-1"));
        assertTrue(html.contains("Макс Ферстаппен"));
        assertTrue(html.contains("Льюис Хэмилтон"));
        assertTrue(html.contains("Шарль Леклер"));
        assertTrue(html.contains("Ландо Норрис"));
    }

    @Test
    public void testEnglishAllDrivers() throws Exception {
        String html = run("en", null);

        assertTrue(html.contains("Formula 1 Drivers List"));
        assertTrue(html.contains("Max Verstappen"));
        assertTrue(html.contains("Lando Norris"));
        assertTrue(html.contains("<b>Driver</b>"));
    }

    /** Фильтр по команде: выводятся только пилоты Ferrari. */
    @Test
    public void testFilterByTeam() throws Exception {
        String html = run("en", "Ferrari");

        assertTrue(html.contains("(filter: Ferrari)"));
        assertTrue(html.contains("<td>Lewis Hamilton</td><td>Ferrari</td><td>44</td><td>7</td>"));
        assertTrue(html.contains("Charles Leclerc"));
        assertFalse(html.contains("Max Verstappen"));
        assertFalse(html.contains("Lando Norris"));
    }

    /** Фильтр не зависит от регистра букв. */
    @Test
    public void testFilterIgnoresCase() throws Exception {
        String html = run("ru", "mclaren");

        assertTrue(html.contains("Ландо Норрис"));
        assertFalse(html.contains("Льюис Хэмилтон"));
    }

    /** Неизвестная команда: сообщение, что пилоты не найдены. */
    @Test
    public void testUnknownTeam() throws Exception {
        String html = run("en", "Mercedes");

        assertTrue(html.contains("No drivers found"));
        assertFalse(html.contains("Lewis Hamilton"));
    }

    /** Потенциально опасное значение фильтра экранируется. */
    @Test
    public void testFilterIsEscaped() throws Exception {
        String html = run("en", "<script>");

        assertFalse(html.contains("<script>"));
        assertTrue(html.contains("&lt;script&gt;"));
    }

    /** Параметр lang не задан: ошибка 406, страница не выводится. */
    @Test
    public void testMissingLang() throws Exception {
        when(request.getParameter("lang")).thenReturn(null);
        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_ACCEPTABLE), anyString());
        verify(response, never()).getWriter();
    }

    /** Недопустимое значение lang: ошибка 406. */
    @Test
    public void testInvalidLang() throws Exception {
        when(request.getParameter("lang")).thenReturn("de");
        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_ACCEPTABLE), anyString());
        verify(response, never()).getWriter();
    }

    @Test
    public void testPost() throws Exception {
        when(request.getParameter("lang")).thenReturn("en");
        when(request.getParameter("team")).thenReturn("Ferrari");
        servlet.doPost(request, response);

        assertTrue(sw.toString().contains("Lewis Hamilton"));
    }
}