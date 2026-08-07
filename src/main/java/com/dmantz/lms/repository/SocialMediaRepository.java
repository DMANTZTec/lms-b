package com.dmantz.lms.repository;

import com.dmantz.lms.entity.SocialMedia;
import com.dmantz.lms.entity.SocialPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {

    List<SocialMedia> findByIsActiveTrue();

    Optional<SocialMedia> findByPlatform(SocialPlatform platform);
}