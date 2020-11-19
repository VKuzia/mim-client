package com.mimteam.mimclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.models.MessageModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageModel> {

    private final Context context;
    private final ArrayList<MessageModel> messages;

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<MessageModel> messages) {
        super(context, resource, messages);
        this.context = context;
        this.messages = (ArrayList<MessageModel>) messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.message_markup, parent, false);
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
