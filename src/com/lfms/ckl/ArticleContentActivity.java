package com.lfms.ckl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class ArticleContentActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_content);
	
		Intent intent = getIntent();
		
		((TextView) findViewById(R.id.title)).setText(intent.getStringExtra(MainActivity.TAG_TITLE));
		((TextView) findViewById(R.id.authors)).setText(intent.getStringExtra(MainActivity.TAG_AUTHORS));
		((TextView) findViewById(R.id.date)).setText(intent.getStringExtra(MainActivity.TAG_DATE));
		((TextView) findViewById(R.id.website)).setText(intent.getStringExtra(MainActivity.TAG_WEBSITE));
		((TextView) findViewById(R.id.content)).setText(intent.getStringExtra(MainActivity.TAG_CONTENT));
		((CheckBox) findViewById(R.id.seen)).setChecked(true);
	}
	
}
