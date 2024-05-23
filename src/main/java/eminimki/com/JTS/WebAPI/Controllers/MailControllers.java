package eminimki.com.JTS.WebAPI.Controllers;

import eminimki.com.JTS.Business.requests.Common.EmailRequest;
import eminimki.com.JTS.Common.Utilities.Manager.EmailManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class MailControllers {
    EmailManager emailManager;

    @PostMapping("/api/mail/send")
    public void sendEmail(@RequestBody EmailRequest emailRequest){
        this.emailManager.sendMail(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getBody());
    }

}
