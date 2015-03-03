package com.example.robert.family;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-02.
 */
@JsonPropertyOrder({
    "name",
    "password"
})
@Data
public class User {
    @JsonProperty("name")
    String name;
    @JsonProperty("password")
    String password;
}
