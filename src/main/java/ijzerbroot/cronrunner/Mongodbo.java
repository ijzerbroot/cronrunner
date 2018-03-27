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

/**
 *
 * @author fhoeben
 */
public class Mongodbo {

    public MongoCursor<Document> runQuery(String mongourl, BasicDBObject query) {
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

    public void insertLog(String mongourl, Document logentry) {
        MongoClient mongoClient;
        mongoClient = new MongoClient(new MongoClientURI(mongourl));
        MongoDatabase database;
        database = mongoClient.getDatabase("crondb");
        MongoCollection<Document> collection;
        collection = database.getCollection("cronlog");
        collection.insertOne(logentry);
        mongoClient.close();
    }

    public List<String> getcronjobs(String mongourl) {

        List<String> cronjoblist = new ArrayList<String>();
        String cronjobline;

        BasicDBObject query;
        query = new BasicDBObject();
        MongoCursor<Document> cursor = runQuery(mongourl, query);
        try {
            while (cursor.hasNext()) {
           //     System.out.println(cursor.next().toJson());
                Document cronjobentry = cursor.next();
                System.out.println("getting cronline for: " + cronjobentry.get("jobname"));
                cronjobline = cronjobentry.get("schedule") + " cronrunner.sh -cronjob " + cronjobentry.get("scriptname");
                System.out.println(cronjobline);
                cronjoblist.add(cronjobline);
            }
            return cronjoblist;
        } finally {
            cursor.close();
        }
    }

    public List<String> getcronjobscriptnames(String mongourl) {

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
                System.out.println(cronjobscriptname);
                cronjoblist.add(cronjobscriptname);
            }
            return cronjoblist;
        } finally {
            cursor.close();
        }
    }

    public String getcronjobscript(String mongourl, String scriptname) {

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
                System.out.println(cronjobscript);
            }
            return cronjobscript;
        } finally {
            cursor.close();
        }
    }
}
