package eminimki.com.JTS.Common.Utilities.Manager;

import eminimki.com.JTS.Common.Utilities.Services.EmailService;
import eminimki.com.JTS.Entities.Employee;
import eminimki.com.JTS.Entities.Task;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailManager implements EmailService {



    private JavaMailSender emailSender;

    @Override
    @Async
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("eminay122@gmail.com");
        message.setSubject(subject);
        message.setText(text + " normalde olması gereken mail: "+ to);
        emailSender.send(message);

    }

    @Async
    public void sendEmployeeCreateMail(Employee employee , String temporaryPassword) {
        sendMail(employee.getMail(),
                "Your Account Has Been Created - Welcome!" ,
                "Hello "+employee.getUsername()+",\n" +
                        "\n" +
                        "Your account has been successfully created! \n" +
                        "\n" +
                        "Username: "+employee.getUsername()+"\n" +
                        "Password: "+temporaryPassword +"\n" +
                        "\n" +
                        "Please do not forget to change your password after logging in!\n" +
                        "\n" +
                        "We wish you a nice day,\n" +
                        "JTS Team");
    }

    @Async
    public void sendTaskCreateMail(Task task) {
        sendMail(task.getSender().getMail(),
                "JTS - " + task.getName(),
                task.getName()+" task created by "+ task.getSender().getUsername() + " on " + task.getCreatedTime());
    }



}
