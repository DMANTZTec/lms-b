package com.dmantz.lms.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationChannel {

	IN_APP("in-app"), EMAIL("email");

	private final String value;

	NotificationChannel(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	// Accepts "in-app", "IN_APP", "inapp", "email", "EMAIL" (case-insensitive)
	@JsonCreator
	public static NotificationChannel fromValue(String raw) {
		if (raw == null) {
			throw new IllegalArgumentException("Notification channel must not be null");
		}
		String normalized = raw.trim().replace("_", "").replace("-", "").toLowerCase();
		for (NotificationChannel channel : values()) {
			if (channel.value.replace("-", "").equals(normalized)) {
				return channel;
			}
		}
		throw new IllegalArgumentException("Invalid notification channel: " + raw + ". Allowed: in-app, email");
	}
}
