package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.R;

/**
 * Created by trianaandaluciaprietogalvan on 11/04/16.
 */
public class AccountUtil {
    public static Account getAccount(Context contexto){
        AccountManager am = AccountManager.get(contexto);
        String accountType = contexto.getString(R.string.account_type);
        Account[] accounts = am.getAccountsByType(accountType);
        Account cuentaEncontrada = null;
        String usuario = MonitorECGUtils.obtenerUltimoUsuarioEnSesion(contexto);
        for (Account cuenta : accounts) {
            if (cuenta.name.equals(usuario)) {
                cuentaEncontrada = cuenta;
                break;
            }
        }
        return  cuentaEncontrada;
    }
}
