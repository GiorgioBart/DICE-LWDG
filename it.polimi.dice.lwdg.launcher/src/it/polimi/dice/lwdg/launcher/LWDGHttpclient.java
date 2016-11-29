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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


@
SuppressWarnings("deprecation")

public class LWDGHttpclient {

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
                showDialog("Conversion completed.\n Output model is:" + outputFile, SWT.ICON_INFORMATION);
            }
        } catch (ConnectException e) {
            showDialog("Connection refused.\n Check if server is running", SWT.ICON_ERROR);
        }

    }
    public static void showDialog(String message, int style) {

        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                MessageBox messageBox = new MessageBox(shell, style);
                messageBox.setMessage(message);
                messageBox.open();
            }
        });
    }

    public static void checkURL(String url) {@
        SuppressWarnings("resource")
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            client.execute(request);
            MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
            messageBox.setMessage("Server is alive");
            messageBox.open();
        } catch (IOException e) {
            MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
            messageBox.setMessage("Connection Refused");
            messageBox.open();
        }
    }

}

