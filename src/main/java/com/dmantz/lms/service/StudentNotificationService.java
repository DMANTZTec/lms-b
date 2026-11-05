package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.SendStudentNotificationRequest;
import java.util.List;

import com.dmantz.lms.dto.response.StudentNotificationResponse;

public interface StudentNotificationService {

	StudentNotificationResponse sendNotification(String instructorId, SendStudentNotificationRequest request);

	List<StudentNotificationResponse> getStudentNotifications(String studentId);
}
