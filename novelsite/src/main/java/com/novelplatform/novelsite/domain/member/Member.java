package com.novelplatform.novelsite.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_id")
    private Long id;

    private String name;

    private String password;

    public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
