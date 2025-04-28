package com.example.librarymanagement.util;

import com.example.librarymanagement.domain.dto.common.DataMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMailUtil {
    JavaMailSender mailSender;

    TemplateEngine templateEngine;

    /**
     * Gửi mail với file html
     *
     * @param mail   Thông tin của mail cần gửi
     * @param template Tên file html trong folder resources/template
     *                 Example: Index.html
     */
    public void sendMailWithHTML(DataMailDto mail,  String template) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());

        // Xử lý nội dung HTML với Thymeleaf
        Context context = new Context();
        if (mail.getProperties() != null) {
            context.setVariables(mail.getProperties());
        }
        String htmlContent = templateEngine.process(template, context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendMailWithAttachment(DataMailDto mail, MultipartFile[] files) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(), true);

        // Đính kèm file
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }
        }

        mailSender.send(mimeMessage);
    }
}
