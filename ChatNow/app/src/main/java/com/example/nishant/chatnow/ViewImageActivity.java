package com.example.nishant.chatnow;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity implements ActionBar.TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		ImageView imageView = (ImageView)findViewById(R.id.imageView);
		
		Uri imageUri = getIntent().getData();
		Picasso.with(this).load(imageUri.toString()).into(imageView);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
//	private void setupActionBar() {

//		getActionBar().setDisplayHomeAsUpEnabled(true);

//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}
}
