package com.example.mytvs.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytvs.R;
import com.example.mytvs.utils.Utility;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.logo_text) TextView logoView;
    @BindView(R.id.input_username) EditText userText;
    @BindView(R.id.input_password) EditText passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/logo.ttf");
        logoView.setTypeface(custom_font);

    }

    /*
    Method triggered on Login button click
     */
    public void login(View view){

        if(Utility.isInternetAvailable()) {
            if(userText.getText().toString().isEmpty()){
                userText.setError("UserName is empty...");
            }
            if(passText.getText().toString().isEmpty()){
                passText.setError("Password is empty...");
            }

            if (userText.getText().toString().equals("test") && passText.getText().toString().equals("123456")){
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                intent.putExtra(Utility.INTENT_USERNAME, userText.getText().toString());
                intent.putExtra(Utility.INTENT_PASSWORD, passText.getText().toString());
                startActivity(intent);
            }else {
                userText.setError("Wrong UserName or Password");
                passText.setError("Wrong UserName or Password");
                Toast.makeText(this, Utility.TOAST_MSG + "Use this", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "No Internet Network", Toast.LENGTH_LONG).show();
        }
    }
}
