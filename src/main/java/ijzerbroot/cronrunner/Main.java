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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bson.Document;

/**
 *
 * @author fhoeben
 */
public class Main {


    
    public void initcron() {

        String mongourl = "mongodb://cronmongo:27017";
        String crontabtekst = "";

        Mongodbo mongocon = new Mongodbo();
        mongocon.getcronjobs(mongourl);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // commons-cli options object
        Options options = new Options();

        // add initcron option
        options.addOption("initcron", false, "Build crontab and scripts from Mongo data");
        // add cronjob option
        options.addOption("cronjob", true, "Execute job");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("initcron")) {
                System.out.println("Got initcron");
                Cronfile cfile = new Cronfile();
                try {
                    cfile.initcronfile();
                    // init crontab
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (cmd.hasOption("cronjob")) {
                System.out.println("Got cronjob");
                // exec cronjob
            } else {
                System.out.println("Got nothing");
            }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

