package com.example.sigma_blue;


import android.graphics.Color;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Stores information about a single tag.
 */
public class Tag implements Comparable<Tag>, IDatabaseItem<Tag>, Serializable {
    private String tagText;
    private Color colour;
    private boolean isChecked = false; // For the TagManager.

    public Tag(String tagText, int colour) {
        this.tagText = tagText;
        this.colour = Color.valueOf(colour);
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

    public void setColour(Color colour) {
        this.colour = colour;
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

    /**
     * Hash Code determines uniqueness based on the combination of both the tag
     * text and color.
     * @return int hash code based on the uniqueness requirements.
     */
    @Override
    public int hashCode() {
        return (this.tagText+Integer.toHexString(colour.toArgb())).hashCode();
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
        return (this.tagText.equals(otherTag.getTagText())
                && (colour.equals(otherTag.getColour())));
    }

    /**
     * Method that returns the document ID that defines this tag is unique in
     * the database.
     * @return the String used as the Doc ID.
     */
    @Override
    public String getDocID() {
        return this.tagText + Integer.toHexString(this.colour.toArgb());
    }

    public String getColourString() {
        return Integer.toHexString(colour.toArgb());
    }
}
