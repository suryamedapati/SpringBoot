package com.learn.codingtask.service;

import com.learn.codingtask.entity.Employee;
import com.learn.codingtask.entity.LeaveRequest;
import com.learn.codingtask.exception.CustomExceptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.admin.emails}")
    private String[] adminEmails;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLeaveAppliedMail(Long leaveId, String userName, String startDate, String endDate, String reason) {
        try{
        for (String adminEmail : adminEmails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("New Leave Request Submitted");
            message.setText("User '" + userName +
                    "'\n\n has applied for leave with(Leave ID: " + leaveId +")" +
                    "\n\n from " + startDate + " to " + endDate +
                    ".\n\n Reason: " + reason+
                    "\n\n\nThanks,"+"\n"+
                            "Operations team.");
            mailSender.send(message);
        }
        } catch (Exception e){
            throw new CustomExceptions.UnauthorizedException("Failed to send reset email. Please check your email configuration.");
        }
    }
    public void passwordResetMail(Employee employee,String otp){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(employee.getEmail());
            message.setSubject("Password Reset OTP");
            message.setText("Hello "+employee.getFirstName()+
                    "."+"\n\nAs you requested for resetting the password, " +
                    "Here is your reset OPT: " + otp+"\n\n\n\n"+
                    "Thanks,"+"\n"+
                    "Operations team.");
            mailSender.send(message);
        } catch (Exception e) {
            // Wrap low-level SMTP error into your own exception
            throw new CustomExceptions.UnauthorizedException("Failed to send reset email. Please check your email configuration.");
        }
    }
    public void userCreatedMail(Employee employee){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(employee.getEmail());
            message.setSubject("Registered Successfully in Employee Leave Management System");
            message.setText("Hello " + employee.getFirstName() + ",\n\n" +
                    "Thank you for registering in Employee Leave Management System.\n\n" +
                    "Here are your login credentials:\n" +
                    "Username: " + employee.getUserName() + "\n" +
                    "To reset your password or set a new one, please visit the following link:\n" +
                    "http://localhost:8080/forgot-password\n\n" +
                    "Thanks,\n" +
                    "Operations Team");
            mailSender.send(message);
        } catch (Exception e) {
            // Wrap low-level SMTP error into your own exception
            throw new CustomExceptions.UnauthorizedException("Failed to send reset email. Please check your email configuration.");
        }
    }
    public void sendLeaveDecisionMail(Employee employee, LeaveRequest leave) {
        try{

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(employee.getEmail());
                message.setSubject("Leave Request Decision");
                message.setText("Hello, "+ employee.getFirstName()+
                        "\nyour leave (Leave ID: " +leave.getId() +
                        ") from " + leave.getStartDate() + " to " + leave.getEndDate() +
                        " has been "+leave.getStatus()+
                        "\n\nThanks,\n" +
                                "Operations Team.");

                mailSender.send(message);
        } catch (Exception e){
            throw new CustomExceptions.UnauthorizedException("Failed to send reset email. Please check your email configuration.");
        }
    }
}
