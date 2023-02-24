package uz.company.task.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.company.task.dto.EmailDetails;
import uz.company.task.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public EmailDetails sendSimpleMail(EmailDetails details) throws Exception {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return details;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            throw new Exception(e);
        }
    }
}
