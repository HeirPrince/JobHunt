package com.example.prince.jobhunt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.model.ImageItem;

import java.util.List;

/**
 * Created by Prince on 3/7/2018.
 */

public class UploadImagesAdapter extends RecyclerView.Adapter<UploadImagesAdapter.UploadHolder> {

	public Context ctx;
	public List<ImageItem> Images;
	public List<ImageItem> Done;

	public UploadImagesAdapter(Context ctx, List<ImageItem> images, List<ImageItem> done) {
		this.ctx = ctx;
		Images = images;
		Done = done;
	}

	@Override
	public UploadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
		return new UploadHolder(v);
	}

	@Override
	public void onBindViewHolder(UploadHolder holder, int position) {
		ImageItem item = Images.get(position);
		holder.setData(item);
	}

	@Override
	public int getItemCount() {
		return Images.size();
	}

	public class UploadHolder extends RecyclerView.ViewHolder {
		public ImageView image;
		public TextView name;
		public ProgressBar progressBar;


		public UploadHolder(View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.image);
			name = itemView.findViewById(R.id.title);
			progressBar = itemView.findViewById(R.id.progress);
		}

		public void setData(ImageItem item){
			name.setText(item.getName());
			Glide.with(ctx).load(item.getFileName()).into(image);
		}

		public void startProgress(Boolean state){
			if (state){
				progressBar.setVisibility(View.VISIBLE);
			}else {
				progressBar.setVisibility(View.GONE);
			}
		}

	}

}
