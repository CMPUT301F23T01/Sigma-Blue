package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;

import android.graphics.Color;

import com.example.sigma_blue.entity.tag.Tag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagTest {
    @Mock
    private Color mockColor;
    private Tag cUT;

    @Before
    public void createCUT() {
        cUT = new Tag("testing", mockColor);
    }

    /* With the way that context works, it's a little useless to do a test with
    * mocking, since some of the method really relies on the context working */
    @Test
    public void testGetColourString() {
        String expected = "ff0000ff";

        Mockito.when(mockColor.toArgb()).thenReturn(0xFF0000FF);

        assertEquals(expected, cUT.getColourString());
    }

}
