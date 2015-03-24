package com.lfms.ckl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListItemAdapter extends ArrayAdapter<Article> {
	private final Context context;
	private final List<Article> articleList;
	private TextView textView;
	    
    public ListItemAdapter(Context context, List<Article> articleList, TextView textView) {
    	super(context, R.layout.list_item, articleList);
    	this.context = context;
        this.articleList = articleList;
        this.textView = textView;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// getting values from selected ListItem
    	final Article article = articleList.get(position);
    	final String title = article.title;
    	final String authors = article.authors;
    	final String date = article.date.toString();
    	final String website = article.website;
    	final String content = article.content;
    	final boolean isSeen = article.isSeen;
    	
    	//set the strings of each list item
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.list_item, parent, false);
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((TextView) convertView.findViewById(R.id.authors)).setText(String.format(context.getString(R.string.authors), authors));
		((TextView) convertView.findViewById(R.id.date)).setText(String.format(context.getString(R.string.date), date));
		((TextView) convertView.findViewById(R.id.website)).setText(String.format(context.getString(R.string.website), website));
		((CheckBox) convertView.findViewById(R.id.seen)).setChecked(isSeen);
		
		// View's click listener
		//Opens article content
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//set seen to true
				article.isSeen = true;
				((CheckBox) v.findViewById(R.id.seen)).setChecked(article.isSeen);
				
				if(textView != null)
				{
					textView.setText(content);
				} else {
					// Starting article content activity
					Intent in = new Intent(context, ArticleContentActivity.class);
					in.putExtra(MainActivity.TAG_TITLE, title);
					in.putExtra(MainActivity.TAG_AUTHORS, authors);
					in.putExtra(MainActivity.TAG_DATE, date);
					in.putExtra(MainActivity.TAG_WEBSITE, website);
					in.putExtra(MainActivity.TAG_CONTENT, content);
					context.startActivity(in);
				}
			}
		});
		
		// View's long click listener
		//Set article as unseen
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//set seen to false
				article.isSeen = false;
				((CheckBox)v.findViewById(R.id.seen)).setChecked(article.isSeen);
				return true;
			}
		});
	
		return convertView;
    }
}