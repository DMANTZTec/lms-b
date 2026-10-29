package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.ContactUsRequest;
import com.dmantz.lms.dto.response.ContactUsResponse;

import java.util.List;

public interface ContactUsService {

    ContactUsResponse createContactUs(ContactUsRequest request);

    ContactUsResponse getContactUsById(Long id);

    List<ContactUsResponse> getAllContactUs();
}
