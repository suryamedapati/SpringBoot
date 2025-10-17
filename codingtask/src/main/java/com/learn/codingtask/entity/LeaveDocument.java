package com.learn.codingtask.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "leave_documents")
public class LeaveDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id", nullable = false)
    private LeaveRequest leave;

    private String fileName;
    private String fileType;
    private Long fileSize;

    @Lob
    private byte[] data;

    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
