package edu.usc.eventme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class UserEventList extends AppCompatActivity {
    private RecyclerView ry;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_list);
        EventList userList = (EventList) getIntent().getSerializableExtra("userList");
        ry = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MyAdaptor myAdaptor = new MyAdaptor(this, userList);
        ry.setAdapter(myAdaptor);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ry.setLayoutManager(llm);
    }
}