package com.novelplatform.novelsite.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String loginId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.USER;

    protected Member() {
    }

    public Member(String loginId, String name, String password, Address address) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = MemberRole.USER;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Address getAddress() {
        return address;
    }

    public MemberRole getRole() {
        return role;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
