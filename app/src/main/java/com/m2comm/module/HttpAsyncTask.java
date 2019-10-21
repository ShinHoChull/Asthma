package com.m2comm.module;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class HttpAsyncTask extends AsyncTask<HttpParam, Integer, String> {

    private HttpInterface httpInterface;
    private Context context;

    public HttpAsyncTask(Context context, HttpInterface httpInterface) {
        this.httpInterface = httpInterface;
        this.context = context;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(HttpParam... params) {

        try {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(params[0].val);
            List params2 = new ArrayList();

            if(params != null){
                for(int i=1;i<params.length;i++)
                {
                    params2.add(new BasicNameValuePair(params[i].key, params[i].val));
                }
            }

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params2, HTTP.UTF_8);

            post.setEntity(ent);
            HttpResponse responsePost = client.execute(post);
            HttpEntity resEntity = responsePost.getEntity();

            if (resEntity != null) {
                return EntityUtils.toString(resEntity);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled(result);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        httpInterface.onResult(result);
    }

}
