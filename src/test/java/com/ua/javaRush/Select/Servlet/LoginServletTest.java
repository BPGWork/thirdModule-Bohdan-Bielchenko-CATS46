package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Util.PasswordUtil;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        servlet = Mockito.spy(new LoginServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        servletContext = mock(ServletContext.class);

        doReturn(servletContext).when(servlet).getServletContext();
        when(request.getContextPath()).thenReturn("/app");
    }

    @Test
    void doPost_shouldRedirectToError_whenUserNotFound() throws Exception {
        when(request.getParameter("login")).thenReturn("wrong");
        when(request.getParameter("password")).thenReturn("123");

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class)) {
            userWrapperMock.when(() -> UserWrapper.getUserList(servletContext))
                    .thenReturn(List.of());

            servlet.doPost(request, response);

            verify(response).sendRedirect("/app/index?error=1");
            verify(response, never()).addCookie(any());
            verify(session, never()).setAttribute(anyString(), any());
        }
    }

    @Test
    void doPost_shouldLoginSuccessfully_whenCredentialsAreValid() throws Exception {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("bohdan");
        when(user.getPassword()).thenReturn("hashed-pass");

        when(request.getParameter("login")).thenReturn("bohdan");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession(false)).thenReturn(session);

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class);
             MockedStatic<PasswordUtil> passwordUtilMock = mockStatic(PasswordUtil.class)) {

            userWrapperMock.when(() -> UserWrapper.getUserList(servletContext))
                    .thenReturn(List.of(user));

            passwordUtilMock.when(() -> PasswordUtil.checkPassword("123456", "hashed-pass"))
                    .thenReturn(true);

            servlet.doPost(request, response);

            verify(session).setAttribute("user", user);
            verify(response).addCookie(argThat(cookie ->
                    cookie.getName().equals("login")
                            && cookie.getValue().equals("bohdan")
                            && cookie.getPath().equals("/")
                            && cookie.getMaxAge() == 60 * 60 * 24 * 7
            ));
            verify(response).sendRedirect("/app/index");
        }
    }

    @Test
    void doPost_shouldRedirectToError_whenLoginOrPasswordIsNull() throws Exception {
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("123");

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class)) {
            userWrapperMock.when(() -> UserWrapper.getUserList(servletContext))
                    .thenReturn(List.of());

            servlet.doPost(request, response);

            verify(response).sendRedirect("/app/index?error=1");
        }
    }

    @Test
    void doPost_shouldThrowException_whenSessionDoesNotExistButUserIsValid() {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("bohdan");
        when(user.getPassword()).thenReturn("hashed-pass");

        when(request.getParameter("login")).thenReturn("bohdan");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession(false)).thenReturn(null);

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class);
             MockedStatic<PasswordUtil> passwordUtilMock = mockStatic(PasswordUtil.class)) {

            userWrapperMock.when(() -> UserWrapper.getUserList(servletContext))
                    .thenReturn(List.of(user));

            passwordUtilMock.when(() -> PasswordUtil.checkPassword("123456", "hashed-pass"))
                    .thenReturn(true);

            assertThrows(NullPointerException.class, () -> servlet.doPost(request, response));
        }
    }
}