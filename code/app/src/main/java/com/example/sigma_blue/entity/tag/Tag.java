package com.example.sigma_blue.entity.tag;


import android.graphics.Color;

import com.example.sigma_blue.database.IDatabaseItem;

import java.io.Serializable;


import java.util.HashMap;
import java.util.function.Function;

/**
 * Stores information about a single tag.
 */
public class Tag implements Comparable<Tag>, IDatabaseItem<Tag>, Serializable {
    private String tagText;
    private Color colour;
    public static final String LABEL = "LABEL", COLOR = "COLOR";

    public Tag(String tagText, int colour) {
        this.tagText = tagText;
        this.colour = Color.valueOf(colour);
    }

    public Tag(String tagText, Color colour) {
        this.tagText = tagText;
        this.colour = colour;
    }

    public Tag(String tagText, String colour) {
        this.tagText = tagText;
        this.colour = Color.valueOf(Color.parseColor("#" + colour));
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

    /**
     * Equals
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag otherTag = (Tag) obj;
        return (this.tagText.equals(otherTag.getTagText()));
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

    /**
     * return the hashmap conversion function
     * @return the function that convert the entity object into a hashmap for db
     */
    public Function<IDatabaseItem<Tag>, HashMap<String,
            Object>> getHashMapOfEntity() {
        return t -> {
            HashMap<String, Object> ret = new HashMap<>();
            Tag tag = t.getInstance();
            ret.put(LABEL, tag.getTagText());
            ret.put(COLOR, tag.getColourString());
            return ret;
        };
    }

    /**
     * Return the current instance with the correct type without explicit type
     * casting
     * @return the current instance, but as the required type.
     */
    @Override
    public Tag getInstance() {
        return this;
    }

    @Override
    public String toString() {
        return tagText;
    }
}
