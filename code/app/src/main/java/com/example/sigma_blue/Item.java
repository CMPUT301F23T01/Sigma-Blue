package com.example.sigma_blue;


import java.util.ArrayList;
import java.util.Date;

public class Item implements Comparable<Item>{

    private String name;
    private Date date;
    private String description;
    private String make;
    private String model;
    private float value;
    private String serialNumber;
    private String comment;


    private ArrayList<Tag> tags;

    /*TODO
        UNFINISHED ITEM OBJECT!!!
        decide the photograph storing method of the item
        (DONE)decide the type of the tag
            add these two attributes

     */


    /**
     * This is constructor of item object, take in required parameters only
     * @param name
     * This is name of the item
     * @param date
     * This is purchase date of the item
     * @param description
     * This is the description of the item
     * @param make
     * This is the make of the item
     * @param model
     * This is the model of the item
     * @param value
     * this is the estimated value of the item
     */
    public Item(String name, Date date, String description, String make, String model, float value) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.make = make;
        this.model = model;
        this.value = value;

        this.tags = new ArrayList<Tag>();

    }

    /**
     * This returns the name of item
     * @return
     * Return the name
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the name of item
     * @param name
     * This is a name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This returns the purchase date of item
     * @return
     * Return the date of purchase
     */
    public Date getDate() {
        return date;
    }

    /**
     * This sets the purchase date of item
     * @param date
     * This is a purchase date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This returns the description of item
     * @return
     * Return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This sets the description of item
     * @param description
     * This is a description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This returns the make of item
     * @return
     * Return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * This sets the make of item
     * @param make
     * This is a make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * This returns the model of item
     * @return
     * Return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * This sets the model of item
     * @param model
     * This is a model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * This returns the estimated value of item
     * @return
     * Return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * This sets the estimated value of item
     * @param value
     * This is a estimated to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * This returns the serial number of item
     * @return
     * Return the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * This sets the serial number of item
     * @param serialNumber
     * This is a serial number to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * This returns the comment of item
     * @return
     * Return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * This sets the comment of item
     * @param comment
     * This is a comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }


    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    /**
     * This sets the comment of item
     * @param tag
     * This is a tag to delete
     * @return boolean
     * if the tag is found and successfully deleted, return true
     * if the tag is not found, return false
     */
    public boolean deleteTag(Tag tag) {
        if (this.tags.contains((Object)tag)){
            this.tags.remove((Object)tag);
            return true;
        }
        return false;
    }

    public boolean hasTag(Tag tag){
        return this.tags.contains((Object)tag);
    }


    /**
     * This overrides equals method of super class
     * @param o
     * This the object for equals method
     */
    @Override
    public boolean equals (Object o) {

        // if is the object self
        if (this == o) {
            return true;
        }

        // if the object is not Item class, return false
        if (!(o instanceof Item)) {
            return false;
        }
        // two items are equals to each other if their name is the same
        Item I = (Item) o;
        return this.getName().equals(I.getName());
    }


    @Override
    public int compareTo(Item item) {
        return this.getName().compareTo(item.getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
