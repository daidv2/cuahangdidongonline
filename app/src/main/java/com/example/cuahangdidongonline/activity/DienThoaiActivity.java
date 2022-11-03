package com.example.cuahangdidongonline.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.cuahangdidongonline.adapter.DienthoaiAdapter;
import com.example.cuahangdidongonline.model.Sanpham;
import com.example.cuahangdidongonline.util.CheckConnection;
import com.example.cuahangdidongonline.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbardt; // khai báo các thuộc tính
    ListView lvdt;
    DienthoaiAdapter dienthoaiAdapter;// gọi lại class bản vẽ
    ArrayList<Sanpham> mangdt;// tạo mảng hứng giá trị đưa vào bản vẽ
    int iddt = 0; // khai báo id loại sản phẩm
    int page = 1;
    View footerview;// khái báo 1 view
    boolean isLoading = false;
    mHandler mHandler;
    boolean limitdata = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        Anhxa();
        // nếu có kết nối mới đọc
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        } else {// ngược lại thông báo
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
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

    // bắt trạng thái Load thêm dữ liệu
    private void LoadMoreData() {
        //khi ấn vào sản phẩm chuyển màn hình sang Chi tiết sản phẩm
        lvdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);//khai báo màn hình
                intent.putExtra("thongtinsanpham", mangdt.get(position));// gửi hết dữ liệu từ mảng
                startActivity(intent);// chuyển màn hình
            }
        });
        //bắt sự kiện kéo của Listview
        lvdt.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int FirstItem, int VisibleItem, int TotalItem) {
                // nếu vị trị đầu tiên + số view hiển thị > tổng, bắt vị trí cuối cùng cho listview
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && limitdata == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    //lấy dữ liệu
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());// đọc dữ liệu của đường dẫn
        String duongdan = Server.Duongdandienthoai + String.valueOf(Page);// tạo đường dẫn
        // đọc hết các dữ liệu
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tendt = "";
                int Giadt = 0;
                String Hinhanhdt = "";
                String Mota = "";
                int Idspdt = 0;
                // nếu biến respones khác null thì đọc dữ liệu
                if (response != null && response.length() != 2) {
                    lvdt.removeFooterView(footerview);// khi có dữ liệu thì bỏ
                    try {
                        JSONArray jsonArray = new JSONArray(response);// khởi tạo JsonArray
                        for (int i = 0; i < jsonArray.length(); i++) {// đọc hết tất cả dữ liệu có trong JsonArray
                            JSONObject jsonObject = jsonArray.getJSONObject(i);// khởi tạo JsonObject
                            id = jsonObject.getInt("id");// đưa giá trị vào các biến
                            Tendt = jsonObject.getString("tensp");
                            Giadt = jsonObject.getInt("giasp");
                            Hinhanhdt = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspdt = jsonObject.getInt("idsanpham");
                            mangdt.add(new Sanpham(id, Tendt, Giadt, Hinhanhdt, Mota, Idspdt));// đưa dữ liệu vào trong mảng
                            dienthoaiAdapter.notifyDataSetChanged();// cập nhật Adapter
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {// hết dữ liệu, đẩy ra thông báo " HẾT dl"
                    limitdata = true;
                    lvdt.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết sản phẩm");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        ) {
            //đẩy dữ liệu lên server dưới dạng HashMap
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(iddt));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    // bắt dự kiện quay về cho Toolbar
    private void ActionToolbar() {
        setSupportActionBar(toolbardt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//tạo nứt Home
        toolbardt.setNavigationOnClickListener(new View.OnClickListener() {// khi click vào quay về trang trước
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // lấy id loại sản phẩm
    private void GetIdloaisp() {
        iddt = getIntent().getIntExtra("idloaisanpham", -1);//tìm giá trị id loại sản phẩm
        Log.d("giatriloaisanpham", iddt + "");
    }

    // khởi tạo và gán giá trị id vào trong thuộc tính
    private void Anhxa() {
        toolbardt = (Toolbar) findViewById(R.id.toolbardienthoai); // gán id cho từng thuộc tính
        lvdt = (ListView) findViewById(R.id.listviewdienthoai);
        mangdt = new ArrayList<>();// cấp phát bộ nhớ cho mảng
        dienthoaiAdapter = new DienthoaiAdapter(getApplicationContext(), mangdt);// gọi lại class dienthoaiAdapter
        lvdt.setAdapter(dienthoaiAdapter);// khi có dữ liệu set cho listview
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);// gán footerView cho progressbar
        mHandler = new mHandler();
    }

    // cấp công việc cho Thread
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// nếu thread gửi lên = 0
                    lvdt.addFooterView(footerview);
                    break;
                case 1:// nếu thread gửi = 1, gửi dữ liệu
                    page++;
                    GetData(page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    // tạo luồng chạy song song với tiến trình
    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // gửi tin nhắn sau 3s
            Message message = mHandler.obtainMessage(1);// phương thức liên kết thread với Handler
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
