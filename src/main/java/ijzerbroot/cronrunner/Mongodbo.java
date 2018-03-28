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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fhoeben
 */
class Mongodbo {

    MongoCursor<Document> runQuery(String mongourl, BasicDBObject query) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient;
        mongoClient = new MongoClient(new MongoClientURI(mongourl));
        MongoDatabase database;
        database = mongoClient.getDatabase("crondb");
        MongoCollection<Document> collection;
        collection = database.getCollection("cronjobs");
        MongoCursor<Document> cursor = collection.find(query).iterator();
        mongoClient.close();
        return cursor;
    }

    void insertLog(String mongourl, Document logentry) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        MongoClient mongoClient;
        mongoClient = new MongoClient(new MongoClientURI(mongourl));
        MongoDatabase database;
        database = mongoClient.getDatabase("crondb");
        MongoCollection<Document> collection;
        collection = database.getCollection("cronlog");
        collection.insertOne(logentry);
        mongoClient.close();
    }

    List<String> getcronjobs(String mongourl) {

        List<String> cronjoblist = new ArrayList<String>();
        String cronjobline;

        BasicDBObject query;
        query = new BasicDBObject();
        MongoCursor<Document> cursor = runQuery(mongourl, query);
        try {
            while (cursor.hasNext()) {
           //     System.out.println(cursor.next().toJson());
                Document cronjobentry = cursor.next();
                cronjobline = cronjobentry.get("schedule") + " cronrunner.sh -cronjob " + cronjobentry.get("scriptname");
                cronjoblist.add(cronjobline);
            }
            return cronjoblist;
        } finally {
            cursor.close();
        }
    }

    List<String> getcronjobscriptnames(String mongourl) {

        List<String> cronjoblist = new ArrayList<String>();
        String cronjobscriptname = "";

        BasicDBObject query;
        query = new BasicDBObject();
        MongoCursor<Document> cursor = runQuery(mongourl, query);
        try {
            while (cursor.hasNext()) {
                //     System.out.println(cursor.next().toJson());
                Document cronjobentry = cursor.next();
                cronjobscriptname = cronjobentry.get("scriptname").toString();
                cronjoblist.add(cronjobscriptname);
            }
            return cronjoblist;
        } finally {
            cursor.close();
        }
    }

    String getcronjobscript(String mongourl, String scriptname) {

        String cronjobscript = "";

        BasicDBObject query;
        query = new BasicDBObject();
        query.put("scriptname",scriptname);
        MongoCursor<Document> cursor = runQuery(mongourl, query);
        try {
            while (cursor.hasNext()) {
                //     System.out.println(cursor.next().toJson());
                Document cronjobentry = cursor.next();
                cronjobscript = cronjobentry.get("script").toString();
            }
            return cronjobscript;
        } finally {
            cursor.close();
        }
    }
}
