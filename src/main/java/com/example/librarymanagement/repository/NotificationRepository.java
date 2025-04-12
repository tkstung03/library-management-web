package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long>, JpaSpecificationExecutor<Notifications> {
    List<Notifications> findByUser_Id(String userId);

    Optional<Notifications> findByIdAndUser_Id(Long notificationId, String userId);
}
