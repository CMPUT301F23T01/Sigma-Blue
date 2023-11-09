package com.example.sigma_blue;

import static android.graphics.Color.WHITE;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores information about a single tag.
 */
public class Tag implements Comparable<Tag>, IDatabaseItem<Tag>{
    private String tagText;
    private Color colour;
    private int colorInt;

    public Tag(String tagText, int colour) {
        this.tagText = tagText;
        this.colour = Color.valueOf(colour);
        colorInt = colour;
    }

    public Tag(String tagText, Color colour) {
        this.tagText = tagText;
        this.colour = colour;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public Color getColour() {
        return colour;
    }

    /**
     * Part of the comparable interface.
     * @param o the object to be compared.
     * @return an integer that is -1 if string label is less than, 0 if equals,
     * and 1 if greater than.
     */
    @Override
    public int compareTo(Tag o) {
        return this.tagText.compareTo(o.getTagText());
    }

    @Override
    public int hashCode() {
        return this.tagText.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag otherTag = (Tag) obj;
        return this.tagText.equals(otherTag.getTagText());
    }

    /**
     * Method that returns the document ID that defines this tag is unique in
     * the database.
     * @return the String used as the Doc ID.
     */
    @Override
    public String getDocID() {
        return this.tagText + this.colour;
    }

    public String getColourString() {
        ArrayList<Float> lst = new ArrayList<>();
        lst.add(colour.alpha());
        lst.add(colour.red());
        lst.add(colour.green());
        lst.add(colour.blue());
        return lst.stream()
                .map(v->String.valueOf(v))
                .reduce("", (s, e) -> s + e);
    }
}
