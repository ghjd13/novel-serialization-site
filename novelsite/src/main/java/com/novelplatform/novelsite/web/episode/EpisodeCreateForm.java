package com.novelplatform.novelsite.web.episode;

import com.novelplatform.novelsite.domain.episode.Episode;
import com.novelplatform.novelsite.domain.novel.Novel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EpisodeCreateForm {

    @NotBlank(message = "회차 제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 10, message = "내용은 10자 이상이어야 합니다.")
    private String content;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // DTO를 Entity로 변환하는 헬퍼 메서드
    public Episode toEntity(Novel novel) {
        return new Episode(this.title, this.content, novel);
    }
}
