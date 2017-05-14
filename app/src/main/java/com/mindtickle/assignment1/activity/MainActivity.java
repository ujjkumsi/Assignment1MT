package com.mindtickle.assignment1.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtickle.assignment1.Assignment1Application;
import com.mindtickle.assignment1.R;
import com.mindtickle.assignment1.adapter.FeedAdapter;
import com.mindtickle.assignment1.models.Photo;
import com.mindtickle.assignment1.utils.Assignment1Utils;
import com.mindtickle.assignment1.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;

    @BindView(R.id.btnGetPhoto)
    Button btnGetPhoto;

    private ObjectMapper mMapper;
    public List<Photo> mPhotos;
    public List<Bitmap> mSlideShowPhotos;
    private FeedAdapter mAdapter;

    //for slide show
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    private ProgressDialog mProgress;

    private int loadingItem, loadedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMapper = Assignment1Application.getMapper();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assignment1Application.getRequestQueue().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSlideShow();
        Assignment1Application.getRequestQueue().stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_slideshow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_start:
                requestFlickrImages();
                break;
            // action with ID action_settings was selected
            case R.id.action_pause:
                stopSlideShow();
                Toast.makeText(this, "Slide show stopped", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

    @OnClick(R.id.btnGetPhoto)
    public void onClickGetPhoto(View view){
        requestFlickrImages();
    }

    /**
     * Initiate volley request for images using flickr api
     * Initializing variables
     */
    private void requestFlickrImages(){

        stopSlideShow();

        btnGetPhoto.setVisibility(View.GONE);
        mPhotos = new ArrayList<Photo>();
        mSlideShowPhotos = new ArrayList<Bitmap>();

        setupFeedAdapter();

        String url = Constants.REST_URL;
        try {
            getFlickPhoto(url);
        } catch (JSONException e) {
            Toast.makeText(this,"Error in sending request",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * setup feed recycler view adapter
     */
    private void setupFeedAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        mAdapter = new FeedAdapter(this, this);
        rvFeed.setAdapter(mAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

    }

    /**
     * parse response from flickapi
     * @param response
     * @throws Exception
     */

    private void processResponse(String response) throws Exception{
        response = response.substring(14,response.length()-1);
        JSONObject resp = new JSONObject(response);
        JSONArray array = resp.getJSONObject("photos").getJSONArray("photo");

        for(int i = 0; i < array.length(); i++){
            try {
                mPhotos.add(mMapper.readValue(array.getJSONObject(i).toString(), Photo.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mPhotos.size() > 0)
            startSlideShow();
        else
            Toast.makeText(this, "No Image Found", Toast.LENGTH_LONG).show();
    }


    /**
     * Timer tasks for slide show with 10sec delay between slides
     */
    public void startSlideShow() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeSlideShowTask();
        //schedule the timer, the TimerTask will run every 10 sec
        timer.schedule(timerTask, 100, 10000);
    }

    public void stopSlideShow() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeSlideShowTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        /**
                         * calculating screen height and width : refer utils-Assigment1Utils.java for more detail
                         * removing 32dp for horizontal and vertical margin: refer activity_main.xml relative layout
                         * logic for slide show
                         *              1. Images filling height of screen shown per slide
                         *              2. 10sec interval between the two slides
                         *              3. loadingItem and loadedItem to keep track of how many of loadingItem are remaining
                         *                  i.e. loadedItem = loadingItem when all photo are downloaded
                         */

                        if (loadingItem == loadedItem) {

                            Toast.makeText(MainActivity.this, new Date().toString(), Toast.LENGTH_SHORT).show();

                            if(mSlideShowPhotos.size() > 0){
                                mProgress.dismiss();

                                mAdapter.updateItems(mSlideShowPhotos.size());
                                scrollToLastPosition();
                            }

                            int screenWidth = Assignment1Utils.getScreenWidth(MainActivity.this) - Assignment1Utils.dpToPx(32);
                            int screenHeight = Assignment1Utils.getScreenHeight(MainActivity.this) - Assignment1Utils.getActionBarHeight(MainActivity.this) - Assignment1Utils.dpToPx(32);

                            int margin = Assignment1Utils.dpToPx(4) + 1;  // 1 to ensure image is within bounds
                            int totalHeight = 0;

                            loadingItem = loadedItem = 0;

                            List<String> urls = new ArrayList<String>();


                            for (int i = mSlideShowPhotos.size(); i < mPhotos.size(); i++) {
                                int w = Integer.parseInt(mPhotos.get(i).getWidth_n());
                                int h = mPhotos.get(i).getHeight_n();
                                if (screenWidth > w + margin) {
                                    if (screenHeight < h) {
                                        mPhotos.remove(i);
                                    } else {
                                        if (screenHeight - totalHeight > h + margin) {
                                            loadingItem++;
                                            urls.add(mPhotos.get(i).getUrl_n());
                                            totalHeight += h + margin;
                                        } else {
                                            break;
                                        }
                                    }
                                } else {
                                    mPhotos.remove(i);
                                }
                            }

                            if (loadingItem > 0) {
                                loadPhotoBitmap(urls);
                            }
                        }
                    }
                });
            }
        };
    }


    /**
     * volley request to get image bitmaps
     * with no retry policy
     * @param urls
     */
    private void loadPhotoBitmap(final List<String> urls){
        for(int i = 0; i < urls.size(); i++){
            Assignment1Application.getRequestQueue().add(new ImageRequest(urls.get(i), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    mSlideShowPhotos.add(bitmap);
                    loadedItem++;
                }

            }, 1080, 1920, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadedItem++;
                }
            }));
        }
    }

    public void scrollToLastPosition(){
        rvFeed.scrollToPosition(mSlideShowPhotos.size()-1);
    }


    /**
     * Volley Request to get photo details from server
     * @param url
     * @throws JSONException
     */


    private void getFlickPhoto(String url) throws JSONException{
        Log.d(Constants.VOLLEY, "GET PHOTOS");
        mProgress = ProgressDialog.show(this, "In progress...", "Getting flickr images url and info", true);
        mProgress.setCancelable(false);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    processResponse(response);
                    mProgress.setMessage("Starting Slideshow");
                }catch (Exception e){
                    Log.e("EXCEPTION", e.toString());
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this,"No response",Toast.LENGTH_LONG).show();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("EXCEPTION", error.toString());
                }
                Log.e("EXCEPTION", "Exception occured in volley");
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        };

       StringRequest request = new StringRequest(Request.Method.GET,
                url,
                listener,
                errorListener
        );
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

       Assignment1Application.getInstance().getRequestQueue().add(request);
    }

}
