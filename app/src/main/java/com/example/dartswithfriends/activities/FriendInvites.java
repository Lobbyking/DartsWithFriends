package com.example.dartswithfriends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.R;

public class FriendInvites extends AppCompatActivity implements View.OnClickListener{

    private Button switchToMid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left);

        switchToMid = findViewById(R.id.leftBackToMid_Button);
        switchToMid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == switchToMid.getId()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
