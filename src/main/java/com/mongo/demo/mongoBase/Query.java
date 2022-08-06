package com.mongo.demo.mongoBase;

import org.bson.conversions.Bson;

public class Query {

    private final String includeKey;
    private final String includeValue;
    private final Bson query;

    public Query(Bson query, String includeKey, String includeValue) {
        this.query = query;
        this.includeKey = includeKey;
        this.includeValue = includeValue;
    }

    public String getIncludeKey() { return includeKey; }
    public String getIncludeValue() { return includeValue; }
    public Bson getQuery() { return query; }
}
