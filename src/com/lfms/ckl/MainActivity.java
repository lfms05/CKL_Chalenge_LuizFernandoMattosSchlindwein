package com.lfms.ckl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://www.ckl.io/challenge/";

	// JSON Node names
	public static final String TAG_TITLE = "title";
	public static final String TAG_AUTHORS = "authors";
	public static final String TAG_DATE = "date";
	public static final String TAG_WEBSITE = "website";
	public static final String TAG_CONTENT = "content";

	// List of articles
	List<Article> articleList;
	
	//The listView to show the articles
	ListView listView;
	
	//Text view to show the content (only in case of tablets)
	public TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		articleList = new ArrayList<Article>();
		
		listView = (ListView) findViewById(R.id.list);
		textView = (TextView) findViewById(R.id.contentMainView);
		
		// ListView's item click listener
		//Opens article content
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				final Article article = articleList.get(position);
		    	final String title = article.title;
		    	final String authors = article.authors;
		    	final String date = article.date.toString();
		    	final String website = article.website;
		    	final String content = article.content;
		    	final boolean isSeen = article.isSeen;
				
		    	//set seen to true
		    	article.isSeen = true;
				((CheckBox) view.findViewById(R.id.seen)).setChecked(true);
				
				//in case text view is not null then it is a tablet
				if(textView != null)
				{
					textView.setText(content);
				} else {
					// Starting article content activity
					Intent in = new Intent(MainActivity.this, ArticleContentActivity.class);
					in.putExtra(TAG_TITLE, title);
					in.putExtra(TAG_AUTHORS, authors);
					in.putExtra(TAG_DATE, date);
					in.putExtra(TAG_WEBSITE, website);
					in.putExtra(TAG_CONTENT, content);
					MainActivity.this.startActivity(in);
				}
			}
		});
		
		//  ListView's item long click listener
		//Set article as unseen
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//set seen to false
				articleList.get(position).isSeen = false;
				((CheckBox)view.findViewById(R.id.seen)).setChecked(false);
				return true;
			}
		});
		
		//Showing progress dialog
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Fetching Articles...");
		pDialog.setCancelable(false);
		pDialog.show();
		
		//getting the json using ion and gson
		Ion.with(this).load(url).asJsonArray().setCallback(new FutureCallback<JsonArray>() {
		   @Override
		    public void onCompleted(Exception e, JsonArray result) {
		       // do stuff with the result or error
			   
			   // Dismiss the progress dialog
			   if (pDialog.isShowing())
					pDialog.dismiss();
			   
			   // looping through All articles
			   for (int i = 0; i < result.size(); i++) {
				   JsonObject jsonObject = result.get(i).getAsJsonObject();
					
					String title = jsonObject.get(TAG_TITLE).getAsString();
					String authors = jsonObject.get(TAG_AUTHORS).getAsString();
					String website = jsonObject.get(TAG_WEBSITE).getAsString();
					String content = jsonObject.get(TAG_CONTENT).getAsString();
					String date = jsonObject.get(TAG_DATE).getAsString();

					Article article = new Article(title, authors, date, website, content, "");
				
					// adding article to article list
					articleList.add(article);
				}
			
				/**
				 * Updating parsed JSON data into ListView
				 * */
				ListAdapter adapter = new ListItemAdapter(MainActivity.this, articleList, textView);
				listView.setAdapter(adapter);
		    }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
			case(R.id.sortByTitle): //sort by title in an ascending order
				Collections.sort(articleList, Article.ArticleTitleComparator);
				break;
			case(R.id.sortByAuthors): //Sort by authors in an anscending order
				Collections.sort(articleList, Article.ArticleAuthorsComparator);
				break;
			case(R.id.sortByNewest): //Sort by date in an anscending order
				Collections.sort(articleList, Article.ArticleNewestComparator);
				break;
			case(R.id.sortByOldest): //Sort by date in a descending order
				Collections.sort(articleList, Article.ArticleOldestComparator);
				break;
			case(R.id.sortByWebsite): //Sort by website in an anscending order
				Collections.sort(articleList, Article.ArticleWebsiteComparator);
				break;
		}
		ListAdapter adapter = new ListItemAdapter(MainActivity.this, articleList, textView);
		listView.setAdapter(adapter);
		return super.onOptionsItemSelected(item);
	}
}
