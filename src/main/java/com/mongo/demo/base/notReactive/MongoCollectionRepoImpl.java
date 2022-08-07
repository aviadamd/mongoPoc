package com.mongo.demo.base.notReactive;

import com.mongo.demo.base.MongoBase;
import com.mongo.demo.base.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.diagnostics.logging.Loggers.getLogger;

public class MongoCollectionRepoImpl implements MongoCollectionRepo {

    private final MongoBase mongoBase;
    private final Logger logger = getLogger(MongoCollectionRepoImpl.class.getName());

    public MongoCollectionRepoImpl(
            MongoConnection mongoConnection, String collectionName) {
        this.mongoBase = new MongoBase(mongoConnection, collectionName);
    }

    @Override
    public Document findElementBy(BasicDBObject searchQuery) {
        try {
            return this.mongoBase.getMongoCollection().find(searchQuery).first();
        } catch (Exception e) {
            logger.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }

    @Override
    public FindIterable<Document> findElementsBy(BasicDBObject searchQuery) {
        try {
            return this.mongoBase.getMongoCollection().find(searchQuery);
        } catch (Exception e) {
            logger.info("findElementsBy error: " + e.getMessage());
            return this.mongoBase.getMongoCollection().find();
        }
    }

    @Override
    public List<Document> documentsGetAllElements() {
        List<Document> documentList = new ArrayList<>();
        try {
            for (Document document : this.iterableGetAllElements()) {
                documentList.add(document);
            }
        } catch (Exception e) {
            logger.info("documentsGetAllElements error: " + e.getMessage());
            return documentList;
        }
        return documentList;
    }

    @Override
    public FindIterable<Document> iterableGetAllElements() {
        return this.mongoBase.getMongoCollection().find();
    }

    @Override
    public void deleteElement(Bson bson) {
        try {
            this.mongoBase.getMongoCollection().deleteOne(bson);
        } catch (Exception e) {
            logger.error("deleteElement error: " + e.getMessage());
        }
    }

    @Override
    public void updateElement(Bson from, Bson to) {
        try {
            this.mongoBase.getMongoCollection().updateOne(from, to);
        } catch (Exception e) {
            logger.error("updateElement error: " + e.getMessage());
        }
    }

    @Override
    public void insertElement(Document document) {
        try {
            this.mongoBase.getMongoCollection().insertOne(document);
        } catch (Exception e) {
            logger.error("insertElement error: " + e.getMessage());
        }
    }

    @Override
    public void insertElements(List<Document> documentList) {
        try {
            mongoBase.getMongoCollection().insertMany(documentList);
        } catch (Exception e) {
            logger.error("insertElements error: " + e.getMessage());
        }
    }

    @Override
    public void replaceElement(String key, Object oldObject, Document document) {
        try {
            Document find = new Document(key, oldObject);
            mongoBase.getMongoCollection().replaceOne(find, document);
        } catch (Exception e) {
            logger.error("replaceElement error: " + e.getMessage());
        }
    }

    @Override
    public void replaceElements(HashMap<String,Object> documents, Document document) {
        try {
            for (Map.Entry<String, Object> entry : documents.entrySet()) {
                Document find = new Document(entry.getKey(), entry.getValue());
                mongoBase.getMongoCollection().replaceOne(find, document);
            }
        } catch (Exception e) {
            logger.error("replaceElements error: " + e.getMessage());
        }
    }

    @Override
    public String getText(Document document, String name) {
        try {
            return document.getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void dropDataBase() {
        this.mongoBase.getMongoConnection()
                .getMongoClient()
                .dropDatabase(this.mongoBase.getDbName());
    }

    @Override
    public void close() {
        this.mongoBase.getMongoConnection()
                .getMongoClient()
                .close();
    }
}
