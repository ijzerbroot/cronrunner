/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ijzerbroot.cronrunner;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bson.Document;

/**
 * @author fhoeben
 */
public class Main {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // commons-cli options object
        Options options = new Options();
        String mongourl = "mongodb://resources_cronmongo_1:27017";

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
                Cronjobfile cjfile = new Cronjobfile();
                Mongodbo mongocon = new Mongodbo();
                try {
                    cfile.initcronfile();
                    List<String> cronjoblist = mongocon.getcronjobs(mongourl);
                    for (String s : cronjoblist) {
                        System.out.println("cronjoblist: " + s);
                        cfile.appendcronfile(s + "\n");
                    }
                    List<String> cronjobscriptnamelist;
                    for (String s : cronjobscriptnamelist = mongocon.getcronjobscriptnames(mongourl)) {
                        cjfile.writefile(s, mongocon.getcronjobscript(mongourl, s));
                        System.out.println("writefile: " + s);
                    }
                    // init crontab
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (cmd.hasOption("cronjob")) {
                System.out.println("Got cronjob");
                Mongodbo mongocon = new Mongodbo();
                String logline = "";
                Document logentry = new Document();
                logentry.put("jobname", cmd.getOptionValue("cronjob"));
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                // exec job and catch stdin/stdout into Document
                try {
                    Process jobproc = new ProcessBuilder().command("sh", cmd.getOptionValue("cronjob")).start();
                    try {
                        int exitcode = jobproc.waitFor();
                        if (exitcode != 0) {
                            logentry.put("timestamp", sdf.format(cal.getTime()));
                            logentry.put("result", "KO");
                            logentry.put("log", output(jobproc.getErrorStream()));
                        } else {
                            logentry.put("timestamp", sdf.format(cal.getTime()));
                            logentry.put("result", "OK");
                            logentry.put("log", output(jobproc.getInputStream()));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mongocon.insertLog(mongourl, logentry);
                // exec cronjob
            } else {
                System.out.println("Got nothing");
            }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static String output(InputStream inputStream) throws IOException {
         StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line); sb.append(System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
}

