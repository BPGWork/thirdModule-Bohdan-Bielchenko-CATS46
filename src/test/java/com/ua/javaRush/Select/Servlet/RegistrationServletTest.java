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

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RegistrationServletTest {

    private RegistrationServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        servlet = spy(new RegistrationServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        servletContext = mock(ServletContext.class);

        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getContextPath()).thenReturn("/app");
    }

    @Test
    void doPost_shouldRedirectToLogin_whenUserAlreadyExists() throws Exception {
        when(request.getParameter("login")).thenReturn("bohdan");
        when(request.getParameter("password")).thenReturn("123");
        when(request.getParameter("confirmPassword")).thenReturn("123");

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class)) {
            userWrapperMock.when(() -> UserWrapper.checkUser("bohdan", servletContext))
                    .thenReturn(true);

            servlet.doPost(request, response);

            verify(response).sendRedirect("/app/login");
            verify(session, never()).setAttribute(anyString(), any());
        }
    }

    @Test
    void doPost_shouldRedirectToRegistration_whenPasswordsDoNotMatch() throws Exception {
        when(request.getParameter("login")).thenReturn("bohdan");
        when(request.getParameter("password")).thenReturn("123");
        when(request.getParameter("confirmPassword")).thenReturn("456");

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class)) {
            userWrapperMock.when(() -> UserWrapper.checkUser("bohdan", servletContext))
                    .thenReturn(false);

            servlet.doPost(request, response);

            verify(response).sendRedirect("/app/registration");
        }
    }

    @Test
    void doPost_shouldRegisterUserAndRedirectToIndex_whenDataIsValid() throws Exception {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("bohdan");

        when(request.getParameter("login")).thenReturn("bohdan");
        when(request.getParameter("password")).thenReturn("123");
        when(request.getParameter("confirmPassword")).thenReturn("123");

        try (MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class);
             MockedStatic<PasswordUtil> passwordUtilMock = mockStatic(PasswordUtil.class)) {

            userWrapperMock.when(() -> UserWrapper.checkUser("bohdan", servletContext))
                    .thenReturn(false);

            passwordUtilMock.when(() -> PasswordUtil.hashPassword("123"))
                    .thenReturn("hashed123");

            userWrapperMock.when(() -> UserWrapper.addUser("bohdan", "hashed123", servletContext))
                    .thenReturn(user);

            servlet.doPost(request, response);

            verify(session).setAttribute("user", user);
            verify(response).addCookie(argThat(cookie ->
                    cookie.getName().equals("login")
                            && cookie.getValue().equals("bohdan")
            ));
            verify(response).sendRedirect("/app/index");
        }
    }
}