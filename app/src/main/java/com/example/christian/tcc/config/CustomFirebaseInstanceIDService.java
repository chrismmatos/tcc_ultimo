package com.example.christian.tcc.config;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;

public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        String token  = FirebaseInstanceId.getInstance().getToken();

        if(usuarioLogado!=null) {
            usuarioLogado.setToken(token);
            usuarioLogado.salvar();
        }
    }
}
