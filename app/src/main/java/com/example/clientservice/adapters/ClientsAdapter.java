package com.example.clientservice.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientservice.OneClientActivity;
import com.example.clientservice.R;
import com.example.clientservice.models.Client;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientHolder> {
	private List<Client> clientList;
	private final Activity activity;

	public ClientsAdapter(List<Client> clientList, Activity activity) {
		this.clientList = clientList;
		this.activity = activity;
	}

	public void setClientList(List<Client> clientList) {
		this.clientList = clientList;
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
			activity.startActivity(intent);
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
