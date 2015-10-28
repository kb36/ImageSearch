package com.github.kb36.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText searchTerm = (EditText) findViewById(R.id.editText);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchTerm.getText().toString();
                if(text == null || text.length() == 0) {
                    Toast.makeText(MainActivity.this, "Invalid search query", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ImageGridResultsActivity.class);
                    intent.putExtra("QUERY", text);
                    startActivity(intent);
                }

            }
        });
    }
}
