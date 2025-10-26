package com.novelplatform.novelsite.domain.member;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Address {

    @NotBlank(message = "도시는 필수입니다.")
    private String city;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String street;

    @NotBlank(message = "우편번호는 필수입니다.")
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }
}
