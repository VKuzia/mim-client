package com.mimteam.mimclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.models.MessageModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private final List<MessageModel> messages;
    private final Context context;

    public MessageAdapter(@NonNull Context context, @NonNull List<MessageModel> messages) {
        super();
        this.messages = messages;
        this.context = context;
    }

    public void add(MessageModel message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Log.i("Name", messages.get(position).getUserName());
        if (messages.get(position).getUserName().equals("")) {
//            Log.i("Position", String.valueOf(position));
            convertView = inflater.inflate(R.layout.current_user_message_markup, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.user_message_markup, parent, false);
        }
        setupMessageModel(convertView, messages.get(position));
        return convertView;
    }

    private void setupMessageModel(@NotNull View convertView, @NotNull MessageModel messageModel) {
        TextView userName = convertView.findViewById(R.id.userName);
        TextView message = convertView.findViewById(R.id.message);
        TextView time = convertView.findViewById(R.id.time);

        userName.setText(messageModel.getUserName());
        message.setText(messageModel.getMessage());
        time.setText(messageModel.getTimeString());
    }
}
