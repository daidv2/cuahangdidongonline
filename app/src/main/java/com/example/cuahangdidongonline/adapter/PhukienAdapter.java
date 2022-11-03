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

public class PhukienAdapter extends BaseAdapter {
    public PhukienAdapter(Context context, ArrayList<Sanpham> arrayphukien) {
        this.context = context;
        this.arrayphukien = arrayphukien;
    }

    Context context;
    ArrayList<Sanpham> arrayphukien;

    @Override
    public int getCount() {
        return arrayphukien.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayphukien.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txttenphukien, txtgiaphukien, txtmotaphukien;
        public ImageView imgphukien;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_phukien, null);
            viewHolder.txttenphukien = (TextView) view.findViewById(R.id.textviewtenphukien);
            viewHolder.txtgiaphukien = (TextView) view.findViewById(R.id.textviewgiaphukien);
            viewHolder.txtmotaphukien = (TextView) view.findViewById(R.id.textviewmotaphukien);
            viewHolder.imgphukien = (ImageView) view.findViewById(R.id.imageviewphukien);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttenphukien.setText(sanpham.getTensanpham());
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(sanpham.getGiasanpham());

        viewHolder.txtgiaphukien.setText("Giá: " + format + " Đ");
        viewHolder.txtmotaphukien.setMaxLines(2);
        viewHolder.txtmotaphukien.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotaphukien.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(viewHolder.imgphukien);
        return view;
    }
}
