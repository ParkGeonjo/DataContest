package com.example.contest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.model.Member;
import com.example.retrofit.RetrofitClient;
import com.example.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText etId, etPw;
    private Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etId = findViewById(R.id.et_register_id);
        etPw = findViewById(R.id.et_register_pw);
        btnRegister = findViewById(R.id.btn_register);

        Retrofit retrofit = RetrofitClient.getClient("http://10.0.2.2:8080");
        apiService = retrofit.create(ApiService.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString();
                String pw = etPw.getText().toString();
                registerUser(id, pw);
            }
        });
    }

    private void registerUser(String id, String pw) {
        Member member = new Member(id, pw);

        apiService.registerUser(member).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RegisterActivity", "회원가입 성공");
                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "회원가입 실패: " + response.message();
                    Log.e("RegisterActivity", errorMessage);
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMessage = "회원가입 실패: " + t.getMessage();
                Log.e("RegisterActivity", errorMessage, t);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
