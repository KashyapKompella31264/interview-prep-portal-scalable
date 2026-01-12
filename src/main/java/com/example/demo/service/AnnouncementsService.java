package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Announcements;
import com.example.demo.repository.AnnouncementsRepository;

@Service
public class AnnouncementsService {
	private final AnnouncementsRepository announcementsRepository;
	public AnnouncementsService(AnnouncementsRepository announcementsRepository) {
		this.announcementsRepository=announcementsRepository;
	}
	public Announcements addAnnouncement(Announcements announcement) {
		return announcementsRepository.save(announcement);
	}
	public List<Announcements> getAnnouncementsforaCourse(String courseId){
		return announcementsRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
	}
}
