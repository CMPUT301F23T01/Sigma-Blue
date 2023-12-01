package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import android.graphics.Color;

import com.example.sigma_blue.entity.tag.Tag;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TagTest {
    @Mock
    private Color mockColor1, mockColor2, mockColor3;
    private List<Tag> cUTs;

    @Before
    public void createCUT() {
        cUTs = new ArrayList<>();

        cUTs.add(new Tag("testing", mockColor1));
        cUTs.add(new Tag("testing2", mockColor2));
        cUTs.add(new Tag("Testing", mockColor3));
    }

    /**
     * Setup the mock color interactions.
     */
    @Before
    public void mockColorInteraction() {
        Mockito.when(mockColor1.toArgb()).thenReturn(0xFF0000FF);
        Mockito.when(mockColor2.toArgb()).thenReturn(0xFFFF00FF);
        Mockito.when(mockColor3.toArgb()).thenReturn(0xFF00FFFF);
    }

    @After
    public void tearDown() {
        cUTs = null;
    }

    @Test
    public void testGetColourString() {
        String expected1 = "ff0000ff";
        String expected2 = "ffff00ff";
        String expected3 = "ff00ffff";

        assertEquals(expected1, cUTs.get(0).getColourString());
        assertEquals(expected2, cUTs.get(1).getColourString());
        assertEquals(expected3, cUTs.get(2).getColourString());
    }

    private HashMap<String, Object> hashMapFormatter(String label, String color) {
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("LABEL", label);
        ret.put("COLOR", color);
        return ret;
    }

    @Test
    public void testHashMapGeneration() {
        // Testing creation of hashmap objects from the tags
        HashMap<String, Object> expected1 = hashMapFormatter("testing",
                "ff0000ff");
        HashMap<String, Object> expected2 = hashMapFormatter("testing2",
                "ffff00ff");
        HashMap<String, Object> expected3 = hashMapFormatter("Testing",
                "ff00ffff");

        assertEquals(expected1, cUTs.get(0).getHashMapOfEntity().apply(cUTs
                .get(0)));
        assertEquals(expected2, cUTs.get(1).getHashMapOfEntity().apply(cUTs
                .get(1)));
        assertEquals(expected3, cUTs.get(2).getHashMapOfEntity().apply(cUTs
                .get(2)));
    }

    @Test
    public void testEquality() {
        assertFalse(cUTs.get(0).equals(cUTs.get(1)));
        assertFalse(cUTs.get(0).equals(cUTs.get(2)));
        assertFalse(cUTs.get(1).equals(cUTs.get(2)));

        assertEquals(cUTs.get(0), new Tag("testing", mockColor1));
        assertEquals(cUTs.get(0), new Tag("testing", mockColor2));
        assertEquals(cUTs.get(0), new Tag("testing", mockColor3));
    }

    @Test
    public void testComparable() {
        assertTrue(0 > cUTs.get(0).compareTo(cUTs.get(1)));
        assertTrue(0 < cUTs.get(0).compareTo(cUTs.get(2)));
        assertTrue(0 == cUTs.get(0).compareTo(cUTs.get(0)));
        assertTrue(0 == cUTs.get(0).compareTo(
                new Tag("testing", mockColor1)));
        assertTrue(0 == cUTs.get(0).compareTo(
                new Tag("testing", mockColor2)));
        assertTrue(0 == cUTs.get(0).compareTo(
                new Tag("testing", mockColor3)));
    }

}
