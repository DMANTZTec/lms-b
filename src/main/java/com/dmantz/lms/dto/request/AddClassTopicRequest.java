package com.dmantz.lms.dto.request;

import java.util.List;

public class AddClassTopicRequest {

    private List<TopicItem> topics;

    public List<TopicItem> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicItem> topics) {
        this.topics = topics;
    }

    public static class TopicItem {
        private Long topicId;
        private String status;

        public Long getTopicId() {
            return topicId;
        }

        public void setTopicId(Long topicId) {
            this.topicId = topicId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        return "AddClassTopicRequest{" +
                "topics=" + topics +
                '}';
    }
}
