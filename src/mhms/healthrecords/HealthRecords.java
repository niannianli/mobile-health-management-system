package mhms.healthrecords;

import mhms.actionbar.ActionBar;
import mhms.actionbar.ActionBar.DialogAction;
import mhms.actionbar.ActionBar.IntentAction;
import mhms.bottombaradapter.ImageAdapter;
import mhms.menu.MenuAction;
import mhms.healthrecords.R;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 
 * @author
 * 
 */
public class HealthRecords extends ActivityGroup {
	private int ID;
	private ActionBar actionBar;
	private GridView gvBar;
	private ImageAdapter ImgAdapter;
	public LinearLayout container;// for sub Activity
	private View dialogview;

	/** top button image **/
	int[] bar_image_array = { R.drawable.bar_btn, R.drawable.bar_btn, R.drawable.bar_btn, R.drawable.bar_btn };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthrecords);

		actionBar = (ActionBar) findViewById(R.id.actionbar);// add actionbar

		// add actionbar return to homepage
		actionBar.setHomeAction(new IntentAction(this, backToMainIntent(this), R.drawable.ic_title_home_default));
		// add actionbar help dialog

		actionBar.addAction(new DialogAction(createDialog(), R.drawable.help_button));

		gvBar = (GridView) this.findViewById(R.id.gvBar);
		gvBar.setNumColumns(bar_image_array.length);// set columns
		gvBar.setSelector(new ColorDrawable(Color.TRANSPARENT));// set
																// transparent
																// color when
																// chosen
		gvBar.setGravity(Gravity.CENTER);// in the middle
		gvBar.setVerticalSpacing(0);// space in vertical
		int width = this.getWindowManager().getDefaultDisplay().getWidth() / bar_image_array.length;
		ImgAdapter = new ImageAdapter(this, bar_image_array, width, 60, R.drawable.bar_itemselector);
		gvBar.setAdapter(ImgAdapter);// Adapter set menu adpater
		gvBar.setOnItemClickListener(new ItemClickEvent());// item Listener
		container = (LinearLayout) findViewById(R.id.Container);
		SwitchActivity(0);// open 0 page in default

	}

	class ItemClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			SwitchActivity(arg2);
		}
	}

	/**
	 * open needed Activity according to ID
	 * 
	 * @param id
	 *            GridView get middle size font
	 */
	public void SwitchActivity(int id) {
		ImgAdapter.SetFocus(id);// get highlight
		container.removeAllViews();// remove all View in container
		Intent intent = null;
		if (id == 0) {
			ID = 0;
			intent = new Intent(HealthRecords.this, HealthDailyRecord.class);
			Toast.makeText(this, "health daily record", Toast.LENGTH_SHORT).show();
		} else if (id == 1) {
			ID = 1;
			intent = new Intent(HealthRecords.this, HealthReport.class);
			Toast.makeText(this, "health report", Toast.LENGTH_SHORT).show();
		} else if (id == 2) {
			ID = 2;
			intent = new Intent(HealthRecords.this, MedicineRecord.class);
			Toast.makeText(this, "medicine record", Toast.LENGTH_SHORT).show();
		} else if (id == 3) {
			ID = 3;
			intent = new Intent(HealthRecords.this, HealthSurvey.class);
			Toast.makeText(this, "survey", Toast.LENGTH_SHORT).show();
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Activity to View
		Window subActivity = getLocalActivityManager().startActivity("subActivity", intent);
		// add View to container
		container.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	// back to home page
	public static Intent backToMainIntent(Context context) {
		Intent intent = new Intent(context, HealthRecords.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	// ActionBar: get help dialog
	private Builder createDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(HealthRecords.this);
		LayoutInflater inflater = getLayoutInflater();
		dialogview = inflater.inflate(R.layout.healthrecords_help_dialog, null);
		builder.setTitle("help");
		builder.setView(dialogview);
		builder.setPositiveButton("submit", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((ViewGroup) dialogview.getParent()).removeView(dialogview);
			}
		});

		builder.setNegativeButton("cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((ViewGroup) dialogview.getParent()).removeView(dialogview);
			}
		});
		return builder;
	};
	// back button; 0:quit?; others: id!=0, back to default page

	public void onBackPressed() {
		Toast.makeText(this, "return", Toast.LENGTH_SHORT).show();
		if (ID != 0) {
			SwitchActivity(0);
		} else {

			AlertDialog.Builder returnbuilder = new Builder(HealthRecords.this);
			returnbuilder.setMessage("quit?");
			returnbuilder.setTitle("hint");
			returnbuilder.setPositiveButton("submit", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					HealthRecords.this.finish();
				}
			});
			returnbuilder.setNegativeButton("cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			returnbuilder.create().show();
		}

	};

	/**
	 * create MENU
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * prevent MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		AlertDialog menudialog;
		View menuview;
		MenuAction addmenu = new MenuAction(getCurrentActivity());
		addmenu.menucreate();
		menudialog = addmenu.menuDialog;
		menuview = addmenu.menuView;
		if (menudialog == null) {
			menudialog = new AlertDialog.Builder(this).setView(menuview).show();
		} else {
			menudialog.show();
		}

		return false;
	}

}
