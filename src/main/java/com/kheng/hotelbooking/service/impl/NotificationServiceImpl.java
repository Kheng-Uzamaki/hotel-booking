package com.kheng.hotelbooking.service.impl;

import com.kheng.hotelbooking.dto.NotificationDTO;
import com.kheng.hotelbooking.entity.Notification;
import com.kheng.hotelbooking.enums.NotificationType;
import com.kheng.hotelbooking.repository.NotificationRepository;
import com.kheng.hotelbooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("Inside send email");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificationDTO.getRecipient());
        message.setSubject(notificationDTO.getSubject());
        message.setText(notificationDTO.getBody());

        mailSender.send(message);

        // Save to database
        Notification notificationToSave = Notification.builder()
                .recipient(notificationDTO.getRecipient())
                .subject(notificationDTO.getSubject())
                .body(notificationDTO.getBody())
                .bookingReference(notificationDTO.getBookingReference())
                .type(NotificationType.EMAIL)
                .build();
        notificationRepository.save(notificationToSave);

    }

    @Override
    public void sendSms() {

    }

    @Override
    public void sendWhatsapp() {

    }
}
