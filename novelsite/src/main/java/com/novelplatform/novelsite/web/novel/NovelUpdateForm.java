package com.novelplatform.novelsite.web.novel;


import com.novelplatform.novelsite.domain.novel.Novel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NovelUpdateForm {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "작가명을 입력해주세요.")
    @Size(max = 50, message = "작가명은 50자 이내여야 합니다.")
    private String author;

    @Size(max = 2000, message = "소개는 2000자 이내여야 합니다.")
    private String description;

    public NovelUpdateForm(){
    }
    public NovelUpdateForm(Novel novel) {
        this.title = novel.getTitle();
        this.author = novel.getAuthor();
        this.description = novel.getDescription();
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
