package com.example.Plantation_system.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.NotificationDTO;
import com.example.Plantation_system.entities.Notification;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.NotificationMapper;
import com.example.Plantation_system.repositories.NotificationRepository;
import com.example.Plantation_system.repositories.UserRepository;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    public NotificationDTO createNotification(Long userId, NotificationDTO notificationDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(notificationDTO.getType());
        notification.setChannel(notificationDTO.getChannel());
        notification.setSubject(notificationDTO.getSubject());
        notification.setBody(notificationDTO.getBody());
        notification.setPayload(notificationDTO.getPayload());
        notification.setStatus(notificationDTO.getStatus() != null ? notificationDTO.getStatus() : "PENDING");
        notification.setSentAt(Instant.now());

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDTO(savedNotification);
    }

    public NotificationDTO getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        return notificationRepository.findUserNotificationsSorted(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserAndStatus(userId, "UNREAD").stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Integer getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notification.setReadAt(Instant.now());
        notification.setStatus("READ");
        notificationRepository.save(notification);
    }

    public void markAsDelivered(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notification.setDeliveredAt(Instant.now());
        notification.setStatus("DELIVERED");
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notificationRepository.delete(notification);
    }

    public void sendMeetupNotification(Long userId, String meetupName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType("MEETUP");
            notification.setChannel("IN_APP");
            notification.setSubject("New Meetup: " + meetupName);
            notification.setBody("A new plantation meetup has been created");
            notification.setStatus("PENDING");
            notification.setSentAt(Instant.now());
            notificationRepository.save(notification);
        }
    }

    public void sendDonationNotification(Long userId, String amount) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType("DONATION");
            notification.setChannel("IN_APP");
            notification.setSubject("Donation Received");
            notification.setBody("You received a donation of " + amount);
            notification.setStatus("PENDING");
            notification.setSentAt(Instant.now());
            notificationRepository.save(notification);
        }
    }
}
