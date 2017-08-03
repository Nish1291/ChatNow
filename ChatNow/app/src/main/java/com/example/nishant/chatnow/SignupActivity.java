package com.example.nishant.chatnow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends ActionBarActivity
{

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                                                //put indigator here

        setContentView(R.layout.activity_signup);

        mUsername=(EditText)findViewById(R.id.username);
        mPassword=(EditText)findViewById(R.id.password);
        mEmail=(EditText)findViewById(R.id.email);
        mSignUpButton = (Button)findViewById(R.id.signUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)                                 // creating new user on parse
            {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty())            //error msg with dialog box
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage(R.string.signup_error_message).setTitle(R.string.signupp_error_title).setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    //add users
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage(e.getMessage()).setTitle(R.string.signupp_error_title).setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });

                }


            }

        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }



}
