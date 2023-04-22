package com.example.jococo;

import com.example.web_sty.jococo.Coco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CocoTest {

    @DisplayName("testWithMinju")
    @Test
    public void fooTest() {
        Coco coo = new Coco();
        String args = "Minju";

        String ret = coo.hello(args);
        // assertEquals :  두 객체의 값이 같은지 여부
        assertEquals("Kim", ret);
    }

}