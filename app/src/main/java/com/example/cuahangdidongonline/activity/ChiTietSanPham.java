package com.example.cuahangdidongonline.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.cuahangdidongonline.R;
import com.example.cuahangdidongonline.model.Giohang;
import com.example.cuahangdidongonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPham extends AppCompatActivity {
    Toolbar toolbarChitiet;// khai báo các thuộc tính có trong thiết kế giao diện
    ImageView imgChitiet;
    TextView txtten, txtgia, txtmota;
    Spinner spinner;
    Button btndatmua;
    // tạo biến hứng cho các giá trị trong GetInformation
    String TenChitiet = "";
    int GiaChitiet = 0;
    String HinhanhChitiet = "";
    String MotaChitiet = "";
    int Idsanpham = 0;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        Anhxa();
        ActionToolbar();
        GetInformation();
        CatchEventSpinner();
        EventButton();
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

    //bắt sự kiện click cho button
    private void EventButton() {
        btndatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() > 0) {// nếu mảng > 0
                    boolean exit = false;
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());// lấy số lượng từ spinner
                    for (int i = 0; i < MainActivity.manggiohang.size(); i++) {// update lại sl nếu có dl
                        if (MainActivity.manggiohang.get(i).getIdsp() == id) {
                            MainActivity.manggiohang.get(i).setSoluongsp((MainActivity.manggiohang.get(i).getSoluongsp() + sl));
                            if (MainActivity.manggiohang.get(i).getSoluongsp() >= 10) {
                                MainActivity.manggiohang.get(i).setSoluongsp(10);
                            }
                            //update lại tổng tiền thanh toán
                            MainActivity.manggiohang.get(i).setGiasp(GiaChitiet * MainActivity.manggiohang.get(i).getSoluongsp());
                            exit = true;
                        }
                    }
                    if (exit == false) {
                        // tính tổng tiền thanh toán
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soluong * GiaChitiet;
                        MainActivity.manggiohang.add(new Giohang(id, TenChitiet, Giamoi, HinhanhChitiet, soluong));
                    }
                } else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soluong * GiaChitiet;
                    MainActivity.manggiohang.add(new Giohang(id, TenChitiet, Giamoi, HinhanhChitiet, soluong));
                }
                Intent intent = new Intent(getApplicationContext(), com.example.cuahangdidongonline.activity.Giohang.class);
                startActivity(intent);
            }
        });
    }

    //bắt sự kiện cho spinner
    private void CatchEventSpinner() {
        Integer[] soluong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};// khai báo giá trị của spinner
        // gọi lại bản vẽ
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(arrayAdapter);// truyền vào bản vẽ
    }

    //lấy dữ liệu màn hình khác truyền cho màn hình này
    private void GetInformation() {
        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");// nhận dữ liệu là 1 dạng Object
        id = sanpham.getID();// gán dữ liệu cho các biến ở phía trên khai báo
        TenChitiet = sanpham.getTensanpham();
        GiaChitiet = sanpham.getGiasanpham();
        HinhanhChitiet = sanpham.getHinhanhsanpham();
        MotaChitiet = sanpham.getMotasanpham();
        Idsanpham = sanpham.getIDSanpham();
        // gán dữ liệu lên trên cho các layout
        txtten.setText(TenChitiet);
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);// định dạng lại giá
        String format = decimalFormat.format(GiaChitiet);
        txtgia.setText("Giá: " + format + " Đ");
        txtmota.setText(MotaChitiet);
        Picasso.with(getApplicationContext()).load(HinhanhChitiet)// gán hình ảnh
                .placeholder(R.drawable.no_image)// nếu ko có hiển thị ảnh k có
                .error(R.drawable.error)// nếu lỗi hiển thị ảnh lỗi
                .into(imgChitiet);// hiển thị ảnh
    }

    // bắt sự kiện quay về cho Toolbar
    private void ActionToolbar() {
        setSupportActionBar(toolbarChitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// tạo nút home
        toolbarChitiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //khởi tạo và gán các giá trị id vào thuộc tính
    private void Anhxa() {
        toolbarChitiet = (Toolbar) findViewById(R.id.toolbarchitietsanpham);//gán id cho từng thuộc tính khai báo bên trên
        imgChitiet = (ImageView) findViewById(R.id.imageviewchitietsanpham);
        txtten = (TextView) findViewById(R.id.textviewtenchitietsanpham);
        txtgia = (TextView) findViewById(R.id.textviewgiachitietsanpham);
        txtmota = (TextView) findViewById(R.id.textviewmotachitietsanpham);
        spinner = (Spinner) findViewById(R.id.spinner);
        btndatmua = (Button) findViewById(R.id.buttondatmua);
    }
}
