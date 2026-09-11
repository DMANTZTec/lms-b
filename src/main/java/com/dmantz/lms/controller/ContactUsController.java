package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.ContactUsRequest;
import com.dmantz.lms.dto.response.ContactUsResponse;
import com.dmantz.lms.service.ContactUsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {

    private final ContactUsService contactUsService;

    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    @PostMapping("/raise-enquiry")
    public ResponseEntity<ContactUsResponse> createContactUs(
            @RequestBody ContactUsRequest request) {

        ContactUsResponse response = contactUsService.createContactUs(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactUsResponse> getContactUsById(
            @PathVariable Long id) {

        ContactUsResponse response = contactUsService.getContactUsById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ContactUsResponse>> getAllContactUs() {

        List<ContactUsResponse> response = contactUsService.getAllContactUs();
        return ResponseEntity.ok(response);
    }
}
