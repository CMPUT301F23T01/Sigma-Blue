package com.example.sigma_blue;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import android.graphics.Color;

import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.tag.Tag;

import org.junit.After;
import org.junit.Before;
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
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ItemTest {
    @Mock
    Color mockColor;
    List<Tag> arrayOfTags;

    Item cUT;

    @Before
    public void characterizeMockColor() {
        Mockito.when(mockColor.toArgb()).thenReturn(0xFF0000FF);
    }

    @Before
    public void makeCUT() {
        cUT = new Item("Test");
    }

    @Before
    public void makeArrayOfTags() {
        // Test tags
        arrayOfTags = new ArrayList<>();

        arrayOfTags.add(new Tag("one", mockColor));
        arrayOfTags.add(new Tag("two", mockColor));
        arrayOfTags.add(new Tag("three", mockColor));
        arrayOfTags.add(new Tag("four", mockColor));
    }

    @After
    public void tearDown() {
        arrayOfTags = null;
        cUT = null;
    }

    @Test
    public void testMake() {
        cUT.setMake("makeTest");

        assertEquals("makeTest", cUT.getMake());

        cUT.setMake("make2");

        assertEquals("make2",  cUT.getMake());

        cUT.setMake(null);

        assertNull(cUT.getMake());
    }

    @Test
    public void testModel() {
        cUT.setModel("modelTest1");

        assertEquals("modelTest1", cUT.getModel());

        cUT.setModel("modelTest2");

        assertEquals("modelTest2",  cUT.getModel());

        cUT.setModel(null);

        assertNull(cUT.getModel());
    }

    @Test
    public void testName() {
        assertEquals("Test", cUT.getName());

        assertThrows(IllegalArgumentException.class, () -> cUT.setName(null));

    }

    @Test
    public void testGetTags() {
        Item testItem = Item.newInstance("name");

        testItem.setTags(arrayOfTags);  // Setting tags

        assertTrue(testItem.hasTag(arrayOfTags.get(0)));

        testItem.getTags().forEach(ele ->
                assertTrue(arrayOfTags.contains(ele)));

    }

    @Test
    public void testDescription() {
        String sampleDescription = "Sample Description";

        cUT.setDescription(null);

        assertNull(cUT.getDescription());

        cUT.setDescription(sampleDescription);
        assertEquals(sampleDescription, cUT.getDescription());
    }

    @Test
    public void testDate() {
        Date testingDate = new Date();

        cUT.setDate(testingDate);
        assertEquals(testingDate, cUT.getDate());
    }

    @Test
    public void testSerialNumber() {
        String testSerial = "dlsakjdslajk";

        cUT.setSerialNumber(null);
        assertNull(cUT.getSerialNumber());

        cUT.setSerialNumber(testSerial);
        assertEquals(testSerial, cUT.getSerialNumber());
    }

    @Test
    public void testComment() {
        String testComment = "comment";

        cUT.setComment(null);
        assertNull(cUT.getComment());

        cUT.setComment(testComment);
        assertEquals(testComment, cUT.getComment());
    }

    @Test
    public void testCopyConstructor() {
        Item copy = new Item(cUT);

        assertEquals(copy, cUT);
    }

    @Test
    public void testImagePath() {
        String path1 = "path1";
        String path2 = "path2";
        String path3 = "path3";

        assertEquals(cUT.getImagePaths().size(), 0);
        cUT.addImagePath(path1);
        assertTrue(cUT.getImagePaths().contains(path1));
        cUT.addImagePath(path2);
        cUT.addImagePath(path3);

        assertEquals(3, cUT.getImagePaths().size());

        assertTrue(cUT.removeImagePath(path1));
        assertFalse(cUT.removeImagePath(path1));

        assertFalse(cUT.getImagePaths().contains(path1));
        assertEquals(2, cUT.getImagePaths().size());
    }

    @Test
    public void testGetTagDocIDs() {
        ArrayList<String> arrayOfTagDocIDs = new ArrayList<>();
        arrayOfTagDocIDs.add("oneff0000ff");
        arrayOfTagDocIDs.add("twoff0000ff");
        arrayOfTagDocIDs.add("threeff0000ff");
        arrayOfTagDocIDs.add("fourff0000ff");

        Item testItem = Item.newInstance("name");

        testItem.setTags(arrayOfTags);  // Setting tags

        testItem.getTagDocIDs().forEach(e -> {
            assertTrue(arrayOfTagDocIDs.contains(e));
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
