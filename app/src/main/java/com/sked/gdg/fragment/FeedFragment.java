package com.sked.gdg.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sked.gdg.adapter.CustomAsyncTaskListAdapter;
import com.sked.gdg.R;
import com.sked.gdg.database.DatabaseHelper;
import com.sked.gdg.database.Table;
import com.sked.gdg.service.SyncService;
import com.sked.gdg.service.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sanjeet on 31-Jan-15.
 */
public class FeedFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView listView;
    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeLayout;
    private CustomAsyncTaskListAdapter adapter;
    private BroadcastReceiver receiver;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gdghome, container, false);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setEnabled(false);
        swipeLayout.setColorSchemeResources(R.color.blue_bright,
                R.color.green_light,
                R.color.orange_light,
                R.color.red_light);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeLayout.setEnabled(firstVisibleItem == 0);
            }
        });
        loadPosts();
        adapter = new CustomAsyncTaskListAdapter(getActivity(),
                R.layout.post_item,
                R.id.ivRowImage,
                R.id.tvFirstName,
                R.id.tvSecondLabel,
                R.id.tvThirdLabel,
                arrayList);
        listView.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*((GDGHome) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));*/
    }

    private void loadPosts() {
        arrayList = new ArrayList<>();
        SQLiteDatabase database = DatabaseHelper.instance().getReadableDatabase();
        try {
            String query;
            int post_type = getArguments().getInt("key_post_type");
            if (getArguments().get("key_feeds") == 0) {
                query = "select * from " + Table.post.TABLE_NAME + " where "
                        + Table.post.POST_TYPE + " = " + post_type + " order by " + Table.CREATED + " desc ";
            } else {
                query = "select * from " + Table.post.TABLE_NAME + " where "
                        + Table.post.PROVIDER + " = " + "\'Events\' and "
                        + Table.post.POST_TYPE + " = " + post_type + " order by " + Table.CREATED + " desc ";
            }
            Cursor cursor = database.rawQuery(query, null);
            if (cursor != null) {
                HashMap<String, Object> map;
                while (cursor.moveToNext()) {
                    map = new HashMap<>();

                    map.put("secondLabel", cursor.getString(cursor.getColumnIndex(Table.post.TITLE)));
                    map.put("thirdLabel", "");
                    map.put("url", cursor.getString(cursor.getColumnIndex(Table.post.URL)));
                    map.put("firstLabel", "");
                    map.put("actorUrl", "");
                    map.put("imageUrl", "");
                    if (cursor.getString(cursor.getColumnIndex(Table.post.TITLE)) != null
                            && !cursor.getString(cursor.getColumnIndex(Table.post.TITLE)).equalsIgnoreCase("")) {
                        arrayList.add(map);
                    }

                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(arrayList.get(position).get("url").toString()));
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setEnabled(false);
        SyncService.startSync(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("SOME_ACTION");
            filter.addAction("SOME_OTHER_ACTION");

            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //do something based on the intent's action
                    Toast.makeText(getActivity(), "Sync Finished", Toast.LENGTH_LONG).show();
                    loadPosts();
                    adapter.notifyDataSetChanged();
                    swipeLayout.setEnabled(true);
                    swipeLayout.setRefreshing(false);

                }
            };
            getActivity().registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}


