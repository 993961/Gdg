package com.sked.gdg.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sked.gdg.database.DBOHelper;
import com.sked.gdg.database.Table;
import com.sked.gdg.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SyncService extends IntentService {
    private static final String ACTION_SYNC = "com.sked.gdg.service.action.SYNC";
    private static final String GDG_GOOGLE_PLUS_URL = "https://www.googleapis.com/plus/v1/people/%2BGdgbbsrofficial/activities/";
    private static final String WTM_GOOGLE_PLUS_URL = "https://www.googleapis.com/plus/v1/people/%2BGdgbbsrofficial/activities/";

    private static final String API_KEY = "AIzaSyA5u50HgL4y_cHsSBA8vMLXVovkfwZoYtc";
    private static final int GDG = 0;
    private static final int WTM = 1;
    private static final String WTM_FACEBOOK_URL = "https://graph.facebook.com/wtmbbsr";
    private static final String GDG_FACEBOOK_URL = "https://graph.facebook.com/GDGBhubaneswar";

    public static void startSync(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SYNC);
        context.startService(intent);
    }

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC.equals(action)) {
                handleSync();
            }
        }
    }

    private void handleSync() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            loadGooglePlusPosts();
            //  loadFaceBookPosts();
        } else {
            Toast.makeText(getApplication(), "No Network Connectivity!", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFaceBookPosts() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {

                Map<String, String> param = new HashMap<String, String>();
                param.put("fields", "feed.limit(250)");
                param.put("access_token", "CAAF7JJTHABABAHm9SKB14u33aBT1j6INv9mw8JYcLK8QsbZA9YRjZC0ig3cGdbrshHfPvHUuU1MFWaqsqqbqwhKZBn0anmrgKgKBhau23sFN0hb9GZAxKrwO2vmuqnc8u5e8hupAPtuxY1ar4IFvG1tp5z26MMMPaeSsg09vrB5QMhRIMMiZC8wFlrHwizrajTOuj3ZCTXxeASy2YQXLt6");
                WebService w = new WebService();

                for (String url : params) {
                    String response = w.webGet(url,
                            "", param);
                    if (response != null) {
                        Log.d("response", response);

                        if (response.contains("feed")) {
                            try {
                                JSONObject feeds = new JSONObject(response).getJSONObject("feed");
                                JSONArray items = feeds.getJSONArray("data");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject anItem = items.getJSONObject(i);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(Table.post.POST_ID, anItem.getString("id"));
                                    if (anItem.has("story")) {
                                        contentValues.put(Table.post.TITLE, anItem.getString("story"));
                                    } else if (anItem.has("message")) {
                                        contentValues.put(Table.post.TITLE, anItem.getString("message"));
                                    }
                                    if (anItem.has("link")) {
                                        contentValues.put(Table.post.URL, anItem.getString("link"));
                                    }
                                    contentValues.put(Table.post.E_TAG, anItem.getString("status_type"));
                                    if (anItem.getString("type").equalsIgnoreCase("event")) {
                                        contentValues.put(Table.post.PROVIDER, "Events");
                                        contentValues.put(Table.post.URL, "https://www.facebook.com" + anItem.getString("link"));
                                    }
                                    contentValues.put(Table.post.PROVIDER_TYPE, 1);
                                    if (url.equalsIgnoreCase(params[0])) {
                                        contentValues.put(Table.post.POST_TYPE, GDG);
                                    } else if (url.equalsIgnoreCase(params[1])) {
                                        contentValues.put(Table.post.POST_TYPE, WTM);
                                    }
                                    contentValues.put(Table.CREATED, anItem.getString("created_time"));
                                    contentValues.put(Table.UPDATED, anItem.getString("updated_time"));
                                    DBOHelper.insert(Table.post.TABLE_NAME, contentValues);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Toast.makeText(getApplication(), "Sync Finished", Toast.LENGTH_LONG).show();
                Intent i = new Intent("SOME_ACTION");
                sendBroadcast(i);
            }
        }.execute(GDG_FACEBOOK_URL, WTM_FACEBOOK_URL);
    }

    private void loadGooglePlusPosts() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {

                Map<String, String> param = new HashMap<String, String>();
                param.put("key", API_KEY);
                WebService w = new WebService();

                for (String url : params) {
                    String response = w.webGet(url,
                            "public", param);
                    Log.d("response", response);
                    if (response.contains("items")) {
                        try {
                            JSONArray items = new JSONObject(response).getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject anItem = items.getJSONObject(i);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(Table.post.POST_ID, anItem.getString("id"));
                                contentValues.put(Table.post.TITLE, anItem.getString("title"));
                                contentValues.put(Table.post.URL, anItem.getString("url"));
                                contentValues.put(Table.post.E_TAG, anItem.getString("etag"));
                                JSONObject provider = anItem.getJSONObject("provider");
                                contentValues.put(Table.post.PROVIDER, provider.getString("title"));
                                contentValues.put(Table.post.PROVIDER_TYPE, 0);
                                if (url.equalsIgnoreCase(params[0])) {
                                    contentValues.put(Table.post.POST_TYPE, GDG);
                                } else if (url.equalsIgnoreCase(params[1])) {
                                    contentValues.put(Table.post.POST_TYPE, WTM);
                                }
                                contentValues.put(Table.CREATED, anItem.getString("published"));
                                contentValues.put(Table.UPDATED, anItem.getString("updated"));
                                DBOHelper.insert(Table.post.TABLE_NAME, contentValues);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadFaceBookPosts();
            }
        }.execute(GDG_GOOGLE_PLUS_URL);
    }

}
