package com.example.christian.tcc.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian on 05/04/2018.
 */


public class Permissao {

    public static boolean validaPermissoes (int requestCode, Activity activity, String [] permissoes){

        List<String> listaPermissoes = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= 23){

            for(String permissao : permissoes){
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager. PERMISSION_GRANTED;
                if(!validaPermissao) listaPermissoes.add(permissao);
            }


            if(listaPermissoes.isEmpty()) return true;

            String [] novasPermissoes = new String [listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode);

        }

        return true;
    }
}
