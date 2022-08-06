package com.mongo.demo;

import com.mongo.demo.dto.PersonInfo;
import com.mongo.demo.dto.PersonInfoAdapter;
import com.mongo.demo.mongoBase.MongoCollectionHandler;
import com.mongo.demo.mongoBase.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.diagnostics.logging.Logger;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mongodb.diagnostics.logging.Loggers.getLogger;

public class MongoCollectionHandlerTest {

    private MongoCollectionHandler mongoInstance;
    private final Logger logger = getLogger(MongoCollectionHandlerTest.class.getName());

    @BeforeEach
    public void initTest() {
        String connectionString = System.getProperty("mongodb.uri");
        MongoConnection mongoConnection = new MongoConnection(connectionString,"aviad");
        this.mongoInstance = new MongoCollectionHandler(mongoConnection, "mydbcollection");
    }

    @Test()
    public void testInsertAndDeleteEmployee() {
        PersonInfo ella = new PersonInfo("44","ella","31");
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella));
        Document ellaList = this.mongoInstance.findElementBy(new BasicDBObject("_id", ella.getId()));
        logger.info("ella obj: " + ellaList.toJson());
        this.mongoInstance.deleteElement(Filters.eq("_id", ella.getId()));
    }

    @Test
    public void testInsertAndUpdateEmployee() {
        PersonInfo ella = new PersonInfo("44","ella","31");
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella));
        this.mongoInstance.updateElement(Filters.eq("age", ella.getAge()), Updates.set("age","66"));
        Document ellaDoc = this.mongoInstance.findElementBy(new BasicDBObject("_id", ella.getId()));

        logger.info(ellaDoc.getString("_id"));
        logger.info(ellaDoc.getString("name"));
        logger.info(ellaDoc.getString("age"));

        Assertions.assertTrue(ellaDoc.containsValue("66"), "ella age updated to 66");

        this.mongoInstance.documentsGetAllElements().forEach(e -> logger.info(e.toString()));
        Document singleFromIterable = this.mongoInstance.iterableGetAllElements()
                .filter(Filters.eq("_id", ella.getId()))
                .first();
        logger.info(singleFromIterable.toJson());
    }

}
