package com.mobitant.firebook;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server extends AsyncTask<String, String, String> {

    ServerResponse delegate;

    @Override
    protected String doInBackground(String... urls) {

        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                InputStream stream;
                StringBuffer buffer = new StringBuffer();
                String line;

                URL url = new URL(urls[0] + "?" + m_strParameter);
                con = (HttpURLConnection) url.openConnection();
                con.connect();
                stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }

    public String m_strParameter = "";

    private void setParameter(HashMap<String, String> mapParam) {

        m_strParameter = "";
        if (mapParam != null) {

            Set<Map.Entry<String, String>> entries = mapParam.entrySet();
            for (Map.Entry<String, String> paramEntry : entries) {
                try {
                    String strValue = URLEncoder.encode(paramEntry.getValue(), "utf-8");
                    m_strParameter += paramEntry.getKey() + "=" + strValue + "&";
                } catch (Exception e) {

                }
            }
        }
    }

    public void onDb(String url, HashMap<String, String> parameter,
                     ServerResponse delegate) {
        this.delegate = delegate;
        setParameter(parameter);
        execute(url);
    }


}