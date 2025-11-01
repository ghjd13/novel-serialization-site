package com.novelplatform.novelsite.domain.novel;

import java.time.LocalDateTime;

import com.novelplatform.novelsite.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "novels")
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String author;

    @Lob
    private String description;

    private String coverImagePath;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member createdBy;

    protected Novel() {
    }

    public Novel(String title, String author, String description, String coverImagePath, Member createdBy) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverImagePath = coverImagePath;
        this.createdBy = createdBy;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void updateCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }
}
