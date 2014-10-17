package tui.dao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Upload {

    public static void main(String[] args) {
        doInBackground();
    }

    protected static String doInBackground() {

        try {
            HttpClientBuilder httpBuilder = HttpClientBuilder.create();
            HttpClient httpClient = httpBuilder.build();

            String fileName = "/Users/nishants/Desktop/nishant.jpg";
            File file = new File(fileName);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, fileName);
            HttpEntity entity = builder.build();

            HttpPost httppost =
                    new HttpPost("http://ec2-54-169-91-68.ap-southeast-1.compute.amazonaws.com/tui/pictures/");
            httppost.setEntity(entity);
            HttpResponse response = httpClient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();
                response.getEntity().getContent().close();
                return responseString;
            } else {
                response.getEntity().getContent().close();
            }

        } catch (Exception e) {

        }
        return null;
    }

}
