package com.mongo.demo;

import com.mongo.demo.dto.PersonInfo;
import com.mongo.demo.dto.PersonInfoAdapter;
import com.mongo.demo.mongoBase.MongoCollectionReactiveRepoImpl;
import com.mongo.demo.mongoBase.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.diagnostics.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static com.mongodb.diagnostics.logging.Loggers.getLogger;

public class MongoCollectionRepoImplReactiveTest {

    private MongoCollectionReactiveRepoImpl mongoInstance;
    private final Logger logger = getLogger(MongoCollectionRepoImplReactiveTest.class.getName());

    @BeforeEach
    public void initTest() {
        //-ea -Dmongodb.uri=localhost:27017
        String connectionString = System.getProperty("mongodb.uri");
        MongoConnection mongoConnection = new MongoConnection(connectionString,"aviad");
        this.mongoInstance = new MongoCollectionReactiveRepoImpl(mongoConnection, "mydbcollection");
    }

    @Test()
    public void testInsertData() {
        PersonInfo ella = new PersonInfo("599","ella","66");
        PersonInfo ella1 = new PersonInfo("445","ella","66");
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella));
        this.mongoInstance.insertElement(PersonInfoAdapter.toDocument(ella1));

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("_id","445");
        basicDBObject.append("name","ella");
        basicDBObject.append("age","66");

        AtomicBoolean isElementNonExitsDoc = new AtomicBoolean(false);
        this.mongoInstance
                .findElementByQueries(new BasicDBObject("_id", "5555"))
                .subscribe(e -> e.ifPresentOrElse(
                        document -> logger.info("find id " + document.toJson()),
                        () -> isElementNonExitsDoc.set(true)))
                .isDisposed();
        Assertions.assertTrue(isElementNonExitsDoc.get(),"document with 5555 is not exists");

        this.mongoInstance
                .findElementByQueries(basicDBObject)
                .subscribe(e -> e.ifPresentOrElse(
                        document -> logger.info("find id " + document.toJson()),
                        () -> isElementNonExitsDoc.set(true)))
                .isDisposed();
        Assertions.assertTrue(isElementNonExitsDoc.get(),"document exists");
    }

    @Test
    public void findElementsTest() {
        this.mongoInstance.findsElementsBySingleQuery(Filters.eq("name","ella"))
                .subscribe(cursor -> cursor.forEachRemaining(e -> logger.info(e.toJson())))
                .isDisposed();

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("_id","445");
        basicDBObject.append("name","ella");
        basicDBObject.append("age","66");
        this.mongoInstance.findsElementsByQueriesOptionOne(basicDBObject)
                .subscribe(cursor -> cursor.forEachRemaining(e -> logger.info(e.toJson())))
                .isDisposed();

        this.mongoInstance.findsElementsByQueriesOptionTwo(basicDBObject)
                .subscribe(document -> logger.info(document.toJson()))
                .isDisposed();
    }

    @Test
    public void printAllDocuments() {
        this.mongoInstance.documentsGetAllElements()
                .subscribe(document -> {
                logger.info(document.toJson());
        }).isDisposed();

        this.mongoInstance.iterableGetAllElements()
                .subscribe(document -> {
                    logger.info(document.toJson());
                }).isDisposed();
    }

//
//        boolean isDeleteElement = this.mongoInstance.deleteElement(Filters.eq("_id", ella.getId()))
//                .doOnError(e -> logger.info(e.getMessage()))
//                .subscribe()
//                .isDisposed();
//        Assertions.assertTrue(isDeleteElement,"documents with ella name and id "+ella.getId()+" delete");
//
//        this.mongoInstance.updateElement(Filters.eq("age", ella.getAge()), Updates.set("age","66"))
//                .subscribe(e -> {
//                    logger.info("update element status: " + e.wasAcknowledged());
//                }).isDisposed();
}
