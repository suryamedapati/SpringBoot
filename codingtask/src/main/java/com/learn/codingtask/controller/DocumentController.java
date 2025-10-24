package com.learn.codingtask.controller;

import com.learn.codingtask.dto.ApiResponse;
import com.learn.codingtask.entity.LeaveDocument;
import com.learn.codingtask.entity.LeaveRequest;
import com.learn.codingtask.service.LeaveDocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class DocumentController {

    private final LeaveDocumentService leaveService;

    public DocumentController(LeaveDocumentService leaveService) {
        this.leaveService = leaveService;
    }




    @PostMapping("/{leaveId}/documents")
    public ResponseEntity<ApiResponse<LeaveDocument>> uploadDocument(@PathVariable Long leaveId,
                                                                     @RequestParam("file") MultipartFile file,
                                                                     @RequestHeader("username") String username) throws Exception {
        LeaveDocument doc = leaveService.uploadDocument(leaveId, file, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Document uploaded successfully", doc));
    }


    @GetMapping("/{leaveId}/documents")
    public ResponseEntity<ApiResponse<List<LeaveDocument>>> getDocuments(@PathVariable Long leaveId) {
        List<LeaveDocument> docs = leaveService.getDocuments(leaveId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Documents fetched successfully", docs));
    }


    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        LeaveDocument doc = leaveService.getDocumentById(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .body(doc.getData());
    }
}

