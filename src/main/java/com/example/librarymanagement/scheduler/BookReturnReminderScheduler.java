package com.example.librarymanagement.scheduler;

import com.example.librarymanagement.domain.dto.common.DataMailDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import com.example.librarymanagement.repository.BorrowReceiptRepository;
import com.example.librarymanagement.util.SendMailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
@Component
public class BookReturnReminderScheduler {

    private static final int maxOverdueDays = 3;

    private final SendMailUtil sendMailUtil;

    private final BorrowReceiptRepository borrowReceiptRepository;

    @Scheduled(cron = "0 15 16 * * ?")// Chạy lúc 15:25 mỗi ngày
    @Transactional
    public void sendReminderEmails() {
        LocalDate overdueThreshold = LocalDate.now().minusDays(maxOverdueDays);
        List<BorrowReceipt> overdueRecords = borrowReceiptRepository.findOverdueRecords(overdueThreshold);

        for (BorrowReceipt record : overdueRecords) {

            Map<String, Object> properties = new HashMap<>();
            properties.put("username", record.getReader().getFullName());
            properties.put("receiptNumber", record.getReceiptNumber());
            properties.put("returnDate", record.getDueDate());

            DataMailDto mailDto = new DataMailDto();
            mailDto.setTo(record.getReader().getEmail());
            mailDto.setSubject("Nhắc nhở trả sách");
            mailDto.setProperties(properties);

            CompletableFuture.runAsync(() -> {
                try {
                    sendMailUtil.sendMailWithHTML(mailDto, "reminderEmail.html");
                } catch (MessagingException e) {
                    log.error("Error sending reminder email to {}", record.getReader().getEmail(), e);
                }
            });
        }
    }
}
