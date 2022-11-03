package com.example.cuahangdidongonline.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangdidongonline.R;
import com.example.cuahangdidongonline.adapter.PhukienAdapter;
import com.example.cuahangdidongonline.model.Sanpham;
import com.example.cuahangdidongonline.util.CheckConnection;
import com.example.cuahangdidongonline.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhuKienActivity extends AppCompatActivity {

    Toolbar toolbarphukien;
    ListView lvphukien;
    PhukienAdapter phukienAdapter;
    ArrayList<Sanpham> mangphukien;
    int idphukien = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    mHandler mHandler;
    boolean limitdata = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phu_kien);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            GetIdloaisp();
            ActionToolbar();
            LoadMoreData();
            GetData(page);
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Kiểm tra kết nối");
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.cuahangdidongonline.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanphukien + String.valueOf(Page);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tenphukien = "";
                int Giaphukien = 0;
                String Hinhanhphukien = "";
                String Mota = "";
                int Idspphukien = 0;
                if (response != null && response.length() != 2) {
                    lvphukien.removeFooterView(footerview);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenphukien = jsonObject.getString("tensp");
                            Giaphukien = jsonObject.getInt("giasp");
                            Hinhanhphukien = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspphukien = jsonObject.getInt("idsanpham");
                            mangphukien.add(new Sanpham(id, Tenphukien, Giaphukien, Hinhanhphukien, Mota, Idspphukien));
                            phukienAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    limitdata = true;
                    lvphukien.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết sản phẩm");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(idphukien));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarphukien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarphukien.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetIdloaisp() {
        idphukien = getIntent().getIntExtra("idloaisanpham", -1);

    }

    private void LoadMoreData() {
        lvphukien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", mangphukien.get(position));

                startActivity(intent);
            }
        });

        lvphukien.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && limitdata == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();

                }

            }
        });
    }

    private void Anhxa() {
        toolbarphukien = (Toolbar) findViewById(R.id.toolbarphukien);
        lvphukien = (ListView) findViewById(R.id.listviewphukien);
        mangphukien = new ArrayList<>();
        phukienAdapter = new PhukienAdapter(getApplicationContext(), mangphukien);
        lvphukien.setAdapter(phukienAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        mHandler = new mHandler();
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvphukien.addFooterView(footerview);
                    break;
                case 1:
                    page++;
                    GetData(page);
                    isLoading = false;
                    break;


            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
