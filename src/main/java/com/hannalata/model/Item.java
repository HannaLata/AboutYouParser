package com.hannalata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {

    @JsonProperty("id")
    private String itemId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("colors")
    private List<String> colors;

    @JsonProperty("price")
    private BigDecimal price;
}
