package com.example.nicolai.clider.model;

import java.util.List;
import java.util.UUID;

public class SwipedClothes {

    public SwipedClothes(List<String> clotheIds) {
        this.clotheIds = clotheIds;
    }

    public List<String> getClotheIds() {
        return clotheIds;
    }

    public void setClotheIds(List<String> clotheIds) {
        this.clotheIds = clotheIds;
    }

    List<String> clotheIds;

}
