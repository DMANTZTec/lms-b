package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class AssignInstructorToCourseRequest {

    @NotEmpty
    private List<String> staffIds;

    public List<String> getStaffIds() { return staffIds; }
    public void setStaffIds(List<String> staffIds) {
        this.staffIds = staffIds;
    }
}