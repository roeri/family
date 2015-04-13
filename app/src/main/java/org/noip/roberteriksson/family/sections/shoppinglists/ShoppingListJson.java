
package org.noip.roberteriksson.family.sections.shoppinglists;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({
    "items"
})
@Data
public class ShoppingListJson {
    @JsonProperty("items")
    private List<ShoppingListItemJson> items = new ArrayList<>();
}