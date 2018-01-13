package com.chatdemo.chatdemo.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chatdemo.chatdemo.R;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by baris on 13/01/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Message> messages;

    private static final int MESSAGE_TYPE_RECEIVED = 0;
    private static final int MESSAGE_TYPE_SENT = 1;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = new ArrayList<>(messages);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == MESSAGE_TYPE_RECEIVED)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        else if (viewType == MESSAGE_TYPE_SENT)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Message message = messages.get(position);

        ((ViewHolder) holder).bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if (message.isBelongedToYou())
            return MESSAGE_TYPE_SENT;
        else
            return MESSAGE_TYPE_RECEIVED;
    }

    public void addItem(int position, Message message) {
        messages.add(position, message);
        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvMessage, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.text_name);
            tvMessage = itemView.findViewById(R.id.text_message);
            tvTime = itemView.findViewById(R.id.text_time);
        }

        public void bind(Message message) {
            tvName.setText(message.getName());
            tvMessage.setText(message.getMessage());

            long date = (new Timestamp(System.currentTimeMillis()).getTime() / 1000 - message.getTimestamp()) / 60;
            String time;
            if (date > 7 * 24 * 60) { time = (date / (7 * 24 * 60) + " w ago"); }
            else if (date > 24 * 60) { time = (date / (24 * 60) + " d ago"); }
            else if (date > 60) { time = (date / 60 + " h ago"); }
            else if (date >= 0){ time = (date + " m ago"); }
            else { time = "now"; }
            tvTime.setText(time);
        }
    }

}
