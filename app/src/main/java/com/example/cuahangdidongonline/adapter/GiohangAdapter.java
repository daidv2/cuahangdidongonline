package com.example.cuahangdidongonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangdidongonline.R;
import com.example.cuahangdidongonline.activity.MainActivity;
import com.example.cuahangdidongonline.model.Giohang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiohangAdapter extends BaseAdapter {
    //constructor
    public GiohangAdapter(Context context, ArrayList<Giohang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    //khai báo các thuộc tính
    Context context;
    ArrayList<Giohang> arraygiohang;
    Intent intent;

    //các pt get, set
    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int i) {
        return arraygiohang.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //gán ánh xạ
    public class ViewHolder {
        public TextView txttengiohang, txtgiagiohang;
        public ImageView imggiohang;
        public Button btnminus, btnvalues, btnplus;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {// nếu view null
            viewHolder = new ViewHolder();//khởi tạo 1 class viewholder
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_giohang, null);// gán dữ liệu dòng giỏ hàng cho view
            // lấy những dl trong class viewholder
            viewHolder.txtgiagiohang = (TextView) view.findViewById(R.id.textviewgiagiohang);
            viewHolder.txttengiohang = (TextView) view.findViewById(R.id.textviewtengiohang);
            viewHolder.imggiohang = (ImageView) view.findViewById(R.id.imageviewgiohang);
            viewHolder.btnminus = (Button) view.findViewById(R.id.buttonminus);
            viewHolder.btnvalues = (Button) view.findViewById(R.id.buttonvalues);
            viewHolder.btnplus = (Button) view.findViewById(R.id.buttonplus);
            view.setTag(viewHolder);
        } else {
            // gán dl nếu có view
            viewHolder = (ViewHolder) view.getTag();
        }
        Giohang giohang = (Giohang) getItem(i);// khởi tạo class của giỏ hàng
        // gán dữ liệu cho từng thuộc tính
        viewHolder.txttengiohang.setText(giohang.getTensp());
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);// định dạng lại giá
        String format = decimalFormat.format(giohang.getGiasp());
        viewHolder.txtgiagiohang.setText("Giá: " + format + " Đ");
        Picasso.with(context).load(giohang.getHinhsp())// gán hình ảnh
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(viewHolder.imggiohang);
        viewHolder.btnvalues.setText(giohang.getSoluongsp() + "");
        int sl = Integer.parseInt(viewHolder.btnvalues.getText().toString());// lấy sl hiện tại
        if (sl >= 10) {//nếu sl >= 10
            viewHolder.btnplus.setVisibility(View.INVISIBLE);// + mờ
            viewHolder.btnminus.setVisibility(View.VISIBLE);// - bình thường
        } else if (sl < 1) {//sl<1
            viewHolder.btnminus.setVisibility(View.INVISIBLE);// - mờ
        } else if (sl >= 1) {// ngược lại bt
            viewHolder.btnminus.setVisibility(View.VISIBLE);
            viewHolder.btnplus.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// bắt sự kiện cho nut +
                //lấy sl mới nhất
                int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) + 1;
                int slht = MainActivity.manggiohang.get(i).getSoluongsp();
                finalViewHolder.btnvalues.setText(slmoinhat + "");
                //lấy giá hiện tại
                long giaht = MainActivity.manggiohang.get(i).getGiasp();
                MainActivity.manggiohang.get(i).setSoluongsp(slmoinhat);
                long giamoinhat = (giaht * slmoinhat) / slht;
                //gán các giá trị mới cho mảng
                MainActivity.manggiohang.get(i).setGiasp(giamoinhat);
                String pattern = "###,###.###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                String format = decimalFormat.format(giamoinhat);
                finalViewHolder.txtgiagiohang.setText("Giá: " + format + " Đ");
                com.example.cuahangdidongonline.activity.Giohang.EventUltil();
                if (slmoinhat > 9) {//nếu sl>9
                    finalViewHolder.btnplus.setVisibility(View.INVISIBLE);// + mờ
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText((String.valueOf(slmoinhat)));
                } else {
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);// hiển thị bt
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText((String.valueOf(slmoinhat)));
                }
            }
        });
        viewHolder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// bắt sự kiên trù cho nút -
                //lấy sl mới nhất
                int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) - 1;
                int slht = MainActivity.manggiohang.get(i).getSoluongsp();
                finalViewHolder.btnvalues.setText(slmoinhat + "");
                long giaht = MainActivity.manggiohang.get(i).getGiasp();
                MainActivity.manggiohang.get(i).setSoluongsp(slmoinhat);
                // tính giá mới nhất
                long giamoinhat = (giaht * slmoinhat) / slht;
                //gán gtri cho mảng
                MainActivity.manggiohang.get(i).setGiasp(giamoinhat);
                String pattern = "###,###.###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                String format = decimalFormat.format(giamoinhat);
                finalViewHolder.txtgiagiohang.setText("Giá: " + format + " Đ");
                com.example.cuahangdidongonline.activity.Giohang.EventUltil();
                if (slmoinhat < 2) {
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnminus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnvalues.setText((String.valueOf(slmoinhat)));
                } else {
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText((String.valueOf(slmoinhat)));
                }
            }
        });
        return view;// trả lại view
    }
}
