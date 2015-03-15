package com.example.robert.family.main.shoppinglist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-01.
 */
@JsonPropertyOrder({
        "id",
        "name"
})
@Data
public class ListOfShoppingListsItemJson {
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
}
