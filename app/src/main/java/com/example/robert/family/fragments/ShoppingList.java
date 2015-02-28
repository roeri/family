
package com.example.robert.family.fragments;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShoppingList {
    @JsonProperty("Items")
    private List<String> Items = new ArrayList<>();


}