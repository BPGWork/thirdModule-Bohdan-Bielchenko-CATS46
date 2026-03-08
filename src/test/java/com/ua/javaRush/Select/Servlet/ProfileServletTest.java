package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfileServletTest {

    private ProfileServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new ProfileServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/app");
        when(request.getRequestDispatcher("/profile.jsp")).thenReturn(dispatcher);
    }

    @Test
    void doGet_shouldWriteMessage_whenSessionIsNull() throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);
        pw.flush();

        org.junit.jupiter.api.Assertions.assertTrue(sw.toString().contains("Session is null"));
    }

    @Test
    void doGet_shouldWriteMessage_whenUserIsNull() throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);
        pw.flush();

        org.junit.jupiter.api.Assertions.assertTrue(sw.toString().contains("User is null in session"));
    }

    @Test
    void doGet_shouldForwardToProfile_whenUserExists() throws Exception {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("bohdan");
        when(user.getResultOfStoryEndings()).thenReturn(5);
        when(user.getListOfStoryEndings()).thenReturn(List.of("Story1: END_WIN"));

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        servlet.doGet(request, response);

        verify(request).setAttribute("nickname", "bohdan");
        verify(request).setAttribute("result", 5);
        verify(request).setAttribute("listOfStoryEndings", List.of("Story1: END_WIN"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_shouldRedirectToIndex_whenButtonMainPage() throws Exception {
        when(request.getParameter("btm")).thenReturn("mainPage");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/index");
    }

    @Test
    void doPost_shouldThrowException_whenButtonParamIsMissing() {
        when(request.getParameter("btm")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> servlet.doPost(request, response));
    }
}