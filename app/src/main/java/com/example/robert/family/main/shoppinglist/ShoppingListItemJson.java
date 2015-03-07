package com.example.robert.family.main.shoppinglist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-01.
 */
@JsonPropertyOrder({
        "text",
        "checked"
})
@Data
public class ShoppingListItemJson {
    @JsonProperty("text")
    String text;
    @JsonProperty("checked")
    boolean checked;
}
