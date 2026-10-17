package com.dmantz.lms.dto.response;

import java.util.List;

public class DailyScheduleResponse {

    private String day;

    private List<ScheduleItemResponse> items;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<ScheduleItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ScheduleItemResponse> items) {
        this.items = items;
    }
}