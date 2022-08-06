package com.mongo.demo.mongoBase;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MongoCollectionReactiveRepo {

    public interface OnUpdate {
        Mono<Boolean> deleteElement(Bson bson);
        Mono<Document> insertElement(Document document);
        Mono<List<Document>> insertElements(List<Document> documentList);
        Mono<UpdateResult> updateElement(Bson from, Bson to);
        Mono<UpdateResult> replaceElement(String key, Object oldObject, Document document);
        Mono<Map.Entry<String, Object>> replaceElements(HashMap<String, Object> documents, Document document);
    }

    public interface OnSearchBy {
        Mono<Optional<Document>> findElementByQueries(BasicDBObject searchQuery);
        Flux<MongoCursor<Document>> findsElementsBySingleQuery(Bson searchQuery);
        Flux<MongoCursor<Document>> findsElementsByQueriesOptionOne(BasicDBObject searchQuery);
        Flux<Document> findsElementsByQueriesOptionTwo(BasicDBObject searchQuery);
    }


    public interface OnSearch {
        Flux<Document> documentsGetAllElements();
        Flux<Document> iterableGetAllElements();
    }

    public interface OnCloseDb {
        void dropDataBase ();
        void close ();
    }
}
