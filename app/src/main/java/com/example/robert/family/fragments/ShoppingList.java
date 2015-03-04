
package com.example.robert.family.fragments;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({
        "items"
})
@Data
public class ShoppingList {
    @JsonProperty("items")
    private List<ShoppingListItem> items = new ArrayList<>();
}