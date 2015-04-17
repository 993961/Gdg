package com.sked.gdg.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.gdg.R;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomAsyncTaskListAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private ArrayList<HashMap<String, Object>> mapArrayList;
    private Context context;
    private int customRowResourceId;
    private int imageViewResourceId;
    private int textViewHeaderResourceId;
    private int textViewDetailResourceId = -1;
    private int textViewSubDetailResourceId = -1;

    public CustomAsyncTaskListAdapter(Context context, int customRowResourceId,
                                      int imageViewResourceId, int textViewHeaderResourceId,
                                      ArrayList<HashMap<String, Object>> mapList) {
        super(context, customRowResourceId, mapList);
        this.customRowResourceId = customRowResourceId;
        this.context = context;
        this.imageViewResourceId = imageViewResourceId;
        this.textViewHeaderResourceId = textViewHeaderResourceId;
        this.mapArrayList = new ArrayList<HashMap<String, Object>>();
        this.mapArrayList.addAll(mapList);
    }

    public CustomAsyncTaskListAdapter(Context context, int customRowResourceId,
                                      int imageViewResourceId, int textViewHeaderResourceId,
                                      int textViewDetailResourceId, int textViewSubDetailResourceId,
                                      ArrayList<HashMap<String, Object>> mapList) {
        super(context, customRowResourceId, mapList);
        this.customRowResourceId = customRowResourceId;
        this.context = context;
        this.imageViewResourceId = imageViewResourceId;
        this.textViewHeaderResourceId = textViewHeaderResourceId;
        this.textViewDetailResourceId = textViewDetailResourceId;
        this.textViewSubDetailResourceId = textViewSubDetailResourceId;
        this.mapArrayList = new ArrayList<HashMap<String, Object>>();
        this.mapArrayList.addAll(mapList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.d("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(customRowResourceId, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.position = position;
            holder.imageView = (ImageView) convertView
                    .findViewById(imageViewResourceId);
            holder.textView1 = (TextView) convertView
                    .findViewById(textViewHeaderResourceId);
            if (textViewDetailResourceId > 0) {
                holder.textView2 = (TextView) convertView
                        .findViewById(textViewDetailResourceId);
            }
            if (textViewSubDetailResourceId > 0) {
                holder.textView3 = (TextView) convertView
                        .findViewById(textViewSubDetailResourceId);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HashMap<String, Object> map = mapArrayList.get(position);

        try {
            holder.textView1.setText(map.get("firstLabel").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.textView2.setText(map.get("secondLabel").toString());
            holder.textView3.setText(map.get("thirdLabel").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.profile_image));
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   new AsyncTask<ViewHolder, Void, Bitmap>() {
            private ViewHolder v;

            @Override
            protected Bitmap doInBackground(ViewHolder... params) {
                Bitmap bitmap = null;
                v = params[0];
                try {
                    bitmap = Utils.compressBitmapTableRow(Utils.THUMBNAIL_PATH +
                            map.get("photo").toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);

                if (v.position == position) {

                    try {
                        if (result != null) {
                            v.imageView.setImageBitmap(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(holder);*/

        return convertView;

    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        int position;
    }

}
