
package org.noip.roberteriksson.family.main.shoppinglists;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@JsonPropertyOrder({
    "items"
})
@Data
public class ListOfShoppingListsJson {
    @JsonProperty("items")
    private List<ListOfShoppingListsItemJson> items = new ArrayList<>();
}