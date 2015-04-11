package org.noip.roberteriksson.family.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-02.
 */
@JsonPropertyOrder({
    "email",
    "password"
})
@Data
public class UserToLoginJson {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}