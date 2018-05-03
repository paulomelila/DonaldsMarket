package com.gmail.paulovitormelila.donaldsmarket;

public class ProductSection {
    private String name;
    private int sectionImageResource;
    private String description;

    public ProductSection(String name, int sectionImageResource, String description) {
        this.name = name;
        this.sectionImageResource = sectionImageResource;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSectionImageResource() {
        return sectionImageResource;
    }

    public void setSectionImageResource(int sectionImageResource) {
        this.sectionImageResource = sectionImageResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
