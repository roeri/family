package com.example.robert.family;

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
    "name",
    "password"
})
@Data
public class UserJson {
    @JsonProperty("name")
    String name;
    @JsonProperty("password")
    String password;
}