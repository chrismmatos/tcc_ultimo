package com.example.christian.tcc.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by christian on 05/04/2018.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private  String NOME_ARQUIVO = "app.preferencias";
    private  int MODE = 0;
    private  SharedPreferences.Editor editor;

    public Preferencias (Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();

    }

    public void salvarUsuarioPreferencias(){


    }

}
