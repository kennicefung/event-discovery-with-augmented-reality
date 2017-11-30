package com.goer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.content.Intent;
import android.widget.TextView;

import com.goer.R;
import com.goer.controller.UserController;
import com.goer.model.User;


public class LoginActivity extends AppCompatActivity {
    private EditText eml;
    private EditText pwd;
    private AppCompatButton btn_login;
    private TextView err_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eml = (EditText) findViewById(R.id.input_email);
        pwd = (EditText) findViewById(R.id.input_password);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        err_msg = (TextView) findViewById(R.id.err_msg);

        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = eml.getText().toString();
                String password = pwd.getText().toString();
                if(!email.equals("") && !password.equals("")) {
                    try {
                        User u = UserController.loginUser(email, password, getApplicationContext());
                        if (u != null) {
                            err_msg.setText("");
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);

                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(main);

                            finish();
                        } else {
                            err_msg.setText(UserController.getLoginErrMsg());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    err_msg.setText("Please input email and password to login.");
                }

            }
        });

    }

}
