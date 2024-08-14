package com.example.clientservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientservice.adapters.ClientsAdapter;
import com.example.clientservice.data.DBHelper;
import com.example.clientservice.models.Client;

import java.util.List;

public class ClientsActivity extends AppCompatActivity {
	final String TABLE_NAME = "clients";
	private ClientsAdapter clientsAdapter;
	private final ActivityResultLauncher<Intent> oneClientLauncher = registerForActivityResult(
		new ActivityResultContracts.StartActivityForResult(),
		result -> {
			DBHelper dbHelper = new DBHelper(ClientsActivity.this);
			clientsAdapter.setClientList(dbHelper.findAll(TABLE_NAME));

			Log.d("TAG", "result: " + result);
		});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DBHelper helper = new DBHelper(ClientsActivity.this);
		List<Client> clientList;
/*
		List<Client> clientList = Arrays.asList(
				new Client(1, "SA", "NA", "111",
						new Date(Date.valueOf("2024-07-18").getTime())),
				new Client(2, "SB", "NB", "222",
						new Date(Date.valueOf("2024-12-24").getTime())),
				new Client(3, "SC", "NC", "333",
						new Date(Date.valueOf("2024-02-05").getTime()))
		);
*/

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clients);

//		helper.dropTable(TABLE_NAME);
		helper.createTable(TABLE_NAME);

		clientList = helper.findAll(TABLE_NAME);

		Log.d("TAG", "receivedClients: " + clientList);

		clientsAdapter = new ClientsAdapter(clientList, this);
		RecyclerView clientsRecycler = findViewById(R.id.clientsRecycler);
		clientsRecycler.setAdapter(clientsAdapter);
		clientsRecycler.setLayoutManager(
				new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.clients_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == R.id.addClientMenuItem){
			Intent intent = new Intent(ClientsActivity.this, OneClientActivity.class);

			oneClientLauncher.launch(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}