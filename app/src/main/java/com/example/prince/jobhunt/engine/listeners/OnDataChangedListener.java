package com.example.prince.jobhunt.engine.listeners;

import java.util.List;

/**
 * Created by Prince on 4/24/2018.
 */

public interface OnDataChangedListener<T> {
	void onDataChanged(List<T> list);
}
