package com.mongo.demo.mongoBase;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class MongoCollectionReactiveRepoImpl implements
        MongoCollectionReactiveRepo.OnCloseDb, MongoCollectionReactiveRepo.OnUpdate,
        MongoCollectionReactiveRepo.OnSearchBy, MongoCollectionReactiveRepo.OnSearch {

    private final String dbName;
    private final MongoConnection mongoConnection;
    private final MongoCollection<Document> mongoCollection;
    private final Logger logger = Loggers.getLogger(MongoCollectionRepoImpl.class.getName());

    public MongoCollectionReactiveRepoImpl(MongoConnection mongoConnection, String collectionName) {
        this.mongoConnection = mongoConnection;
        this.mongoCollection = mongoConnection.getMongoDatabase().getCollection(collectionName);
        this.dbName = mongoConnection.getMongoDatabase().getName();
    }

    @Override
    public Mono<Boolean> deleteElement(Bson bson) {
        try {
            return Mono.just(this.mongoCollection.deleteOne(bson).wasAcknowledged());
        } catch (Exception e) {
            logger.info("deleteElement error: " + e.getMessage());
            return Mono.just(false);
        }
    }

    @Override
    public Mono<Document> insertElement(Document document) {
        try {
            this.mongoCollection.insertOne(document);
            return Mono.just(document);
        } catch (Exception e) {
            logger.info("insertElement error: " + e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<List<Document>> insertElements(List<Document> documentList) {
        try {
            this.mongoCollection.insertMany(documentList);
            return Mono.just(documentList);
        } catch (Exception e) {
            logger.info("insertElement error: " + e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<UpdateResult> updateElement(Bson from, Bson to) {
        try {
            return Mono.just(this.mongoCollection.updateOne(from, to));
        } catch (Exception e) {
            logger.info("updateElement error: " + e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<UpdateResult> replaceElement(String key, Object oldObject, Document document) {
        try {
            return Mono.just(this.mongoCollection.replaceOne(new Document(key, oldObject), document));
        } catch (Exception e) {
            logger.info("replaceElement error: " + e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<Map.Entry<String, Object>> replaceElements(HashMap<String, Object> documents, Document document) {
        try {
            return Flux.fromIterable(documents.entrySet())
                    .next()
                    .doOnNext(e -> this.mongoCollection.replaceOne(new Document(e.getKey(), e.getValue()), document));
        } catch (Exception e) {
            logger.info("replaceElements error: " + e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<Optional<Document>> findElementByQuery(Bson searchQuery) {
        try {
            return Mono.just(Optional.ofNullable(this.mongoCollection.find(searchQuery).first()));
        } catch (Exception e) {
            logger.error("findElementBy error :" + e.getMessage());
            return Mono.just(Optional.empty());
        }
    }

    @Override
    public Mono<Optional<Document>> findElementByQueries(BasicDBObject searchQuery) {
        try {
            return Mono.just(Optional.ofNullable(this.mongoCollection.find(searchQuery).first()));
        } catch (Exception e) {
            logger.error("findElementBy error :" + e.getMessage());
            return Mono.just(Optional.empty());
        }
    }

    @Override
    public Flux<MongoCursor<Document>> findsElementsBySingleQuery(Bson query) {
        try {
            return Flux.just(this.mongoCollection.find(query).iterator());
        } catch (Exception e) {
            logger.error("findElementsBy error: " +  e.getMessage());
            return Flux.empty();
        }
    }

    @Override
    public Flux<MongoCursor<Document>> findsElementsByQueriesOptionOne(BasicDBObject searchQuery) {
        try {
            return Flux.just(this.mongoCollection.find(searchQuery).iterator());
        } catch (Exception e) {
            logger.error("findElementsBy error: " +  e.getMessage());
            return Flux.empty();
        }
    }

    @Override
    public Flux<Document> findsElementsByQueriesOptionTwo(BasicDBObject searchQuery) {
        try {
            return Flux.fromIterable(this.mongoCollection.find(searchQuery));
        } catch (Exception e) {
            logger.error("findElementsBy error: " + e.getMessage());
            return Flux.empty();
        }
    }

    @Override
    public Flux<Document> documentsGetAllElements() {
        List<Document> documentList = new ArrayList<>();
        try {
            for (Document document : this.mongoCollection.find()) {
                documentList.add(document);
            }
        } catch (Exception e) {
            logger.info("documentsGetAllElements error: " + e.getMessage());
        }
        return Flux.fromIterable(documentList);
    }

    @Override
    public Flux<Document> iterableGetAllElements() {
        try {
            return Flux.fromIterable(this.mongoCollection.find());
        } catch (Exception e) {
            logger.error("iterableGetAllElements error: " + e.getMessage());
            return Flux.empty();
        }
    }

    @Override
    public void dropDataBase() {
        this.mongoConnection
                .getMongoClient()
                .dropDatabase(this.dbName);
    }

    @Override
    public void close() {
        this.mongoConnection
                .getMongoClient()
                .close();
    }
}
