/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ijzerbroot.cronrunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author fhoeben
 */
public class Cronfile {

           String fileName = "/root/cron/crontab.txt";
           
    public void initcronfile()
            throws IOException {
        String str = "# crontab\nPATH=/root/cron:/bin:/usr/bin:/sbin:/usr/sbin:/usr/local/bin\nAWS_SECRET_ACCESS_KEY=\"$AWS_SECRET_ACCESS_KEY\"\nAWS_ACCESS_KEY_ID=\"$AWS_ACCESS_KEY_ID\"\n";
         BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(str);

        writer.close();
    }
    
    public void appendcronfile(String cronjob) 
  throws IOException {
    String str = cronjob;
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
    writer.append(str);
     
    writer.close();
}
}