package com.example.clientservice.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientservice.ClientsActivity;
import com.example.clientservice.OneClientActivity;
import com.example.clientservice.R;
import com.example.clientservice.data.DBHelper;
import com.example.clientservice.models.Client;

import java.util.List;

import lombok.Setter;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientHolder> {
	@Setter
    private List<Client> clientList;
	private final Activity activity;
	ActivityResultLauncher<Intent> oneClientLauncher;

	public ClientsAdapter(List<Client> clientList, Activity activity, ActivityResultLauncher<Intent> oneClientLauncher) {
		this.clientList = clientList;
		this.activity = activity;
		this.oneClientLauncher = oneClientLauncher;
	}

    @NonNull
	@Override
	public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		//View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent);
		View view = LayoutInflater.from(activity).inflate(R.layout.item_client, parent,
				false);
		return new ClientHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
		Client client = clientList.get(position);

		holder.clientSurnameText.setText(client.getSurname());
		holder.clientNameText.setText(client.getName());
		holder.clientBirthDateText.setText(String.valueOf(client.getDate()));
		holder.clientId = client.getId();
		//
		holder.itemView.setOnClickListener(v -> {
			Intent intent = new Intent(activity, OneClientActivity.class);
			intent.putExtra("client", client);

			oneClientLauncher.launch(intent);
		});

		holder.itemView.setOnLongClickListener(v -> {
			PopupMenu popupMenu = new PopupMenu(activity, holder.itemView);

			popupMenu.inflate(R.menu.one_client_popup);
			popupMenu.setOnMenuItemClickListener(item -> {
				int itemId = item.getItemId();

				if(itemId == R.id.deleteClientMenuItem) {
					DBHelper dbHelper = new DBHelper(activity);

					dbHelper.deleteById(client.getId());
					clientList.remove(position);
					notifyItemRemoved(position);
				}

				return true;
			});

			popupMenu.show();

			return true;
		});
	}

	@Override
	public int getItemCount() {
		return clientList.size();
	}

	public class ClientHolder extends RecyclerView.ViewHolder{
		TextView clientSurnameText;
		TextView clientNameText;
		TextView clientBirthDateText;

		Integer clientId;

		public ClientHolder(@NonNull View itemView) {
			super(itemView);
			clientSurnameText = itemView.findViewById(R.id.clientSurnameText);
			clientNameText = itemView.findViewById(R.id.clientNameText);
			clientBirthDateText = itemView.findViewById(R.id.clientBirthDateText);
		}
	}
}
