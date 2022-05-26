package com.example.projekt.country;

public class CountryItem implements ItemType{
    private final String name;
    private final int id;
    private final boolean wantToSee;

    public CountryItem(String name, int id, boolean wantToSee) {
        this.name = name;
        this.id = id;
        this.wantToSee = wantToSee;
    }

    public String getName() {
        return name;
    }

    @Override
    public EItemType getType() {
        return EItemType.Country;
    }

    public int getId() {
        return id;
    }

    public boolean isWantToSee() {
        return wantToSee;
    }
}
