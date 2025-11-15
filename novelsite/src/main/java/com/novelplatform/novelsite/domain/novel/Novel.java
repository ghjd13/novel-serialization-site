package com.novelplatform.novelsite.domain.novel;

import java.time.LocalDateTime;
import java.util.ArrayList; // 추가
import java.util.List; // 추가

import com.novelplatform.novelsite.domain.episode.Episode;
import com.novelplatform.novelsite.domain.member.Member;

import jakarta.persistence.*;

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

    // --- [추가] Episode와의 1:N 관계 ---
    // Novel이 삭제되면 관련된 Episode도 모두 삭제되도록 설정 (cascade, orphanRemoval)
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();
    // --- [추가] 끝 ---

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
    // --- [추가] Getter for episodes ---
    public List<Episode> getEpisodes() {
        return episodes;
    }
    // --- [추가] 끝 ---
}
