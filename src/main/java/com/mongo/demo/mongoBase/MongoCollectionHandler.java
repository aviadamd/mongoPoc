package com.mongo.demo.mongoBase;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoCollectionHandler {

    private final String dbName;
    private final MongoConnection mongoConnection;
    private final MongoCollection<Document> mongoCollection;
    private final Logger logger = Loggers.getLogger(MongoCollectionHandler.class.getName());

    public MongoCollectionHandler(MongoConnection mongoConnection, String collectionName) {
        this.mongoConnection = mongoConnection;
        this.mongoCollection = mongoConnection.getMongoDatabase().getCollection(collectionName);
        this.dbName = mongoConnection.getMongoDatabase().getName();
    }

    public Document findElementBy(BasicDBObject searchQuery) {
        try {
            return this.mongoCollection.find(searchQuery).first();
        } catch (Exception e) {
            logger.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }

    public FindIterable<Document> findElementsBy(BasicDBObject searchQuery) {
        try {
            return this.mongoCollection.find(searchQuery);
        } catch (Exception e) {
            logger.info("findElementsBy error: " + e.getMessage());
            return this.mongoCollection.find();
        }
    }

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

    public FindIterable<Document> iterableGetAllElements() {
        return this.mongoCollection.find();
    }

    public void deleteElement(Bson bson) {
        try {
            this.mongoCollection.deleteOne(bson);
        } catch (Exception e) {
            logger.error("deleteElement error: " + e.getMessage());
        }
    }

    public void updateElement(Bson from, Bson to) {
        try {
            this.mongoCollection.updateOne(from, to);
        } catch (Exception e) {
            logger.error("updateElement error: " + e.getMessage());
        }
    }

    public void insertElement(Document document) {
        try {
            this.mongoCollection.insertOne(document);
        } catch (Exception e) {
            logger.error("insertElement error: " + e.getMessage());
        }
    }

    public void insertElements(List<Document> documentList) {
        try {
            mongoCollection.insertMany(documentList);
        } catch (Exception e) {
            logger.error("insertElements error: " + e.getMessage());
        }
    }

    public void replaceElement(String key, Object oldObject, Document document) {
        try {
            Document find = new Document(key, oldObject);
            mongoCollection.replaceOne(find, document);
        } catch (Exception e) {
            logger.error("replaceElement error: " + e.getMessage());
        }
    }

    public void replaceElements(HashMap<String,Object> documents, Document document) {
        try {
            for (Map.Entry<String, Object> entry : documents.entrySet()) {
                Document find = new Document(entry.getKey(), entry.getValue());
                mongoCollection.replaceOne(find, document);
            }
        } catch (Exception e) {
            logger.error("replaceElements error: " + e.getMessage());
        }
    }

    public String getText(Document document, String name) {
        try {
            return document.getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    public void dropDataBase() {
        this.mongoConnection
                .getMongoClient()
                .dropDatabase(this.dbName);
    }

    public void close() {
        this.mongoConnection
                .getMongoClient()
                .close();
    }
}
