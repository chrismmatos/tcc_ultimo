package com.example.christian.tcc.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.tcc.R;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;

public class PreferenciasActivity extends AppCompatActivity {
    private TextView txtNumAcompanhamentos;
    private EditText edtNome;
    private TextView txtTipoUsuario;
    private Button btnCancelar;
    private Button btnSalvar;
    private RadioGroup radioGroup;
    private RadioButton radioCar;
    private RadioButton radioVan;
    private RadioButton radioAmbulancia;
    private RadioButton radioMoto;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_activiry);

        radioAmbulancia = (RadioButton) findViewById(R.id.radio_ambulancia) ;
        radioCar = (RadioButton) findViewById(R.id.radio_carro) ;
        radioMoto = (RadioButton) findViewById(R.id.radio_moto) ;
        radioVan = (RadioButton) findViewById(R.id.radio_van) ;
        checkBox = (CheckBox) findViewById(R.id.check_vtr) ;
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        edtNome= (EditText) findViewById(R.id.edt_pref_nome);
        txtNumAcompanhamentos = (TextView) findViewById(R.id.txt_num_acomps);
        txtTipoUsuario = (TextView) findViewById(R.id.txt_pref_usuario);
        btnCancelar = (Button) findViewById(R.id.btn_pref_cancelar);
        btnSalvar = (Button) findViewById(R.id.btn_pref_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaDados(v);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        carregaDados();
    }

    private void carregaDados(){

        if(usuarioLogado.getVtr()!=null) {
            checkBox.setChecked(true);

            switch (usuarioLogado.getVtr()){
                case "Ambulância":
                    radioAmbulancia.setChecked(true);
                    break;
                case "Moto":
                    radioMoto.setChecked(true);
                    break;
                case "Carro":
                    radioCar.setChecked(true);
                    break;
                case "Van":
                    radioVan.setChecked(true);
                    break;
                default:
                    break;
            }

        }
        txtTipoUsuario.setText(usuarioLogado.getTipoUsuario()+ " ("+ usuarioLogado.getTipoPCD()+usuarioLogado.getTipoAgente()+ ")");
        txtNumAcompanhamentos.setText(usuarioLogado.getAcompanhamentosRealizados()+"");
        edtNome.setText(usuarioLogado.getNome());
    }

    public void salvaDados(View v) {

        if (checkBox.isChecked()) {

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_carro:
                    usuarioLogado.setVtr("Carro");
                    break;

                case R.id.radio_ambulancia:
                    usuarioLogado.setVtr("Ambulância");
                    break;

                case R.id.radio_moto:
                    usuarioLogado.setVtr("Moto");
                    break;

                case R.id.radio_van:
                    usuarioLogado.setVtr("Van");
                    break;

                default:
                    break;
            }
        }

        else
            usuarioLogado.setVtr(null);

        usuarioLogado.setNome(edtNome.getText().toString());
        usuarioLogado.salvar();
        Toast.makeText(getApplicationContext(), "Informações atualizadas com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

}
