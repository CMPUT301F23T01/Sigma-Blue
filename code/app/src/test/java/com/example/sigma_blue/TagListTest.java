package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import android.graphics.Color;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class TagListTest {
    @Mock
    private TagDB dummyHandler;
    @Mock
    private Color mockColor;

    private TagList classUnderTest;

    @Before
    public void initializeCUT() {
        this.classUnderTest = TagList.newInstance(dummyHandler);
    }

    @After
    public void tearDown() {
        this.classUnderTest = null;
    }

    @Test
    public void testHashMapOfTag() {
        HashMap<String, String> expected = new HashMap<>();
        expected.put(TagList.COLOR, "ffff0000");
        expected.put(TagList.LABEL, "Spider");

        Mockito.when(mockColor.toArgb()).thenReturn(0xFFFF0000);

        Tag input = new Tag("Spider", mockColor);


        assertEquals(expected, TagList.hashMapOfTag.apply(input));
    }

    @Test
    public void testTagOfDocument() {
        Tag expected = new Tag("abc", mockColor);
    }
}
