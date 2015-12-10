package com.github.lpedrosa;

public class Cat {

    public final String name;
    public final String colour;
    public final String description;

    public Cat(String name, String colour, String description) {
        this.name = name;
        this.colour = colour;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
