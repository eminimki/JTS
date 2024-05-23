package eminimki.com.JTS.Common.Utilities.Services;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendMail(String to, String subject , String text);

}
