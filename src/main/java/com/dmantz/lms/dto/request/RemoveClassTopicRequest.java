package com.dmantz.lms.dto.request;

import java.util.List;

public class RemoveClassTopicRequest {

    private List<Long> topicIds;

    public List<Long> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Long> topicIds) {
        this.topicIds = topicIds;
    }
}
