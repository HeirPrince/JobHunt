package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Prince on 2/28/2018.
 */

public class DialogHelper {

	private Context ctx;
	MaterialDialog dialog;

	public DialogHelper(Context ctx) {
		this.ctx = ctx;
		dialog = new MaterialDialog.Builder(ctx).build();
	}

	public MaterialDialog showSimpleDialog(String title, String message, String positiveText, String negativeText){
		return new MaterialDialog.Builder(ctx)
				.title(title)
				.content(message)
				.positiveText(positiveText)
				.negativeText(negativeText)
				.show();
	}

	public void dismissDialog() {
		dialog.dismiss();
	}

	public interface onClickBtn{
		void clicked(Boolean status);
	}

	public void showActionDialog(String title, String message, String positiveText, String negativeText, final onClickBtn callback){
		dialog = new MaterialDialog.Builder(ctx)
				.title(title)
				.content(message)
				.positiveText(positiveText)
				.negativeText(negativeText)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						callback.clicked(true);
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						callback.clicked(false);
					}
				})
				.show();

	}

}
