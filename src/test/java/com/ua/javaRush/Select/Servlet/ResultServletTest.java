package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResultServletTest {

    private ResultServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new ResultServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/app");
        when(request.getRequestDispatcher("/result.jsp")).thenReturn(dispatcher);
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
    void doGet_shouldForwardToResult_whenHistoryExists() throws Exception {
        History history = mock(History.class);
        when(history.getName()).thenReturn("Story1");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("history")).thenReturn(history);

        servlet.doGet(request, response);

        verify(request).setAttribute("bgHistory", "image/historyBg/Story1.png");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_shouldInvalidateSessionAndRedirectToMain() throws Exception {
        when(request.getParameter("goHome")).thenReturn("goHomePage");
        when(request.getSession(false)).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/app/main");
    }

    @Test
    void doPost_shouldThrowException_whenGoHomeParamIsMissing() {
        when(request.getParameter("goHome")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> servlet.doPost(request, response));
    }
}