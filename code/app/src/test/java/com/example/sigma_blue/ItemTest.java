package com.example.sigma_blue;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;

import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.tag.Tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ItemTest {
    @Mock
    Color mockColor;

    @Test
    public void testGetTagNames() {
        ArrayList<Tag> arrayOfTags = new ArrayList<>();
        arrayOfTags.add(new Tag("one", mockColor));
        arrayOfTags.add(new Tag("two", mockColor));
        arrayOfTags.add(new Tag("three", mockColor));
        arrayOfTags.add(new Tag("four", mockColor));

        ArrayList<String> arrayOfNames = arrayOfTags.stream().map(e -> e
                .getTagText()).collect(Collectors.toCollection(ArrayList::new));

        Item testItem = Item.newInstance("name");
        testItem.setTags(arrayOfTags);

        testItem.getTags().stream().forEach(e -> {
            assert(arrayOfTags.contains(e));
        });

        testItem.getTagNames().stream().forEach(e -> {
            assert(arrayOfNames.contains(e));
        });
    }

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
        Item item = new Item("3090", date, "Testing",
                "Comment", "Make", "Evga",
                "GA102-220-A1", 799.99);
        item.addTag(new Tag("computer components", mockColor));

        assertEquals("computer components", (item.getTags().get(0)
                .getTagText()));

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
        Item item = new Item("3090", date, "Testing",
                "comment", "Testing2", "Evga",
                "GA102-220-A1", 799.99);
        item.addTag(new Tag("computer components", mockColor));

        assertEquals("computer components", (item.getTags().get(0)
                .getTagText()));

        item.addTag(new Tag("graphics card", mockColor));
        assertEquals("graphics card", (item.getTags().get(1).getTagText()));

        boolean success;
        success = item.deleteTag(new Tag("computer components", mockColor ));
        assertEquals("graphics card", (item.getTags().get(0).getTagText()));
        assertEquals(success, true);

        success = item.deleteTag(new Tag("computer components", mockColor));
        assertEquals(1, (item.getTags().size()));
        assertEquals(success, false);
    }
}
