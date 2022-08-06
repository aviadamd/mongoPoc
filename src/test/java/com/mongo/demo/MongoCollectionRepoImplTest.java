package com.mongo.demo;

import com.mongo.demo.dto.PersonInfo;
import com.mongo.demo.dto.PersonInfoAdapter;
import com.mongo.demo.mongoBase.MongoCollectionRepoImpl;
import com.mongo.demo.mongoBase.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.diagnostics.logging.Logger;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.mongodb.diagnostics.logging.Loggers.getLogger;

public class MongoCollectionRepoImplTest {

    private MongoCollectionRepoImpl mongoInstance;
    private final Logger logger = getLogger(MongoCollectionRepoImplTest.class.getName());

    @BeforeEach
    public void initTest() {
        //-ea -Dmongodb.uri=localhost:27017
        String connectionString = System.getProperty("mongodb.uri");
        MongoConnection mongoConnection = new MongoConnection(connectionString,"aviad");
        this.mongoInstance = new MongoCollectionRepoImpl(mongoConnection, "mydbcollection");
    }

    @Test()
    public void testInsertAndDeleteEmployee() {
        PersonInfo ella = new PersonInfo("444","ella","31");
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella));
        Document ellaList = this.mongoInstance.findElementBy(new BasicDBObject("_id", ella.getId()));
        logger.info("ella obj: " + ellaList.toJson());
        this.mongoInstance.deleteElement(Filters.eq("_id", ella.getId()));
    }

    @Test
    public void testInsertAndUpdateEmployee() {
        PersonInfo ella = new PersonInfo("445","ella","31");
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella));
        this.mongoInstance.updateElement(Filters.eq("age", ella.getAge()), Updates.set("age","66"));
        Document ellaDoc = this.mongoInstance.findElementBy(new BasicDBObject("_id", ella.getId()));

        logger.info(ellaDoc.getString("_id"));
        logger.info(ellaDoc.getString("name"));
        logger.info(ellaDoc.getString("age"));

        Assertions.assertTrue(ellaDoc.containsValue("66"), "ella age updated to 66");

        this.mongoInstance.documentsGetAllElements().forEach(e -> logger.info(e.toString()));
        Optional<Document> singleFromIterable = Optional.ofNullable(this.mongoInstance.iterableGetAllElements()
                .filter(Filters.eq("_id", ella.getId()))
                .first());
        singleFromIterable.ifPresent(ele -> logger.info(ele.toJson()));
    }

}
