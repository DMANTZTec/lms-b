package com.dmantz.lms.dto.response;

public class TotalHoursSpentResponse {

    private double totalHours;
    private String label = "Total learning time";

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "TotalHoursSpentResponse{" +
                "totalHours=" + totalHours +
                ", label='" + label + '\'' +
                '}';
    }
}
