package org.noip.roberteriksson.family.sections.shoppinglists;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Created by robert on 2015-03-01.
 */
@JsonPropertyOrder({
        "id",
        "sequence",
        "name"
})
@Data
public class ListOfShoppingListsItemJson {
    @JsonProperty("id")
    int id;
    @JsonProperty("sequence")
    int sequence;
    @JsonProperty("name")
    String name;
}
