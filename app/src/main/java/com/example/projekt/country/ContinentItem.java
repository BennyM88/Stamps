package com.example.projekt.country;

public class ContinentItem implements ItemType{
    private final String name;

    public ContinentItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public EItemType getType() {
        return EItemType.Header;
    }
}
