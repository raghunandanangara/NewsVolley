package com.raghu.newsvolley;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/** Displaying News from RESTful server(with JSON response) using Volley Google Library*/
public class NewsActivity extends Activity {

    private String TAG = this.getClass().getSimpleName();
	private String url = "http://mobilatr.mob.f2.com.au/services/views/9.json";
    
    /** Elements in activity_main.xml*/
    private ListView lstView;
    private Button refreshButton;
    
    /** Volley Library Variables*/
    private RequestQueue mRequestQueue;
    ImageLoader.ImageCache imageCache;
    ImageLoader imageLoader;

    /** Array list to capture JSON data*/
    private ArrayList<NewsModel> arrNews ;
    
    /** Layout inflater instance*/
    private LayoutInflater lf;
    
    /** Adapter to populate array list in Listview*/
    private NewsAdapter va;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

    	lf = LayoutInflater.from(this);

    	/** Using Memory Cache(RAM) of Volley Library only
    	 * If images are in high number you need to implement Disk Cache also
    	 **/
    	imageCache = new BitmapLruCache();
    	mRequestQueue =  Volley.newRequestQueue(this);
    	imageLoader = new ImageLoader(mRequestQueue, imageCache);

    	/** Initializing Arraylist and Adapter*/
    	arrNews = new ArrayList<NewsModel>();
    	va = new NewsAdapter(lf, arrNews, imageLoader);

    	/** Once refresh button is pressed clear all the data in arraylist
    	 * and start JSON query again
    	 **/
    	refreshButton = (Button) findViewById(R.id.refresh);
    	refreshButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			//TODO Ideal way to do is to compare JSON request and if
    			//there is a update from server then clear and update the list
    			arrNews.clear();
    			startJsonRequest();
    		}
    	});

    	lstView = (ListView) findViewById(R.id.listView);
    	lstView.setOnItemClickListener(new OnItemClickListener() {

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position,
    				long id) {
    			//Get TinyURL and start browser intent
    			NewsModel tempView = (NewsModel) parent.getItemAtPosition(position);
    			Intent i = new Intent(Intent.ACTION_VIEW);
    			i.setData(Uri.parse(tempView.getTinyURL()));
    			startActivity(i);
    		}
    	});
    	lstView.setAdapter(va);

    	startJsonRequest();
    }

    /** Shows progress dialog until JSON response is parsed.
	 * Also sorts arraylist with PublishDate and updates arrayadapter
	 **/
    private void startJsonRequest()
    {
    	/** Progressdialog to show loading progress before populating listview*/
        final ProgressDialog pd;
        
        pd = ProgressDialog.show(this,"Please Wait...","Loading from Server...", false, true);
        
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
        	@Override
            public void onResponse(JSONObject response) {
        		
                Log.i(TAG,response.toString());
                
                parseJSON(response);
                
                /** On the fly Sorting ArrayAdapter with PublishDate*/
                Collections.sort(arrNews, new Comparator<NewsModel>()
                {
                	@Override
                	public int compare(NewsModel a, NewsModel b)
                	{
                		if(a.getPubDate()==null || b.getPubDate()==null)
                			return 0;
                		return b.getPubDate().compareToIgnoreCase(a.getPubDate());
                	}
                });

                va.notifyDataSetChanged();
                pd.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.getMessage());
            }
        });

        //Add JSON Request to Volley Request Queue
        mRequestQueue.add(jr);
    }

    /** Parsing according to JSON response Keys */
    private void parseJSON(JSONObject json){
    	//TODO Investigate if entire parsing can be done in
    	//separate thread to avoid ANR on UI thread 
    	try{
    		//Get JSON Array values with Key="items"
    		JSONArray items = json.getJSONArray("items");
    		for(int i=0;i<items.length();i++){
    			//TODO Check NULL conditions for each item
    			JSONObject item = items.getJSONObject(i);
    			NewsModel nm = new NewsModel();
    			nm.setTitle(item.optString("headLine"));
    			nm.setDescription(item.optString("slugLine"));
    			nm.setTinyURL(item.optString("tinyUrl"));
    			nm.setPubDate(item.optString("dateLine"));

    			//Usually Server will send null if there is no image URI
    			String link = item.getString("thumbnailImageHref");
    			if(!item.isNull("thumbnailImageHref"))
    				//if((!link.equals("null")) || (!link.isEmpty()) || link!=null)
    			{
    				//But unfortunately, server is sending empty string ""
    				//This is rare and special case
    				if(!link.equals(""))
    				{
    					nm.setLink(link);
    					//Log.e("Raghu", link);
    				}
    				//So Handling this special case
    				//by explicitly setting null instead of empty string
    				else
    				{
    					String temp = null;
    					nm.setLink(temp);
    				}
    			}

    			arrNews.add(nm);
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
