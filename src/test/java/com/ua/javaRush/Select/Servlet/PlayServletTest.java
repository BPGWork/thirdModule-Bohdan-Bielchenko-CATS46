package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Wrapper.HistoryPhaseWrapper;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class PlayServletTest {

    private PlayServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        servlet = spy(new PlayServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        servletContext = mock(ServletContext.class);

        doReturn(servletContext).when(servlet).getServletContext();

        when(request.getContextPath()).thenReturn("/app");
        when(request.getRequestDispatcher("/play.jsp")).thenReturn(dispatcher);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void doGet_shouldRedirectToIndex_whenSessionIsNull() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/index");
    }

    @Test
    void doGet_shouldRedirectToIndex_whenHistoryIsNull() throws Exception {
        when(session.getAttribute("history")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/index");
    }

    @Test
    void doGet_shouldWriteError_whenHistoryPhasesAreEmpty() throws Exception {
        History history = mock(History.class);
        when(history.getName()).thenReturn("Story1");
        when(session.getAttribute("history")).thenReturn(history);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        try (MockedStatic<HistoryPhaseWrapper> historyPhaseWrapperMock = mockStatic(HistoryPhaseWrapper.class)) {
            historyPhaseWrapperMock.when(() -> HistoryPhaseWrapper.getHistory("Story1"))
                    .thenReturn(List.of());

            servlet.doGet(request, response);

            verify(response, never()).sendRedirect(anyString());
        }
    }

    @Test
    void doGet_shouldInitCurrentPhaseAndForwardToPlayPage() throws Exception {
        History history = mock(History.class);
        HistoryPhase phase = mock(HistoryPhase.class);

        when(history.getName()).thenReturn("Story1");
        when(session.getAttribute("history")).thenReturn(history);
        when(session.getAttribute("currentPhaseTitle")).thenReturn(null);

        when(phase.getTitle()).thenReturn("Start");
        when(phase.getListChoice()).thenReturn(List.of("A", "B"));

        try (MockedStatic<HistoryPhaseWrapper> historyPhaseWrapperMock = mockStatic(HistoryPhaseWrapper.class)) {
            historyPhaseWrapperMock.when(() -> HistoryPhaseWrapper.getHistory("Story1"))
                    .thenReturn(List.of(phase));

            servlet.doGet(request, response);

            verify(session).setAttribute("currentPhaseTitle", "Start");
            verify(request).setAttribute("title", "Start");
            verify(request).setAttribute("choiceList", List.of("A", "B"));
            verify(request).setAttribute("bgHistory", "image/historyBg/Story1.png");
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void doPost_shouldMoveToNextPhase_whenChoiceLeadsToAnotherPhase() throws Exception {
        History history = mock(History.class);
        HistoryPhase current = mock(HistoryPhase.class);
        HistoryPhase next = mock(HistoryPhase.class);

        when(history.getName()).thenReturn("Story1");
        when(session.getAttribute("history")).thenReturn(history);
        when(session.getAttribute("currentPhaseTitle")).thenReturn("Start");
        when(request.getParameter("result")).thenReturn("Go");

        when(current.getTitle()).thenReturn("Start");
        when(current.getValueWithMap("Go")).thenReturn("Next");
        when(next.getTitle()).thenReturn("Next");

        try (MockedStatic<HistoryPhaseWrapper> historyPhaseWrapperMock = mockStatic(HistoryPhaseWrapper.class)) {
            historyPhaseWrapperMock.when(() -> HistoryPhaseWrapper.getHistory("Story1"))
                    .thenReturn(List.of(current, next));

            servlet.doPost(request, response);

            verify(session).setAttribute("currentPhaseTitle", "Next");
            verify(response).sendRedirect("/app/play");
        }
    }

    @Test
    void doPost_shouldHandleEndPhaseAndRedirectToResult() throws Exception {
        History history = mock(History.class);
        HistoryPhase current = mock(HistoryPhase.class);
        User user = mock(User.class);

        when(history.getName()).thenReturn("Story1");
        when(session.getAttribute("history")).thenReturn(history);
        when(session.getAttribute("currentPhaseTitle")).thenReturn("Start");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("result")).thenReturn("Finish");

        when(current.getTitle()).thenReturn("Start");
        when(current.getValueWithMap("Finish")).thenReturn("END_WIN");

        when(user.getListOfStoryEndings()).thenReturn(new ArrayList<>());
        when(user.getResultOfStoryEndings()).thenReturn(3);

        try (MockedStatic<HistoryPhaseWrapper> historyPhaseWrapperMock = mockStatic(HistoryPhaseWrapper.class);
             MockedStatic<UserWrapper> userWrapperMock = mockStatic(UserWrapper.class)) {

            historyPhaseWrapperMock.when(() -> HistoryPhaseWrapper.getHistory("Story1"))
                    .thenReturn(List.of(current));

            servlet.doPost(request, response);

            verify(user).addStoryEnding("Story1: END_WIN");
            verify(user).setResultOfStoryEndings(4);
            verify(session).setAttribute("user", user);
            verify(session).setAttribute("resultText", "END_WIN");
            verify(session).removeAttribute("currentPhaseTitle");
            userWrapperMock.verify(() -> UserWrapper.changeUser(user, servletContext));
            verify(response).sendRedirect("/app/result");
        }
    }
}