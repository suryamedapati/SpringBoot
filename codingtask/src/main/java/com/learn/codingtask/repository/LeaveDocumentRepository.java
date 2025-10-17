package com.learn.codingtask.repository;

import com.learn.codingtask.entity.LeaveDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveDocumentRepository extends JpaRepository<LeaveDocument, Long> {

    List<LeaveDocument> findByLeaveId(Long leaveId);

    LeaveDocument findById(long documentId);

    int countByLeaveId(Long leaveId);
}