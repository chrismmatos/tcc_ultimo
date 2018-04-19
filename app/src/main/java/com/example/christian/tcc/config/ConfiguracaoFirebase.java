package com.example.christian.tcc.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by christian on 05/04/2018.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebaseDatabase(){

        if(referenciaFirebase ==null) {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if( autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;

    }

}
