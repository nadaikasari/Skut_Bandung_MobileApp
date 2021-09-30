package com.sttbandung.skutbandung.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sttbandung.skutbandung.ClickListener.ItemClickListener;
import com.sttbandung.skutbandung.MainActivity;
import com.sttbandung.skutbandung.R;
import com.sttbandung.skutbandung.adapter.DestinasiAdapter;
import com.sttbandung.skutbandung.adapter.KategoriDestinasiAdapter;
import com.sttbandung.skutbandung.adapter.KecamatanDestinasiAdapter;
import com.sttbandung.skutbandung.handler.Config;
import com.sttbandung.skutbandung.pojo.Destinasi;
import com.sttbandung.skutbandung.pojo.KategoriDestinasi;
import com.sttbandung.skutbandung.pojo.Kecamatan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListDestinasiActivity extends AppCompatActivity implements DestinasiAdapter.OnItemClickListener {

    public static final String EXTRA_NAME = "nama_destinasi";
    public static final String EXTRA_JUMLAH = "jumlah_pengunjung";
    public static final String EXTRA_GAMBAR = "gambar_destinasi";
    public static final String EXTRA_ALAMAT = "alamat_destinasi";
    public static final String EXTRA_HARGA = "harga_destinasi";
    public static final String EXTRA_SUHU = "suhu";
    public static final String EXTRA_KELEMBAPAN = "kelembapan";
    public static final String EXTRA_KETERANGAN = "keterangan_destinasi";
    public static final String EXTRA_ID_USER = "id_user";
    public static final String EXTRA_UID_USER = "uid_user";
    public static final String EXTRA_SALDO_USER = "saldo_user";


    String kategori, config, ArrayName, idUser, UidUser, SaldoUser;
    private RecyclerView recyclerView;
    private DestinasiAdapter adapter;
    private KecamatanDestinasiAdapter adapterKecamatan;
    private ArrayList<Destinasi> arrayList;
    private ArrayList<Kecamatan> arrayListKecamatan;
    private RequestQueue mRequestQueue;
    private MenuItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_destinasi);

        //get kategori
        Intent intent = getIntent();
        KategoriDestinasi coll = intent.getParcelableExtra("DESTINASI");
        kategori = coll.getNama_destinasi();

        //get data intent
        idUser = intent.getStringExtra("ID");
        UidUser = intent.getStringExtra("UID");
        SaldoUser = intent.getStringExtra("SALDO");

        recyclerView = findViewById(R.id.rv_destinasi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        arrayListKecamatan = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);

        getData();

        if (kategori.equals("Wisata di Berbagai Kecamatan")) {
            config = Config.DESTINASI_KATEGORI;
            ArrayName = "Kategori";
            getKecamatan();
        }


    }


    private void getData() {
        if (kategori.equals("Wisata Sejarah")) {
            config = Config.DESTINASI1;
            ArrayName = "destinasicategori";
        } else if (kategori.equals("Wisata Hiburan")) {
            config = Config.DESTINASI2;
            ArrayName = "destinasicategori";
        } else if (kategori.equals("Wisata Malam")) {
            config = Config.DESTINASI3;
            ArrayName = "destinasicategori";
        } else if (kategori.equals("Seluruh Kategori Wisata")) {
            config = Config.DESTINASIALL;
            ArrayName = "destinasi";
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, config, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(ArrayName);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String id = hit.getString("id_destinasi");
                                String name = hit.getString("nama_destinasi");
                                String pengunjung = hit.getString("jumlah_pengunjung");
                                String gambar = hit.getString("gambar_destinasi");
                                String alamat = hit.getString("alamat_destinasi");
                                String kategori_wisata = hit.getString("kategori_wisata");
                                String kategori_kecamatan = hit.getString("kategori_kecamatan");
                                String harga = hit.getString("harga_destinasi");
                                String suhu = hit.getString("suhu");
                                String kelembapan = hit.getString("kelembapan");
                                String keterangan = hit.getString("keterangan_destinasi");

                                arrayList.add(new Destinasi(id, pengunjung, name, gambar, alamat, kategori_wisata, kategori_kecamatan, keterangan, harga, suhu, kelembapan));
                            }
                            adapter = new DestinasiAdapter(arrayList, ListDestinasiActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(ListDestinasiActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void getKecamatan() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, config, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(ArrayName);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String kecamatan = hit.getString("kategori_kecamatan");

                                arrayListKecamatan.add(new Kecamatan(kecamatan));
                            }
                            adapterKecamatan = new KecamatanDestinasiAdapter(arrayListKecamatan, ListDestinasiActivity.this);
                            recyclerView.setAdapter(adapterKecamatan);
                            adapterKecamatan.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Toast.makeText(ListDestinasiActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, DetailDestinasiActivity.class);
        Destinasi clickedItem = arrayList.get(position);
        i.putExtra(EXTRA_NAME, clickedItem.getNama());
        i.putExtra(EXTRA_JUMLAH, clickedItem.getPengunjung());
        i.putExtra(EXTRA_GAMBAR, clickedItem.getGambar());
        i.putExtra(EXTRA_ALAMAT, clickedItem.getAlamat());
        i.putExtra(EXTRA_HARGA, clickedItem.getHarga());
        i.putExtra(EXTRA_SUHU, clickedItem.getSuhu());
        i.putExtra(EXTRA_KELEMBAPAN, clickedItem.getKelembapan());
        i.putExtra(EXTRA_KETERANGAN, clickedItem.getKeterangan());
        i.putExtra(EXTRA_ID_USER, idUser);
        i.putExtra(EXTRA_UID_USER, UidUser);
        i.putExtra(EXTRA_SALDO_USER, SaldoUser);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_option_menu, menu);

        //search listener
        MenuItem searchItem = menu.findItem(R.id.search);
//            SearchView searchView = (SearchView) searchItem.getActionView();
//            searchView.setQueryHint(getResources().getString(R.string.search_hint));
//            // below line is to call set on query text listener method.
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    // inside on query text change method we are
//                    // calling a method to filter our recycler view.
//                    filter(newText);
//                    return false;
//                }
//            });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Destinasi> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Destinasi item : arrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }


}