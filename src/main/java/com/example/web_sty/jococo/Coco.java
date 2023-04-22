package com.example.web_sty.jococo;


public class Coco {
    public String hello(String name) {
        switch (name) {
            case "hi":
                return "nice to meet you";
            case  "Minju":
                return "Kim";
            default:
                return "hello method ~";
        }
    }

    public String callFoo() {
        return "call Foo Class";
    }
}
