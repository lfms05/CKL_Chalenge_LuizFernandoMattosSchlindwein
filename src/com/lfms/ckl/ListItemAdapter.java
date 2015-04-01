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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListItemAdapter extends ArrayAdapter<Article> {
	private final Context context;
	private final List<Article> articleList;
	private TextView textView;
	
	static class ViewHolder {
		TextView title;
		TextView authors;
		TextView website;
		TextView date;
		ImageView image;
		CheckBox seen;
	}
	    
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
    	
    	ViewHolder holder;
    	
    	//set up the view holder
    	if(convertView == null)
    	{
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView = inflater.inflate(R.layout.list_item, parent, false);
    		
    		holder = new ViewHolder();
    		holder.title = (TextView) convertView.findViewById(R.id.title);
    		holder.authors = (TextView) convertView.findViewById(R.id.authors);
    		holder.date = (TextView) convertView.findViewById(R.id.date);
    		holder.website = (TextView) convertView.findViewById(R.id.website);
    		holder.image = (ImageView) convertView.findViewById(R.id.image);
    		holder.seen = (CheckBox) convertView.findViewById(R.id.seen);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	
    	//set the strings of each list item
    	holder.title.setText(title);
		holder.authors.setText(String.format(context.getString(R.string.authors), authors));
		holder.date.setText(String.format(context.getString(R.string.date), date));
		holder.website.setText(String.format(context.getString(R.string.website), website));
		holder.seen.setChecked(isSeen);
		
		return convertView;
    }
}