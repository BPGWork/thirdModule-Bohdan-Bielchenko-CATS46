package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Dto.HistoryDto;

import java.util.List;

public class HistoryWrapper {
    private static List<History> storiesList;

    public static List<History> getStoriesList () {
        storiesList = HistoryDto.readStories();
        return storiesList;
    }
}
