package com.example.sigma_blue;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemTest {

    @Test
    public void testItemAddTags() {
        // build a mock item
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = formatter.parse("2022-01");
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        Item item = new Item("3090", date, "Testing", "Evga", "GA102-220-A1", (float)799.99);
        item.addTag(new Tag("computer components", new Color()));

        assertEquals("computer components", (item.getTags().get(0).getTagText()));

        item.addTag(new Tag("graphics card", new Color()));
        assertEquals("graphics card", (item.getTags().get(1).getTagText()));

    }

    @Test
    public void testItemDeleteTags() {
        // build a mock item
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = formatter.parse("2022-01");
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        Item item = new Item("3090", date, "Testing", "Evga", "GA102-220-A1", (float)799.99);
        item.addTag(new Tag("computer components", new Color()));

        assertEquals("computer components", (item.getTags().get(0).getTagText()));

        item.addTag(new Tag("graphics card", new Color()));
        assertEquals("graphics card", (item.getTags().get(1).getTagText()));


        boolean success;
        success = item.deleteTag(new Tag("computer components", new Color()));
        assertEquals("graphics card", (item.getTags().get(0).getTagText()));
        assertEquals(success, true);

        success = item.deleteTag(new Tag("computer components", new Color()));
        assertEquals(1, (item.getTags().size()));
        assertEquals(success, false);
    }
}
