package com.novelplatform.novelsite.domain.member;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new LinkedHashSet<>();

    protected Member() {
        // for JPA
    }

    private Member(String username, String password, Set<MemberRole> roles) {
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
        this.roles = new LinkedHashSet<>(Objects.requireNonNull(roles, "roles must not be null"));
    }

    public static Member createAuthor(String username, String password) {
        return new Member(username, password, Set.of(MemberRole.AUTHOR));
    }

    public static Member createReader(String username, String password) {
        return new Member(username, password, Set.of(MemberRole.READER));
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<MemberRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void changePassword(String encodedPassword) {
        this.password = Objects.requireNonNull(encodedPassword, "encodedPassword must not be null");
    }

    public void addRole(MemberRole role) {
        this.roles.add(Objects.requireNonNull(role, "role must not be null"));
    }
}
