package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class HistoryServletTest {

    private HistoryServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = spy(new HistoryServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/app");
        when(request.getRequestDispatcher("/history.jsp")).thenReturn(dispatcher);
    }

    @Test
    void doGet_shouldRedirectToIndex_whenSessionIsNull() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/index");
    }

    @Test
    void doGet_shouldRedirectToIndex_whenHistoryIsNull() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("history")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/index");
    }

    @Test
    void doGet_shouldForwardToHistoryPage_whenHistoryExists() throws Exception {
        History history = mock(History.class);
        when(history.getName()).thenReturn("Story1");
        when(history.getInfo()).thenReturn("Info text");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("history")).thenReturn(history);

        servlet.doGet(request, response);

        verify(request).setAttribute("nameHistory", "Story1");
        verify(request).setAttribute("info", "Info text");
        verify(request).setAttribute("bgHistory", "image/historyBg/Story1.png");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_shouldRedirectToMain_whenStartIsNull() throws Exception {
        when(request.getParameter("start")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/main");
    }

    @Test
    void doPost_shouldInitGameAndRedirectToPlay_whenStartExists() throws Exception {
        when(request.getParameter("start")).thenReturn("start");
        when(request.getSession(true)).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute("index", 0);
        verify(session).removeAttribute("resultText");
        verify(response).sendRedirect("/app/play");
    }
}