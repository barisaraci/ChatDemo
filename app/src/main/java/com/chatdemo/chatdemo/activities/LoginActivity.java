package com.chatdemo.chatdemo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chatdemo.chatdemo.R;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ViewGroup layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        String name = preferences.getString("name", "");

        if (!name.equals("")) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        context = this;
        initLayout();
    }

    private void initLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutMain = findViewById(R.id.layout_main);

        final EditText editName = findViewById(R.id.edit_name);
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                if (name.length() > 2) {
                    editor.putString("name", name);
                    editor.apply();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(layoutMain, "enter a name more than 2 characters long", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });
    }

}
