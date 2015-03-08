
package com.example.robert.family.main.shoppinglist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@JsonPropertyOrder({
    "items"
})
@Data
public class ShoppingListsJson {
    @JsonProperty("items")
    private List<ShoppingListsItemJson> items = new ArrayList<>();
}