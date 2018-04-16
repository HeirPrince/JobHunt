package com.example.prince.jobhunt.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.activities.MoreNew;

/**
 * Created by Prince on 3/26/2018.
 */

public class JobHeaderViewHolder extends RecyclerView.ViewHolder {

	public TextView headerTitle;
	public Button more;
	private Context ctx;

	public JobHeaderViewHolder(View itemView, final Context ctx) {
		super(itemView);
		this.ctx = ctx;
		headerTitle = itemView.findViewById(R.id.header_title);
		more = itemView.findViewById(R.id.more);

		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ctx.startActivity(new Intent(ctx, MoreNew.class));
			}
		});
	}
}
