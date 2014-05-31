package com.raghu.newsvolley;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/** Adapter for managing NewsModel*/
public class NewsAdapter extends BaseAdapter{

	private ArrayList<NewsModel> arrNews ;
	private LayoutInflater lf;
	private ImageLoader imageLoader;

	public NewsAdapter(LayoutInflater lf2, ArrayList<NewsModel> arrNews2,
			ImageLoader imageLoader2) {
		this.lf = lf2;
		this.arrNews = arrNews2;
		this.imageLoader = imageLoader2;
	}

	@Override
	public int getCount() {
		return arrNews.size();
	}

	@Override
	public Object getItem(int i) {
		return arrNews.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh ;
		if(view == null){
			vh = new ViewHolder();
			view = lf.inflate(R.layout.row_listview,null);
			vh.tvImage = (NetworkImageView) view.findViewById(R.id.imgImage);
			vh.tvTitle = (TextView) view.findViewById(R.id.txtTitle);
			vh.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
			vh.tvDate = (TextView) view.findViewById(R.id.txtDate);
			view.setTag(vh);
		}
		else{
			vh = (ViewHolder) view.getTag();
		}

		NewsModel nm = arrNews.get(i);

		//If Image URI is null then hide corresponding Imageview
		String url = nm.getLink();
		if(url!=null)
		{
			vh.tvImage.setVisibility(View.VISIBLE);
			vh.tvImage.setImageUrl(nm.getLink(), imageLoader);
		}
		else
		{
			vh.tvImage.setVisibility(View.GONE);
		}
		vh.tvTitle.setText(nm.getTitle());
		vh.tvDesc.setText(nm.getDescription());
		vh.tvDate.setText(nm.getPubDate());
		return view;
	}

	//To Hold views in a Listview
	class  ViewHolder{
		//This ImageView is Volley Library special ImageView
		NetworkImageView tvImage;
		
		TextView tvTitle;
		TextView tvDesc;
		TextView tvDate;
	}
}