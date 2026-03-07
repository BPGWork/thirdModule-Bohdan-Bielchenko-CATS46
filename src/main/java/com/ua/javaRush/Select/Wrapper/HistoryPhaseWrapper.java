package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Factory.HistoryPhaseFactory;

import java.util.List;

public class HistoryPhaseWrapper {
    private static List<HistoryPhase> historyPhases;

    public static List<HistoryPhase> getHistory (String nameHistory) {
        historyPhases = HistoryPhaseFactory.createHistory(nameHistory);
        return historyPhases;
    }
}
