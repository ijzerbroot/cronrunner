/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ijzerbroot.cronrunner;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author fhoeben
 */
public class Mongodbo {
    
    public void getcronjobs(String mongourl) {
        MongoClient mongoClient;
        mongoClient = new MongoClient(new MongoClientURI(mongourl));

        MongoDatabase database;
        database = mongoClient.getDatabase("crondb");
        MongoCollection<Document> collection;

        collection = database.getCollection("cronjobs");

        BasicDBObject query;
        query = new BasicDBObject();
        MongoCursor<Document> cursor = collection.find(query).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }
}
