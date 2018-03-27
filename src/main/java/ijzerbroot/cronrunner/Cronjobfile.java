package ijzerbroot.cronrunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Cronjobfile {


    public void writefile(String fname, String tekst)
            throws IOException {

        // set file executable
        File scriptfile = new File(fname);
        final boolean b = scriptfile.setExecutable(true);

        BufferedWriter writer = new BufferedWriter(new FileWriter(fname));
        writer.write(tekst);
        writer.close();

    }
}
