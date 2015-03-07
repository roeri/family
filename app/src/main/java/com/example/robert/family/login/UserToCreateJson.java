package com.example.robert.family.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Builder;

/**
 * Created by robert on 2015-03-02.
 */
@JsonPropertyOrder({
    "email",
    "password"
})
@Data
public class UserToCreateJson {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}