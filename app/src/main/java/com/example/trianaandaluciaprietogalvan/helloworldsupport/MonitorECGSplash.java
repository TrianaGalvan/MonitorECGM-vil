package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.MonitorECGUtils;

public class MonitorECGSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //verificar si existen usuarios en sesion
        String usuario = MonitorECGUtils.obtenerUltimoUsuarioEnSesion(this);
        //ya existen usuarios registrados
        if(usuario == null){
            AccountManager am = AccountManager.get(this);
            am.addAccount(getString(R.string.account_type), null, null, null, this, null, null);
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        //terminar esta actividad sin que se pueda volver a abrir
        finish();
    }
}
