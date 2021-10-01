package com.sttbandung.skutbandung.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sttbandung.skutbandung.R;
import com.google.zxing.Result;
import com.sttbandung.skutbandung.adapter.DestinasiAdapter;
import com.sttbandung.skutbandung.handler.Config;
import com.sttbandung.skutbandung.pojo.Capture;
import com.sttbandung.skutbandung.pojo.Destinasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopupActivity extends AppCompatActivity {

    private Button btn_voucher, btn_scanqr;
    private EditText isi_voucher;
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        isi_voucher = findViewById(R.id.edit_idvoucher);
        btn_voucher = findViewById(R.id.btn_loadvoucher);
        btn_scanqr = findViewById(R.id.btn_openScan);

        //get intent
        Intent intent = getIntent();
        id_user = intent.getStringExtra("ID_USER");

        btn_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intent = new IntentIntegrator(
                        TopupActivity.this
                );
                intent.setPrompt("For flash using volume key");
                intent.setBeepEnabled(true);
                intent.setOrientationLocked(true);
                intent.setCaptureActivity(Capture.class);
                intent.initiateScan();
            }
        });

        btn_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Topup();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult i = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(i.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    TopupActivity.this
            );

            builder.setTitle("Result");
            builder.setMessage(i.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), "OOPS.. Kamu salah", Toast.LENGTH_SHORT).show();
        }
    }

    public void Topup() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id_user", id_user);
        params.put("id_voucher", isi_voucher.getText());
        client.put(Config.TOP_UP, params,  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d("onSuccess: ", "BERHASIL");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


}