package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Wrapper.HistoryWrapper;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.*;

class IndexServletTest {

    private IndexServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        servlet = spy(new IndexServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        servletContext = mock(ServletContext.class);

        doReturn(servletContext).when(servlet).getServletContext();

        when(request.getSession(true)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/app");
    }

    @Test
    void doGet_shouldSetNeedAuth_whenNoUserAndLoginCookieMissing() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("other", "value")});

        History history = mock(History.class);

        try (MockedStatic<HistoryWrapper> historyWrapperMock = mockStatic(HistoryWrapper.class)) {
            historyWrapperMock.when(HistoryWrapper::getStoriesList).thenReturn(List.of(history));

            servlet.doGet(request, response);

            verify(request).setAttribute("needAuth", true);
            verify(request).setAttribute("historyList", List.of(history));
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void doGet_shouldRestoreUserFromCookie_whenCookieMatchesUser() throws Exception {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("bohdan");

        when(session.getAttribute("user")).thenReturn(null);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("login", "bohdan")});

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class);
             MockedStatic<HistoryWrapper> historyWrapperMock = mockStatic(HistoryWrapper.class)) {

            userWrapperMock.when(() -> UserWrapper.getUserList(servletContext))
                    .thenReturn(List.of(user));

            historyWrapperMock.when(HistoryWrapper::getStoriesList)
                    .thenReturn(List.of());

            servlet.doGet(request, response);

            verify(session).setAttribute("user", user);
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void doPost_shouldRedirectToProfile_whenProfileParamExists() throws Exception {
        when(request.getParameter("profile")).thenReturn("profile");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/profile");
    }

    @Test
    void doPost_shouldSaveHistoryAndRedirectToMain_whenValidChoiceHistory() throws Exception {
        History history = mock(History.class);
        when(history.getName()).thenReturn("Story1");

        when(request.getParameter("profile")).thenReturn(null);
        when(request.getParameter("choiceHistory")).thenReturn("Story1");

        try (MockedStatic<HistoryWrapper> historyWrapperMock = mockStatic(HistoryWrapper.class)) {
            historyWrapperMock.when(HistoryWrapper::getStoriesList)
                    .thenReturn(List.of(history));

            servlet.doPost(request, response);

            verify(session).setAttribute("history", history);
            verify(response).sendRedirect("/app/main");
        }
    }

    @Test
    void doPost_shouldRedirectBackToIndex_whenNoParamsHandled() throws Exception {
        when(request.getParameter("profile")).thenReturn(null);
        when(request.getParameter("choiceHistory")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/app/index");
    }
}