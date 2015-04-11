package org.noip.roberteriksson.family.main.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-07.
 */
@JsonPropertyOrder({
        "email",
        "name",
        "lastseen",
        "lastactivity"
})
@Data
public class ProfileJson {
    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String name;
    @JsonProperty("lastseen")
    private String lastSeen;
    @JsonProperty("lastactivity")
    private String lastActivity;
}
