package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;

import android.graphics.Color;

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

    @Test
    public void testGetColourString() {
        String expected = "12712700";
        Mockito.when(mockColor.alpha()).thenReturn(127f);
        Mockito.when(mockColor.red()).thenReturn(127f);
        Mockito.when(mockColor.green()).thenReturn(127f);
        Mockito.when(mockColor.blue()).thenReturn(127f);

        assertEquals(expected, cUT.getColourString());
    }

}
