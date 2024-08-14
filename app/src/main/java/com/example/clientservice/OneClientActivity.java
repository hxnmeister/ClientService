package com.example.clientservice;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientservice.data.DBHelper;
import com.example.clientservice.models.Client;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.util.regex.Pattern;

public class OneClientActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_client);

/*
		Intent intent = getIntent();
		Client client = (Client) intent.getSerializableExtra("client");
		Log.d("TAG", "onCreate: " + client);
*/


    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.one_client_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		final String TABLE_NAME = "clients";
		int itemId = item.getItemId();

		if (itemId == R.id.saveClientMenuItem){
			try {
				final String PHONE_PATTERN = "^\\+380\\d{9}$|^0\\d{9}$";
				final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

				TextInputEditText nameTextInput = findViewById(R.id.nameTextInput);
				TextInputEditText surnameTextInput = findViewById(R.id.surnameTextInput);
				EditText phoneEditText = findViewById(R.id.phoneEditTextPhone);
				EditText dateEditText = findViewById(R.id.dateEditTextDate);

				String name = nameTextInput.getText().toString();
				String surname = surnameTextInput.getText().toString();
				String phone = phoneEditText.getText().toString();
				String date = dateEditText.getText().toString();

				if(name.length() == 0 &&
					surname.length() == 0 &&
					phone.length() == 0 &&
					date.length() == 0){
					throw new RuntimeException("All fields are required!");
				}
				else if(!isStatementValid(phone, PHONE_PATTERN)) {
					throw new RuntimeException("Wrong phone number format!");
				}
				else if(!isStatementValid(date, DATE_PATTERN)) {
					throw new RuntimeException("Wrong date format!");
				}

				DBHelper dbHelper = new DBHelper(OneClientActivity.this);

				dbHelper.insert(new Client(2, surname, name, phone, new Date(Date.valueOf(date).getTime())), TABLE_NAME);
				setResult(RESULT_OK);
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