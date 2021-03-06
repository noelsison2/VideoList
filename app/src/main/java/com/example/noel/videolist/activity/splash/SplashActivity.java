package com.example.noel.videolist.activity.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noel.videolist.activity.main.MainActivity;
import com.example.noel.videolist.R;

/**
 * Created by Noel on 3/13/2017.
 */

public class SplashActivity extends AppCompatActivity {

    Toast toastNameError;
    Button buttonStart;
    EditText editTextName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        toastNameError = Toast.makeText(
                getApplicationContext(),
                getString(R.string.splash_error_name_empty),
                Toast.LENGTH_SHORT
        );

        buttonStart = (Button) findViewById(R.id.splash_b_start);
        editTextName = (EditText) findViewById(R.id.splash_et_name);
        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handled = true;
                    submitName();
                }
                return handled;
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitName();
            }
        });
    }

    private void submitName() {
        String inputName = editTextName.getText().toString().trim();
        if (inputName.isEmpty()) {
            toastNameError.show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.saved_first_time), false);
        editor.putString(getString(R.string.saved_name), inputName.toString());
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideToast() {
        if (toastNameError != null) {
            toastNameError.cancel();
        }
    }
}
