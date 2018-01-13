package com.chatdemo.chatdemo.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chatdemo.chatdemo.R;
import com.chatdemo.chatdemo.chat.ChatAdapter;
import com.chatdemo.chatdemo.chat.Message;
import com.chatdemo.chatdemo.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<Message> messages = new ArrayList<>();
    private String name;

    private static final String URL_MESSAGES = "https://jsonblob.com/api/jsonBlob/61d68d54-d93e-11e7-a24a-934385df7024";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate);
        setContentView(R.layout.activity_chat);

        context = this;
        name = getIntent().getExtras().getString("name");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initLayout();
        loadMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollBarStyle(RecyclerView.SCROLLBARS_OUTSIDE_OVERLAY);

        final EditText editMessage = findViewById(R.id.edit_message);
        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                editMessage.setText("");
                if (message.length() != 0)
                    postMessage(message);
            }
        });
    }

    private void postMessage(String msg) {
        Message message = new Message(msg, name, "", new Timestamp(System.currentTimeMillis()).getTime() / 1000, true);
        messages.add(message);
        adapter.addItem(adapter.getItemCount(), message);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }

    private void loadMessages() {
        StringRequest req = new StringRequest(Request.Method.GET, URL_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonMain = new JSONObject(response);
                            JSONArray jsonMessages = jsonMain.getJSONArray("data");

                            for (int i = 0; i < jsonMessages.length(); i++) {
                                JSONObject objMsg = (JSONObject) jsonMessages.get(i);
                                String message = objMsg.getString("message");
                                String name = objMsg.getString("nickname");
                                String avatarURL = objMsg.getString("avatarUrl");
                                String timestamp = objMsg.getString("timestamp");
                                messages.add(new Message(message, name, avatarURL, Long.valueOf(timestamp), false));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter = new ChatAdapter(context, messages);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }

}
