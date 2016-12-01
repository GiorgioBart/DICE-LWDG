package it.polimi.dice.lwdg.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.swt.SWT;


/**
 * This class give help in http connection for DICE-LWDG Launcher 
 * @author Giorgio Bartoccioni
 *
 */
@
SuppressWarnings("deprecation")

public class LWDGHttpclient {

	/**
	 * This method send the given input file to the given rest url and write the given output file 
	 * @param restURL URL of the RESTful service
	 * @param inputFile Name of the input file 
	 * @param outputFile Name of the output file
	 * @throws ClientProtocolException 
	 * @throws IOException
	 */
	@
    SuppressWarnings("resource")
    public static void PostXmi(String restURL, String inputFile, String outputFile) throws ClientProtocolException, IOException {


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(restURL + "/generateToscaBlueprint");

        try {
            File file = new File(inputFile);
            FileEntity entity = new FileEntity(file, "text/plain; charset=\"UTF-8\"");
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            HttpEntity outentity = response.getEntity();

            if (outentity != null) {
                InputStream is = outentity.getContent();
                String filePath = outputFile;
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                int inByte;
                while ((inByte = is.read()) != -1)
                    fos.write(inByte);
                is.close();
                fos.close();
            	LWDGgui.showDialog("Conversion completed.\n Output model is:" + outputFile, SWT.ICON_INFORMATION);                
            }
        } catch (ConnectException e) {
        	LWDGgui.showDialog("Connection refused.\n Check if server is running", SWT.ICON_ERROR);
        }

    }
	
    /**
     * This method execute a simple GET at the given URL to check if service is alive 
     * @param url
     */
    public static Boolean checkURL(String url) {@
        SuppressWarnings("resource")
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            client.execute(request);
            return true;
        } catch (IOException e) {
        	return false;
        }
    }

}

