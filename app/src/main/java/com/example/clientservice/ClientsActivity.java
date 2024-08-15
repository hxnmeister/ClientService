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
import com.example.clientservice.data.JsonHelper;
import com.example.clientservice.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsActivity extends AppCompatActivity {
	private final String MIME_TYPE_JSON = "application/json";
	private ClientsAdapter clientsAdapter;
	private final ActivityResultLauncher<Intent> oneClientLauncher = registerForActivityResult(
		new ActivityResultContracts.StartActivityForResult(),
		result -> {
			Intent data = result.getData();

			if(result.getResultCode() == RESULT_OK){
				DBHelper dbHelper = new DBHelper(ClientsActivity.this);
				List<Client> clientList = dbHelper.findAll();

				clientsAdapter.setClientList(clientList);

				if(data.getBooleanExtra("isUpdated", false)){
					Integer clientId = data.getIntExtra("clientId", -1);

					if(clientId != -1) {
						int position = findClientPositionInListById(clientId, clientList);

						if(position != -1) {
							clientsAdapter.notifyItemChanged(position);
						}
					}
				}
				else {
					clientsAdapter.notifyItemInserted(clientList.size() - 1);
				}
			}

			Log.d("TAG", "result: " + result);
		});

	private ActivityResultLauncher<String> createDocumentLauncher;
	private ActivityResultLauncher<String[]> openDocumentLauncher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DBHelper helper = new DBHelper(ClientsActivity.this);
		List<Client> clientList;

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clients);

//		helper.dropTable(TABLE_NAME);
		helper.createTable();

		clientList = helper.findAll();

		Log.d("TAG", "receivedClients: " + clientList);

		clientsAdapter = new ClientsAdapter(clientList, this, oneClientLauncher);
		RecyclerView clientsRecycler = findViewById(R.id.clientsRecycler);
		clientsRecycler.setAdapter(clientsAdapter);
		clientsRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

		createDocumentLauncher = registerForActivityResult(
				new ActivityResultContracts.CreateDocument(MIME_TYPE_JSON),
				uri -> {
					if(uri != null) {
						JsonHelper.saveToFileFromDB(ClientsActivity.this, uri);
					}
				}
		);

		openDocumentLauncher = registerForActivityResult(
				new ActivityResultContracts.OpenDocument(),
				uri -> {
					if(uri != null) {
						DBHelper dbHelper = new DBHelper(ClientsActivity.this);

						JsonHelper.loadFromFileToDB(ClientsActivity.this, uri);

						clientsAdapter.setClientList(dbHelper.findAll());
						clientsAdapter.notifyDataSetChanged();
					}
				}
		);
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
		else if (itemId == R.id.clearMenuItem) {
			DBHelper dbHelper = new DBHelper(ClientsActivity.this);

			dbHelper.dropTable();
			dbHelper.createTable();

			clientsAdapter.setClientList(new ArrayList<>());
			clientsAdapter.notifyDataSetChanged();
		}
		else if (itemId == R.id.exportMenuItem) {
			createDocumentLauncher.launch("clients");
		}
		else if (itemId == R.id.importMenuItem) {
			openDocumentLauncher.launch(new String[]{ MIME_TYPE_JSON });
		}

		return super.onOptionsItemSelected(item);
	}

	private int findClientPositionInListById(int id, List<Client> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
				return i;
			}
		}

		return -1;
	}
}