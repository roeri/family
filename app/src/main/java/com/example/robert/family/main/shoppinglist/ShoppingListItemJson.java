package com.example.robert.family.main.shoppinglist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-01.
 */
@JsonPropertyOrder({
        "id",
        "shoppinglists_id",
        "users_id",
        "sequence",
        "text",
        "checked"
})
@Data
public class ShoppingListItemJson {
    @JsonProperty("id")
    int id;
    @JsonProperty("shoppinglists_id")
    int shoppingListsId;
    @JsonProperty("users_id")
    int usersId;
    @JsonProperty("sequence")
    int sequence;
    @JsonProperty("text")
    String text;
    @JsonProperty("checked")
    boolean checked;
}
