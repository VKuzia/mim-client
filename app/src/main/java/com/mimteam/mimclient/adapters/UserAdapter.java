package com.mimteam.mimclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.AvatarView;
import com.mimteam.mimclient.models.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserModel> {

    public UserAdapter(@NonNull Context context, int resource, @NonNull List<UserModel> users) {
        super(context, resource, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_markup, parent, false);
        }
        setupUserModel(convertView, getItem(position));
        return convertView;
    }

    private void setupUserModel(@NotNull View convertView, @NotNull UserModel userModel) {
        AvatarView avatar = convertView.findViewById(R.id.userAvatar);
        TextView userName = convertView.findViewById(R.id.userNameView);

        avatar.setChatName(userModel.getName());
        userName.setText(userModel.getName());
    }
}
