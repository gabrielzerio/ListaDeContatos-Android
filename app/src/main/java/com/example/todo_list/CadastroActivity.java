package com.example.todo_list;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtNome;

    private EditText txtNumero;

    private Switch swFavorito;
    private int contatoId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView ibVoltar = findViewById(R.id.ibVoltar);
        ibVoltar.setOnClickListener(this);
        Button btnSalvar= findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(this);


        txtNome = findViewById(R.id.txtNome);
        txtNumero = findViewById(R.id.txtNumero);
        swFavorito = findViewById(R.id.swFavorito);


        if(getIntent().hasExtra("contato")){
            Contato contato = (Contato) getIntent().getSerializableExtra("contato");
            txtNome.setText(contato.getName());
            txtNumero.setText(contato.getNumeroCelular());
            swFavorito.setChecked(contato.isFavorito());
            contatoId = contato.getId();
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ibVoltar){
            finish();
        }else if(v.getId() == R.id.btnSalvar){


            Contato contato = new Contato(txtNome.getText().toString(), txtNumero.getText().toString(), swFavorito.isChecked());
            if(contatoId > 0){
                atualizarContato(contato);
            }else if(contatoId == 0){
                cadastrarContato(contato);
            }
        }
    }

    private void atualizarContato(Contato contato) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContatosApi contatosApi = retrofit.create(ContatosApi.class);

        Call<Void> atualizarContato = contatosApi.atualizarContato(contatoId, contato);
        atualizarContato.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Contato atualizada!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro ao atualizar Contato!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(CadastroActivity.this, "Erro de comunicação com Api", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void cadastrarContato(Contato contato){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContatosApi contatosApi = retrofit.create(ContatosApi.class);
        Call<Contato> criaContatoServer = contatosApi.criarContato(contato);
        criaContatoServer.enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(CadastroActivity.this, "Erro ao salvar Contato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable throwable) {
                Toast.makeText(CadastroActivity.this, "Erro de comunicação API", Toast.LENGTH_LONG).show();
            }
        });
    }

}