package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.ContactUsRequest;
import com.dmantz.lms.dto.response.ContactUsResponse;
import com.dmantz.lms.entity.ContactStatus;
import com.dmantz.lms.entity.ContactUs;
import com.dmantz.lms.mapper.ContactUsMapper;
import com.dmantz.lms.repository.ContactUsRepository;
import com.dmantz.lms.service.ContactUsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;
    private final ContactUsMapper contactUsMapper;

    public ContactUsServiceImpl(ContactUsRepository contactUsRepository, ContactUsMapper contactUsMapper) {
        this.contactUsRepository = contactUsRepository;
        this.contactUsMapper = contactUsMapper;
    }

    @Override
    public ContactUsResponse createContactUs(ContactUsRequest request) {

        ContactUs contactUs = new ContactUs();

        contactUs.setFullName(request.getFullName());
        contactUs.setMobileNumber(request.getMobileNumber());
        contactUs.setEmail(request.getEmail());
        contactUs.setCurrentPosition(request.getCurrentPosition());
        contactUs.setLocation(request.getLocation());

        // Default status for newly created contact requests
        contactUs.setStatus(ContactStatus.NEW);

        ContactUs savedContact = contactUsRepository.save(contactUs);

        ContactUsResponse response = new ContactUsResponse();

        response.setId(savedContact.getId());
        response.setFullName(savedContact.getFullName());
        response.setMobileNumber(savedContact.getMobileNumber());
        response.setEmail(savedContact.getEmail());
        response.setCurrentPosition(savedContact.getCurrentPosition());
        response.setLocation(savedContact.getLocation());
        response.setStatus(savedContact.getStatus());
        response.setMessage("Contact request submitted successfully.");

        return response;
    }

    @Override
    public ContactUsResponse getContactUsById(Long id) {

        ContactUs contactUs = contactUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                                "Contact enquiry not found with id: " + id));

        return contactUsMapper.toResponse(contactUs);
    }

    @Override
    public List<ContactUsResponse> getAllContactUs() {

        return contactUsRepository.findAll()
                .stream()
                .map(contactUsMapper::toResponse)
                .toList();
    }
}
