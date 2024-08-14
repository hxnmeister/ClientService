package com.example.clientservice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		/*ListView listView = findViewById(R.id.listView);
		String[] data = {
			"Hello", "World", "!!!"
		};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
				this, android.R.layout.simple_list_item_1, data);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener((parent, view, position, id) -> {
			//Log.d("TAG", "item: " + ((TextView)view).getText());
			//Log.d("TAG", "item: " + parent.getItemAtPosition(position).getClass());
			Log.d("TAG", "item: " + listView.getItemAtPosition(position));
		});*/
		//
	}
}