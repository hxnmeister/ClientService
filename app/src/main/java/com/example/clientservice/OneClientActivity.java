package com.example.clientservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientservice.data.DBHelper;
import com.example.clientservice.databinding.ActivityOneClientBinding;
import com.example.clientservice.models.Client;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.util.regex.Pattern;

public class OneClientActivity extends AppCompatActivity {
	Client incomeClient;
	ActivityOneClientBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		binding = ActivityOneClientBinding.inflate(getLayoutInflater());

		super.onCreate(savedInstanceState);
		setContentView(binding.getRoot());

		Intent intent = getIntent();
		incomeClient = (Client) intent.getSerializableExtra("client");

		if (incomeClient != null) {
			binding.nameTextInput.setText(incomeClient.getName());
			binding.surnameTextInput.setText(incomeClient.getSurname());
			binding.phoneEditTextPhone.setText(incomeClient.getPhone());
			binding.dateEditTextDate.setText(incomeClient.getDate().toString());
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.one_client_menu, menu);

		if (incomeClient != null) {
			menu.findItem(R.id.saveClientMenuItem).setTitle("Update client");
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == R.id.saveClientMenuItem){
			try {
				final String PHONE_PATTERN = "^\\+380\\d{9}$|^0\\d{9}$";
				final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

				Client client = incomeClient != null ? incomeClient : new Client();
				Intent data = new Intent();
				DBHelper dbHelper = new DBHelper(OneClientActivity.this);

				String name	= binding.nameTextInput.getText().toString();
				String surname = binding.surnameTextInput.getText().toString();
				String phone = binding.phoneEditTextPhone.getText().toString();
				String date = binding.dateEditTextDate.getText().toString();

				if(name.isEmpty() && surname.isEmpty() && phone.isEmpty() && date.isEmpty()){
					throw new RuntimeException("All fields are required!");
				}
				else if(!isStatementValid(phone, PHONE_PATTERN)) {
					throw new RuntimeException("Wrong phone number format!");
				}
				else if(!isStatementValid(date, DATE_PATTERN)) {
					throw new RuntimeException("Wrong date format!");
				}

				client.setName(binding.nameTextInput.getText().toString());
				client.setSurname(binding.surnameTextInput.getText().toString());
				client.setPhone(binding.phoneEditTextPhone.getText().toString());
				client.setDate(Date.valueOf(binding.dateEditTextDate.getText().toString()));

				if (incomeClient != null) {
					dbHelper.update(client);

					data.putExtra("isUpdated", true).putExtra("clientId", incomeClient.getId());
				}
				else {
					dbHelper.insert(client);
				}

				setResult(RESULT_OK, data);
			}
			catch (Exception e) {
				setResult(RESULT_CANCELED);
			}

			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean isStatementValid(String input, String validationPattern) {
		final Pattern pattern = Pattern.compile(validationPattern);

		return pattern.matcher(input).matches();
	}
}