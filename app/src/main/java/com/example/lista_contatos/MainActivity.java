package com.example.lista_contatos;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ContatoAdapter contatoAdapter;
    private ListView lvContatos;

    private Switch swFavoritos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvContatos = findViewById(R.id.lvContatos);
        swFavoritos = findViewById(R.id.swFavoritos);

        buscarContatos("");
        editarContato();

        FloatingActionButton botaoFlutuante = findViewById(R.id.botaoFlutuante);
        botaoFlutuante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastrar = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(telaCadastrar);
            }
        });
        swFavoritos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch foi ativado (ligado)
                    buscarContatos("true");
                } else {
                    buscarContatos("");

                }
            }
        });
        lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) parent.getItemAtPosition(position);

                Intent intent = new Intent(Intent.ACTION_DIAL); // ou Intent.ACTION_CALL com permissão
                intent.setData(Uri.parse("tel:" + contato.getNumeroCelular()));

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarContatos("");
    }


    private void editarContato(){
        lvContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) parent.getItemAtPosition(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Escolha uma opção")
                        .setMessage("O que deseja fazer com a contato?")
                        .setPositiveButton("Atualizar", ((dialog, which) -> {

                                Intent telaCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                                telaCadastro.putExtra("contato", contato);
                                startActivity(telaCadastro);

                        }))
                        .setNegativeButton("Remover", ((dialog, which) -> {
                            removerContato(contato.getId());
                        }))
                        .setNeutralButton("Cancelar", null)
                        .show();

                return true;
            }
        });
    }

    private void removerContato(int contatoId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContatosApi contatosApi = retrofit.create(ContatosApi.class);

        Call<Void> deletarContato = contatosApi.deletarContato(contatoId);
        deletarContato.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Contato excluída com sucesso!", Toast.LENGTH_LONG).show();
                    buscarContatos("");
                } else {
                    Toast.makeText(MainActivity.this, "Houve um erro ao excluir contato", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Erro ao comunicar com a Api", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buscarContatos(String soFavoritos){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContatosApi contatosApi = retrofit.create(ContatosApi.class);

        Call<List<Contato>> getContatosServer = contatosApi.getContatos(soFavoritos);

        getContatosServer.enqueue(new Callback<List<Contato>>() {
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                if(response.isSuccessful()){
                    List<Contato> contatos = response.body();
                    contatoAdapter = new ContatoAdapter(MainActivity.this, contatos);
                    lvContatos.setAdapter(contatoAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Erro ao buscar contatos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}