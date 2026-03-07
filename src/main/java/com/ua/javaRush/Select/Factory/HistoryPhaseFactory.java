package com.ua.javaRush.Select.Factory;

import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Dto.HistoryPhaseDto;

import java.util.*;

public class HistoryPhaseFactory {
    private static Map<String, Map<String, String>> historyPhaseMap;

    public static List<HistoryPhase> createHistory (String nameHistory) {
        historyPhaseMap = HistoryPhaseDto.readHistory(nameHistory);

        List<HistoryPhase> historyPhaseList = new ArrayList<>();
        String phaseTitle;
        Map<String, String> phaseChoice;

        for (Map.Entry<String, Map<String, String>> mapEntry : historyPhaseMap.entrySet()) {
            phaseTitle = mapEntry.getKey();
            phaseChoice = mapEntry.getValue();

            historyPhaseList.add(new HistoryPhase(phaseTitle, phaseChoice));
        }

        return historyPhaseList;
    }
}
