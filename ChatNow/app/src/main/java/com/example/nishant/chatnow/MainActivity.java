package com.example.nishant.chatnow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int Take_Photo_Request = 0;
    public static final int Take_Video_Request = 1;
    public static final int Pick_Photo_Request = 2;
    public static final int Pick_Video_Request = 3;

    public static final int Media_Type_Image = 4;
    public static final int Media_Type_Video = 5;
    public static final int File_Size_Limit = 1024 * 1024 * 10;

    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {

            switch(which) {
                case 0: // Take picture
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(Media_Type_Image);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, Take_Photo_Request);
                    }
                    break;

                case 1: // Take video
                    Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(Media_Type_Video);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
                        startActivityForResult(videoIntent, Take_Video_Request);
                    }
                    break;

                case 2: // Choose picture
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, Pick_Photo_Request);
                    break;

                case 3: // Choose video
                    Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    Toast.makeText(MainActivity.this, R.string.file_size, Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseVideoIntent, Pick_Video_Request);
                    break;
            }
        }};

    private Uri getOutputMediaFileUri(int media_type_image) {
        if (isExternalStorageAvailable()) {
            //1
            String appName=MainActivity.this.getString(R.string.app_name);
            File mediaStorageDir= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);
            //2
            if(! mediaStorageDir.exists())
            {
               if(mediaStorageDir.mkdirs()){
                   Log.e(TAG,"Failed to Create Directory");
                   return null;
               }
            }
            //3
            //4
            File mediaFile;
            Date now=new Date();
            String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path =mediaStorageDir.getPath() + File.separator;
            if(media_type_image == Media_Type_Image){
                mediaFile=new File(path + "IMG_" + timeStamp + ".jpg");
            }
            else
            {
                if(media_type_image == Media_Type_Video){
                    mediaFile=new File(path + "VID_" + timeStamp + ".mp4");
                }
                else {
                    return null;
                }
            }
            Log.e(TAG,"File: "+ Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);
        }
        else {

            return null;
        }
    }
    private boolean isExternalStorageAvailable()
    {
        String state= Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        else
        {

            return false;
        }
    }


    SectionsPagerAdapter mSectionsPagerAdapter;


    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser= ParseUser.getCurrentUser();

        if(currentUser == null)                                             //to check if anybody is logged in
        {
            NavigateToLogin();
        }

        else
        {
            Log.i(TAG, currentUser.getUsername());
        }


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == Pick_Photo_Request || requestCode == Pick_Video_Request)
            {
                if (data == null) {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                }
                else {
                    mMediaUri = data.getData();
                }

                //Log.i(TAG, "Media URI: " + mMediaUri);
                if (requestCode == Pick_Video_Request) {
                    // make sure the file is less than 10 MB
                    int fileSize = 0;
                    InputStream inputStream = null;

                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);

                        fileSize = inputStream.available();
                    }
                    catch (FileNotFoundException e) {
                        Toast.makeText(this, R.string.file_problem, Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e) {
                        Toast.makeText(this, R.string.file_problem, Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally {
                        try {
                            assert inputStream != null;
                            inputStream.close();
                        } catch (IOException e) { /* Intentionally blank */ }
                    }

                    if (fileSize >= File_Size_Limit) {
                        Toast.makeText(this, R.string.file_size, Toast.LENGTH_LONG).show();
                    }
                }
            }
            else
            {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }
            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);

            String fileType;
            if (requestCode == Pick_Photo_Request || requestCode == Take_Photo_Request) {
                fileType = ParseConstants.TYPE_IMAGE;
            }
            else {
                fileType = ParseConstants.TYPE_VIDEO;
            }

            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    private void NavigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        switch(itemId)
        {
            case   R.id.action_logout:
                    ParseUser.logOut();
                    NavigateToLogin();
                    break;

            case    R.id.action_edit_friends:
                    Intent intent=new Intent(this,EditFriendsActivity.class);
                    startActivity(intent);
                    break;

            case    R.id.action_camera:
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setItems(R.array.camera_choices,mDialogListener);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {

    }


}
