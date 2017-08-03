package com.example.nishant.chatnow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    protected TextView mSignupTextView;
    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                                                        // put indigator here
        setContentView(R.layout.activity_login);

        mSignupTextView=(TextView)findViewById(R.id.signup);
        mSignupTextView.setOnClickListener((View.OnClickListener) this);
        mUsername=(EditText)findViewById(R.id.username);
        mPassword=(EditText)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.login);


    }

    public void onClick(View v)                     //signup label
    {
        Intent intent=new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void onPress(View v)                     //login button
    {

        String username=mUsername.getText().toString();
        String password=mPassword.getText().toString();

        username=username.trim();
        password=password.trim();


        if (username.isEmpty()||password.isEmpty())            //error msg with dialog box
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage(R.string.login_error_message).setTitle(R.string.login_error_title).setPositiveButton(android.R.string.ok,null);

            AlertDialog dialog=builder.create();
            dialog.show();

        }
        else
        {
            //Loging in
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {



                    if(e==null)
                    {
                        //success
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(e.getMessage()).setTitle(R.string.login_error_title).setPositiveButton(android.R.string.ok, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
            });
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
