package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.CardiologoDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.MonitorECGUtils;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.NetworkUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.PacienteDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFinal extends AppCompatActivity{

    private static final String LOG_TAG = LoginFinal.class.getSimpleName();
    private AccountAuthenticatorResponse accountAuthenticatorResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_final);
        Intent intent = getIntent();
        accountAuthenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (accountAuthenticatorResponse != null) {
            accountAuthenticatorResponse.onRequestContinued();
        }

    }

    public void onClickRecuperarContrasena(View view) {
        Intent intentRecuperarContrasena = new Intent();
        intentRecuperarContrasena.setClass(this,RecuperarContrasena.class);
        startActivity(intentRecuperarContrasena);
    }

    public void onClickEntrarAplicacion(View view) {
        //obtener los datos del form
        TextView correoTxt= (TextView) findViewById(R.id.txtCorreo);
        TextView contrasenaTxt = (TextView) findViewById(R.id.txtContraseña);
        final String correo = correoTxt.getText().toString();
        final String pass = contrasenaTxt.getText().toString();

        //verificar conexion de red
        if(NetworkUtil.isOnline(this)){
            ServicioWeb.loginPaciente(correo, pass, new Callback<Paciente>() {
                @Override
                public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                    Paciente paciente = response.body();
                    ContentResolver rs = getContentResolver();
                    //si existe el paciente
                    if(paciente != null){
                        //verificar que el paciente ya exista en la bd, sino registrarlo
                        boolean exis = verificarPacienteBD(paciente,rs);
                        //si no existe;crearlo
                        if(!exis) {
                            //verificar si existe el cardiologo ya registrado
                            verificarRegistroMedico(paciente.cardiologo, rs);
                            //insertar paciente
                            PacienteDAO.insertarPaciente(paciente, rs);
                        }

                        MonitorECGUtils.guardarUltimoUsuarioEnSesion(LoginFinal.this, correo);
                        finishLogin(correo, pass);
                        Account cuentaEncontrada = AccountUtil.getAccount(getBaseContext());

                        if (cuentaEncontrada != null) {
                            String contentAuthority = getString(R.string.content_authority);
                            //hacer el cotent provider que se actualize automaticamente ante algun cambio
                            ContentResolver.setIsSyncable(cuentaEncontrada, contentAuthority, 1);
                            ContentResolver.setSyncAutomatically(cuentaEncontrada, contentAuthority, true);
                        }

                        Intent intentHistorial = new Intent(LoginFinal.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MainActivity.PARAM_CORREO,correo);
                        startActivity(intentHistorial);
                        finish();
                    }
                    //no existe el paciente
                    else {
                        Toast.makeText(getApplicationContext(), "Usuario  o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        authenticatorFinish(null);
                    }
                }

                @Override
                public void onFailure(Call<Paciente> call, Throwable t) {
                    authenticatorFinish(null);
                }
            });
        }else{
            Toast.makeText(LoginFinal.this, "Verifica tu conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificarPacienteBD(Paciente p,ContentResolver rs){
        Uri uriPaciente = MonitorECGContrato.PacienteEntry.buildPacienteId(p.idPaciente);
        Cursor cr = rs.query(uriPaciente, PacienteDAO.COLUMNS_PACIENTE, null, null, null);
        //el paciente no existe hay que registrarlo
        if(cr.getCount() == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public void verificarRegistroMedico(final Cardiologo car, final ContentResolver rs){
        Uri uriCardiologoId = MonitorECGContrato.CardiologoEntry.buildCardiologoId(car.idCardiologo);
        Cursor cursor = rs.query(uriCardiologoId, CardiologoDAO.PROYECCION_VERIFICAR_CARDIOLOGO, null, null, null);
        //verificar si el cursor tiene datos
        int count = cursor.getCount();
        //no existe el cardiologo regstrado en la bd
        if(count == 0){
            //obtener el cardiologo del servidor
            ServicioWeb.obtenerCardiologo(car, new Callback<Cardiologo>() {
                @Override
                public void onResponse(Call<Cardiologo> call, Response<Cardiologo> response) {
                    Cardiologo cardiologo = response.body();
                    CardiologoDAO.insertarCardiologo(cardiologo, rs);
                }

                @Override
                public void onFailure(Call<Cardiologo> call, Throwable t) {
                    Log.e("LoginFinal","No se obtuvó el cardiologo con el id: "+car.idCardiologo);
                }
            });
        }
    }

    private void authenticatorFinish(Bundle bundle){
        if(accountAuthenticatorResponse != null){
            if (bundle != null) {
                accountAuthenticatorResponse.onResult(bundle);
            } else {
                accountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            accountAuthenticatorResponse = null;
        }

    }

    private void finishLogin(String correo, String pass){
        Bundle data = new Bundle();
        String accountType = getString(R.string.account_type);

        data.putString(AccountManager.KEY_ACCOUNT_NAME, correo);
        data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        Intent intent = new Intent();
        intent.putExtras(data);

        final Account account = new Account(correo,accountType);

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(accountType);

        Account cuentaMatch = null;

        for (Account cuenta: accounts){
            if(cuenta.name.equals(correo)){
                cuentaMatch = cuenta;
                break;
            }
        }

        if(cuentaMatch != null){
            //solo se coloca la contraseña
            am.setPassword(cuentaMatch, pass);
        }else{
            //no existe la cuenta hay que crearla
            if(am.addAccountExplicitly(account, pass, null)){
                authenticatorFinish(data);
                setResult(RESULT_OK, intent);
            }
            //no se creo correctamente 
            else{
                authenticatorFinish(null);
                Log.e(LOG_TAG, "No se creo correctamente la cuenta");
            }
        }
    }

    public void onClickRegistrarse(View view) {
        //verificar si hay conexion
        boolean online = NetworkUtil.isOnline(this);
        if(online){
            Intent intentRegistrarse = new Intent(this,Registrarse.class);
            startActivity(intentRegistrarse);
        }
        else{
            Toast.makeText(this,"Verifica tu conexión a internet",Toast.LENGTH_SHORT).show();
        }
    }

}
