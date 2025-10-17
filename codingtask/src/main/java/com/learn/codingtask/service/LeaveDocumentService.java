package com.learn.codingtask.service;

import com.learn.codingtask.entity.LeaveDocument;
import com.learn.codingtask.entity.LeaveRequest;
import com.learn.codingtask.repository.LeaveDocumentRepository;
import com.learn.codingtask.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveDocumentService {

    private final LeaveDocumentRepository documentRepository;
    private final LeaveRequestRepository leaveRepository;


    public LeaveDocumentService(LeaveDocumentRepository documentRepository, LeaveRequestRepository leaveRepository) {
        this.documentRepository = documentRepository;

        this.leaveRepository = leaveRepository;
    }



    public LeaveDocument uploadDocument(Long leaveId, MultipartFile file, String username) throws Exception {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (documentRepository.countByLeaveId(leaveId) >= 3) {
            throw new RuntimeException("Maximum 3 documents allowed per leave");
        }

        LeaveDocument doc = LeaveDocument.builder()
                .leave(leave)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .data(file.getBytes())
                .uploadedBy(username)
                .uploadedAt(LocalDateTime.now())
                .build();

        return documentRepository.save(doc);
    }

    public List<LeaveDocument> getDocuments(Long leaveId) {
        return documentRepository.findByLeaveId(leaveId);
    }

    public LeaveDocument getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }
}

