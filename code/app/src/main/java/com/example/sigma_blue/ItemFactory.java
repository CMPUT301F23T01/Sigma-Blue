package com.example.sigma_blue;

import java.util.Date;
import java.util.Optional;

/**
 * Factory class containing different manufacturing methods. Useful for making
 * items with some default values.
 */
public class ItemFactory {
    String factoryTitle;
    Date factoryDate;
    String factoryDescription;
    String factoryMake;
    String factoryModel;
    Float factoryValue;

    public static ItemFactory newInstance(Date d) {
        ItemFactory ret = new ItemFactory();
        ret.setFactoryDate(d);
        return ret;
    }

    /**
     * Private constructors. Please access this class by using the newInstance
     * method.
     */
    private ItemFactory() {
        factoryTitle = "";
        factoryDate = new Date();
        factoryDescription = "";
        factoryMake = "";
        factoryModel = "";
        factoryValue = 0f;

    }

    /**
     * Null construction as a default.
     * @return an Item object with every value set to empty (but not null)
     */
    public Item emptyItem() {
        return new Item("", new Date(), "", "","", "",
                0f);
    }

    /**
     * Null construction as a default.
     * @return an Item object with every value set to empty (but not null)
     */
    public Item templateItem() {
        return new Item("", getFactoryDate(), "", "","", "",
                0f);
    }

    /**
     * Factory Date setter
     * @param d is a Date object that will be used to generate items
     */
    public void setFactoryDate(Date d) {
        factoryDate = d;
    }

    /**
     * Gets the date if a factory one has not been set.
     * @return a Date object that will either have the current time, or the time
     * set on to the factory date.
     */
    private Date getFactoryDate() {
        return (Date) factoryDate.clone();  // Clone so no dangling reference.
    }
}
