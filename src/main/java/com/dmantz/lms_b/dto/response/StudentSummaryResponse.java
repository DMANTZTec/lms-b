package com.dmantz.lms_b.dto.response;

public class StudentSummaryResponse {

    private long totalEnrolled;
    private long planned;
    private long ongoing;
    private long completed;
    private double averageProgress;

    public long getTotalEnrolled() {
        return totalEnrolled;
    }

    public void setTotalEnrolled(long totalEnrolled) {
        this.totalEnrolled = totalEnrolled;
    }

    public long getPlanned() {
        return planned;
    }

    public void setPlanned(long planned) {
        this.planned = planned;
    }

    public long getOngoing() {
        return ongoing;
    }

    public void setOngoing(long ongoing) {
        this.ongoing = ongoing;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(double averageProgress) {
        this.averageProgress = averageProgress;
    }

    @Override
    public String toString() {
        return "StudentSummaryResponse{" +
                "totalEnrolled=" + totalEnrolled +
                ", planned=" + planned +
                ", ongoing=" + ongoing +
                ", completed=" + completed +
                ", averageProgress=" + averageProgress +
                '}';
    }
}
