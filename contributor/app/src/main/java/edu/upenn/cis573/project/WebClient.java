package edu.upenn.cis573.project;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.Map;
import java.util.Scanner;


public class WebClient {

    private String host;
    private int port;

    public WebClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public WebClient() {

    }

    /**
     * Make an HTTP request to the RESTful API at the object's host:port
     * The request will be of the form http://[host]:[port]/[resource]?
     * followed by key=value& for each of the entries in the queryParams map.
     * @return the JSON object returned by the API if successful, null if unsuccessful
     */
    public String makeRequest(String resource, Map<String, Object> queryParams) {

        String request = "http://" + host + ":" + port + resource + "?";

        for (String key : queryParams.keySet()) {
            request += key + "=" + queryParams.get(key) + "&";

        }

        //Log.v("webclient", request);

        /*
        Web traffic must be done in a background thread in Android.
        This approach is deprecated but this should still work!
         */
        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... requests) {
                try {
                    URL url = new URL(requests[0]);
                    url.openConnection();
                    Scanner in = new Scanner(url.openStream());
                    String response = "";
                    while (in.hasNext()) {
                        String line = in.nextLine();
                        response += line;
                    }

                    in.close();
                    return response;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        asyncTask.execute(request);
        try {
            String response = asyncTask.get();
            //Log.v("webclient", response);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }


}
