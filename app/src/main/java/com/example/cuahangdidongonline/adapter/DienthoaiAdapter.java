package com.example.cuahangdidongonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangdidongonline.R;
import com.example.cuahangdidongonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DienthoaiAdapter extends BaseAdapter {
    Context context; // khai báo 1 màn hình
    ArrayList<Sanpham> arraydienthoai; // mảng chứa giá trị phù hợp với sản phẩm

    public DienthoaiAdapter(Context context, ArrayList<Sanpham> arraydienthoai) {
        this.context = context;
        this.arraydienthoai = arraydienthoai;
    } // constructor

    @Override
    public int getCount() {
        return arraydienthoai.size();
    }// lấy ra kích thước của mảng

    @Override
    public Object getItem(int i) {
        return arraydienthoai.get(i);
    }// lấy ra các thuộc tính cửa item có trong mảng

    @Override
    public long getItemId(int i) {
        return i;
    }// vị trí của item trong mảng

    public class ViewHolder {
        public TextView txttendienthoai, txtgiadienthoai, txtmotadienthoai;
        public ImageView imgdienthoai;
    }// khi có dữ liệu sẽ gán lại

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null; // khởi tạo 1 class của ViewHolder
        if (view == null) {// nếu view đầu tiên là null
            viewHolder = new ViewHolder();// khởi tạo 1 viewHolder
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_dienthoai, null);//gán layout dong_dienthoai vào view
            viewHolder.txttendienthoai = (TextView) view.findViewById(R.id.textviewdienthoai);
            viewHolder.txtgiadienthoai = (TextView) view.findViewById(R.id.textviewgiadienthoai);
            viewHolder.txtmotadienthoai = (TextView) view.findViewById(R.id.textviewmotadienthoai);
            viewHolder.imgdienthoai = (ImageView) view.findViewById(R.id.imageviewdienthoai);// gọi thuộc tính trong class viewHolder và gán id
            view.setTag(viewHolder);// set các id vào view
        } else {
            viewHolder = (ViewHolder) view.getTag(); // khi có dữ liệu, gán ánh xạ
        }
        //gán dữ liệu lên cho view
        Sanpham sanpham = (Sanpham) getItem(i); // gọi lại khuôn sản phẩm của từng sản phẩm
        viewHolder.txttendienthoai.setText(sanpham.getTensanpham());// gán dữ liệu vào textview
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);// định dạng lại giá
        String format = decimalFormat.format(sanpham.getGiasanpham());
        viewHolder.txtgiadienthoai.setText("Giá: " + format + " Đ");
        viewHolder.txtmotadienthoai.setMaxLines(2);// giới hạn mô tả
        viewHolder.txtmotadienthoai.setEllipsize(TextUtils.TruncateAt.END);// nếu nhiều hơn có dấu ...
        viewHolder.txtmotadienthoai.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())// gán hình ảnh
                .placeholder(R.drawable.no_image)// nếu ko có hiển thị ảnh k có
                .error(R.drawable.error)// nếu lỗi hiển thị ảnh lỗi
                .into(viewHolder.imgdienthoai);// hiển thị ảnh
        return view;
    }
}
