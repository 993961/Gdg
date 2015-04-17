package com.sked.gdg.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sked.gdg.R;
import com.sked.gdg.activity.SettingsActivity;
import com.sked.gdg.fragment.AboutFragment;
import com.sked.gdg.fragment.ContactFragment;
import com.sked.gdg.fragment.HelpFragment;
import com.sked.gdg.fragment.HomeFragment;
import com.sked.gdg.service.SyncService;

import java.io.InputStream;

import it.neokree.googlenavigationdrawer.GAccount;
import it.neokree.googlenavigationdrawer.GAccountListener;
import it.neokree.googlenavigationdrawer.GSection;
import it.neokree.googlenavigationdrawer.GoogleNavigationDrawer;


public class GDGHome extends GoogleNavigationDrawer implements GAccountListener {
    private static final String TAG = "GDGHome";
    GSection section1, section2, recorder, night, last, settingsSection;
    private GAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate(R.menu.gdghome, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void init(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null)
            account = new GAccount(bundle.getString("name"), bundle.getString("email"), new ColorDrawable(Color.parseColor("#9e9e9e")), this.getResources().getDrawable(R.drawable.bamboo));
        this.addAccount(account);
        this.setAccountListener(this);

        // create sections
        section1 = this.newSection("GDG Bhaubaneswar",
                this.getResources().getDrawable(R.drawable.ic_launcher), HomeFragment.newInstance(0))
                /*.setSectionColor((Color.parseColor("#2196f3")))*/;
        section2 = this.newSection("Women Tech Makers",
                this.getResources().getDrawable(R.drawable.women_tech_makers), HomeFragment.newInstance(1))
                /*.setSectionColor((Color.parseColor("#2196f3")))*/;
        // recorder section with icon and 10 notifications this.getResources().getDrawable(R.drawable.ic_mic_white_24dp),.setNotifications(10);
        recorder = this.newSection("Contact Us",
                new ContactFragment())
                /*.setSectionColor((Color.parseColor("#2196f3")))*/;
        // night section with icon, section color and notifications this.getResources().getDrawable(R.drawable.ic_hotel_grey600_24dp),.setNotifications(150)
        night = this.newSection("About Us", new AboutFragment())
                /*.setSectionColor(Color.parseColor("#2196f3"))*/;
        // night section with section color
        last = this.newSection("Help", new HelpFragment())
                /*.setSectionColor((Color.parseColor("#2196f3")))*/;

        Intent i = new Intent(this, SettingsActivity.class);
        settingsSection = this.newSection("Settings",
                this.getResources().getDrawable(R.drawable.ic_settings_black_24dp), i)
                /*.setSectionColor((Color.parseColor("#2196f3")))*/;

        // add your sections to the drawer
        this.addSection(section1);
        this.addSection(section2);
        this.addDivisor();
        this.addSection(recorder);
        this.addSection(night);
        this.addDivisor();
        this.addSection(last);
        this.addBottomSection(settingsSection);

        // start thread
        //   t.start();
        assert bundle != null;
        new LoadProfileImage().execute(bundle.getString("image"));
        if (!bundle.getString("background").equals("")) {
            new LoadBackgroundImage().execute(bundle.getString("background"));
        }

    }


    @Override
    public void onAccountOpening(GAccount account) {
        // open account activity or do what you want
    }

    // after 5 second (async task loading photo from website) change user photo
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        // Toast.makeText(getApplicationContext(), "Loaded 'from web' user image", Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.w("PHOTO","user account photo setted");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    /**
     * Fetching user's information name, email, profile pic
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            account.setPhoto(result);
            notifyAccountDataChanged();
            // account.setPhoto(getResources().getDrawable(R.drawable.photo));
            //   GDGHome.this.addAccount(account);
        }
    }

    private class LoadBackgroundImage extends AsyncTask<String, Void, Bitmap> {


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            account.setBackground(result);
            notifyAccountDataChanged();
            // account.setPhoto(getResources().getDrawable(R.drawable.photo));
            //   GDGHome.this.addAccount(account);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
}
