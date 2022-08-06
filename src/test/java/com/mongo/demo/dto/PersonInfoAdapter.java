package com.mongo.demo.dto;

import com.mongo.demo.mongoBase.DocumentAdapter;
import org.bson.Document;

public class PersonInfoAdapter {

    private PersonInfoAdapter() { }

    public static Document toDocument(final PersonInfo personInfo) {
        return DocumentAdapter.toDocument(personInfo
                .append("_id", personInfo.getId())
                .append("name", personInfo.getName()))
                .append("age", personInfo.getAge());
    }
}
