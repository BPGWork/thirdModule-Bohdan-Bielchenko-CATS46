package com.ua.javaRush.Select.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class HistoryPhase {
    private String title;
    private Map<String, String> phaseChoice;

    public List<String> getListChoice () {
        List<String> choiceList = new ArrayList<>();
        for(Map.Entry<String, String> choiceEntry : phaseChoice.entrySet()) {
            choiceList.add(choiceEntry.getKey());
        }

        return choiceList;
    }

    public String getValueWithMap (String key) {
        return phaseChoice.get(key);
    }
}
