package com.lfms.ckl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	// contacts JSONArray
	JSONArray articles = null;

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
		
		// Calling async task to get json
		new GetContacts().execute();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Fetching Articles...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					// Getting JSON Array node
					articles = new JSONArray(jsonStr);

					// looping through All Contacts
					for (int i = 0; i < articles.length(); i++) {
						JSONObject jsonArticle = articles.getJSONObject(i);
						
						String title = jsonArticle.getString(TAG_TITLE);
						String authors = jsonArticle.getString(TAG_AUTHORS);
						String website = jsonArticle.getString(TAG_WEBSITE);
						String content = jsonArticle.getString(TAG_CONTENT);
						String date = jsonArticle.getString(TAG_DATE);

						Article article = new Article(title, authors, date, website, content, "");
					
						// adding article to article list
						articleList.add(article);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new ListItemAdapter(MainActivity.this, articleList, textView);
			listView.setAdapter(adapter);
		}
		
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
