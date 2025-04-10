package com.kheng.hotelbooking.service;

import com.kheng.hotelbooking.dto.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO);
    void sendSms();
    void sendWhatsapp();
}
