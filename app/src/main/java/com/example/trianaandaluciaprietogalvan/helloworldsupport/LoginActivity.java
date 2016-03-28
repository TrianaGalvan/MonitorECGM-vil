package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void onClickEntrar(View view) {
        Intent loginIntent = new Intent(this,loginFinal.class);
        startActivity(loginIntent);
    }

    public void onClickRegistrarse(View view) {
        Intent intentRegistrarse = new Intent(this,Registrarse.class);
        startActivity(intentRegistrarse);
    }
}
