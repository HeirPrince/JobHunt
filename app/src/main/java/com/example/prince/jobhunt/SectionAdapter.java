package com.example.prince.jobhunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Prince on 4/19/2018.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder>{

	private Context ctx;

	public SectionAdapter(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_section_item, parent, false);
		return new SectionViewHolder(v);
	}

	@Override
	public void onBindViewHolder(SectionViewHolder holder, final int position) {


	}

	public void checkSectionTag(String tag){
		if (tag.equals("New"))
			Toast.makeText(ctx, "new clicked", Toast.LENGTH_SHORT).show();
		else if (tag.equals("Older"))
			Toast.makeText(ctx, "older clicked", Toast.LENGTH_SHORT).show();
		else if (tag.equals("Yesterday"))
			Toast.makeText(ctx, "popular clicked", Toast.LENGTH_SHORT).show();
		else if (tag.equals("Category"))
			Toast.makeText(ctx, "category clicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public class SectionViewHolder extends RecyclerView.ViewHolder {

		public TextView sectionName;

		public SectionViewHolder(View itemView) {
			super(itemView);
			sectionName = itemView.findViewById(R.id.section_title);
		}
	}
}
