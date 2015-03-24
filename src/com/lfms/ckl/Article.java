package com.lfms.ckl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import android.text.format.DateFormat;

public class Article {

	public String title;
	public String authors;
	public String date;
	public String website;
	public String content;
	public String image;
	public boolean isSeen;
	
	public Article (String title, String authors, String date, String website, String content, String image)
	{
		this.title = title;
		this.authors = authors;
		this.date = date;
		this.website = website;
		this.content = content;
		this.image = image;
		this.isSeen = false;
	}
	
	public static Comparator<Article> ArticleTitleComparator = new Comparator<Article>() 
	{
		public int compare(Article article1, Article article2) 
		{
			//sort by title in an ascending order
			return article1.title.toUpperCase().compareTo(article2.title.toUpperCase());
		}
	};
	
	public static Comparator<Article> ArticleAuthorsComparator = new Comparator<Article>() 
	{
		public int compare(Article article1, Article article2) 
		{
			//sort by title in an ascending order
			return article1.authors.toUpperCase().compareTo(article2.authors.toUpperCase());
		}
	};
	
	public static Comparator<Article> ArticleWebsiteComparator = new Comparator<Article>() 
	{
		public int compare(Article article1, Article article2) 
		{
			//sort by title in an ascending order
			return article1.website.toUpperCase().compareTo(article2.website.toUpperCase());
		}
	};
	
	public static Comparator<Article> ArticleNewestComparator = new Comparator<Article>() 
	{
		public int compare(Article article1, Article article2) 
		{
			//sort by title in an ascending order
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = new SimpleDateFormat("MM/dd/yyy").parse(article1.date);
				date2 = new SimpleDateFormat("MM/dd/yyy").parse(article2.date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return date2.compareTo(date1);
		}
	};
	
	public static Comparator<Article> ArticleOldestComparator = new Comparator<Article>() 
	{
		public int compare(Article article1, Article article2) 
		{
			//sort by title in an ascending order
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = new SimpleDateFormat("MM/dd/yyy").parse(article1.date);
				date2 = new SimpleDateFormat("MM/dd/yyy").parse(article2.date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return date1.compareTo(date2);
		}
	};
}
