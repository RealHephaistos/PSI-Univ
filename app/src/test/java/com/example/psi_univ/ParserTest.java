package com.example.psi_univ;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {
    @Test
    public void normalParsing() throws Exception {
        IcsToRooms a = new IcsToRooms();
        System.out.print(a.getRooms());
        assertEquals(4, 2 + 2);
    }
}