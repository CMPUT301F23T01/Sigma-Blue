package com.example.sigma_blue;

import android.graphics.Color;

/**
 * Stores information about a single tag.
 */
public class Tag implements Comparable<Tag>{
    private String tagText;
    private Color colour;

    public Tag(String tagText, int colour) {
        this.tagText = tagText;
        //this.colour = Color.valueOf(colour); // requires API 26
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
}
