package com.example.prince.jobhunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince on 4/11/2018.
 */

public class MoreImageAdapter extends RecyclerView.Adapter<MoreImageAdapter.ImageHolder> {

	private Context ctx;
	private List<String> urls = new ArrayList<>();
	private List<String> titles = new ArrayList<>();

	public MoreImageAdapter(Context ctx, List<String> urls, List<String> titles) {
		this.ctx = ctx;
		this.urls = urls;
		this.titles = titles;
	}

	@Override
	public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
		return new ImageHolder(v);
	}

	@Override
	public void onBindViewHolder(ImageHolder holder, int position) {
		String url = urls.get(position);
		String title = titles.get(position);

		if (title != null && url != null){
			holder.setData(title, url);
		}else {
			Toast.makeText(ctx, "no images found", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public int getItemCount() {
		return urls.size();
	}

	public class ImageHolder extends RecyclerView.ViewHolder {

		public TextView imageTitle;
		public ImageView imageView;

		public ImageHolder(View itemView) {
			super(itemView);
			imageTitle = itemView.findViewById(R.id.title);
			imageView = itemView.findViewById(R.id.image);
		}

		public void setData(String t, String url){
			imageTitle.setText(t);
			Glide.with(ctx).load(url).into(imageView);
		}
	}

}
