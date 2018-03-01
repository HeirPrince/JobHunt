package com.example.prince.jobhunt.engine;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Prince on 2/28/2018.
 */

public class DialogHelper {

	private Context ctx;

	public DialogHelper(Context ctx) {
		this.ctx = ctx;
	}

	public MaterialDialog showSimpleDialog(String title, String message, String positiveText, String negativeText){
		return new MaterialDialog.Builder(ctx)
				.title(title)
				.content(message)
				.positiveText(positiveText)
				.negativeText(negativeText)
				.show();
	}

}
