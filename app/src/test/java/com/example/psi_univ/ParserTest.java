package com.example.psi_univ;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void normalParsing() throws Exception {
        IcsToRooms a = new IcsToRooms();
        System.out.println(a.getRooms());
        assertEquals(4, 2 + 2);
    }
}