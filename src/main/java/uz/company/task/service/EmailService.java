package uz.company.task.service;

import uz.company.task.dto.EmailDetails;

public interface EmailService {
    EmailDetails sendSimpleMail(EmailDetails details) throws Exception;
}
