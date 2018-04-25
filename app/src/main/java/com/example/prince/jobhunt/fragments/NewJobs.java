package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prince.jobhunt.JobAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewJobs extends Fragment implements DiscreteScrollView.OnItemChangedListener {


	public static final String TITLE = "New";
	private DiscreteScrollView itemPicker;
	private InfiniteScrollAdapter infiniteAdapter;

	private FirebaseAgent agent;
	private ProgressBar progressBar;
	private RecyclerView new_list;
	private TextView currentItemName;
	private TextView currentItemPrice;
	private ImageView rateItemButton;

	private List<Job> jobList;
	private List<String> idList;

	public NewJobs() {
		// Required empty public constructor
		agent = new FirebaseAgent(getContext());
	}

	public static NewJobs newInstance() {
		return new NewJobs();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_new_jobs, container, false);

		setUI(v);
		getJobs();

		return v;
	}

	public void setUI(final View v){

		currentItemName = v.findViewById(R.id.item_name);
		currentItemPrice = v.findViewById(R.id.item_price);

		jobList = new ArrayList<>();
		idList = new ArrayList<>();
		itemPicker = v.findViewById(R.id.item_picker);
		itemPicker.addOnItemChangedListener(this);

		agent.getJobList(new FirebaseAgent.jobList() {
			@Override
			public void All(List<Job> jobs, List<String> ids) {
				jobList.addAll(jobs);
				idList.addAll(ids);

				itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
				infiniteAdapter = InfiniteScrollAdapter.wrap(new JobAdapter(getContext(), jobList, idList));
				itemPicker.setAdapter(new JobAdapter(getContext(), jobList, idList));
				itemPicker.setItemTransitionTimeMillis(150);
				itemPicker.setSlideOnFling(true);
				itemPicker.setOverScrollEnabled(true);
				itemPicker.setElevation(4);
				itemPicker.setItemTransformer(new ScaleTransformer.Builder()
						.setMinScale(0.8f)
						.build());

				for (Job job : jobList){
					onItemChanged(job);
				}
			}
		});
	}

	public void getJobs() {

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}


	@Override
	public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
		int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
		onItemChanged(jobList.get(positionInDataSet));
	}

	private void onItemChanged(Job item) {
		currentItemName.setText(item.getTitle());
		currentItemPrice.setText(String.valueOf(item.getSalary()));;
	}
}
