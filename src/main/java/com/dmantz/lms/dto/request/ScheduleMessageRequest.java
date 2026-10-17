package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ScheduleMessageRequest {

    @NotBlank(message = "Message must not be blank")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
