package edu.usc.eventme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventBoxes extends AppCompatActivity {
    private RecyclerView ry;
    private Button back;
    String[] sortBy;
    AutoCompleteTextView autoView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventboxses_layout);

        EventList results = (EventList) getIntent().getSerializableExtra("searchResult");
        ry = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        Intent toMain = new Intent(this,MainActivity.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toMain);
            }
        });

        sortBy = getResources().getStringArray(R.array.sortBy);
        autoView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item,sortBy);
        autoView.setAdapter(arrayAdapter);
        autoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent refresh = new Intent(getApplication(),EventBoxes.class);
                results.sort(sortBy[i]);
                refresh.putExtra("searchResult",results);
                startActivity(refresh);
                finish();
            }
        });

        MyAdaptor myAdaptor = new MyAdaptor(this, results);
        ry.setAdapter(myAdaptor);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ry.setLayoutManager(llm);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
