package com.example.cuahangdidongonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangdidongonline.R;
import com.example.cuahangdidongonline.activity.ChiTietSanPham;
import com.example.cuahangdidongonline.model.Sanpham;
import com.example.cuahangdidongonline.util.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanphamAdapter extends RecyclerView.Adapter<SanphamAdapter.ItemHolder> {

    private Context context;
    private ArrayList<Sanpham> arraysanpham;

    public SanphamAdapter(Context context, ArrayList<Sanpham> arraysanpham) {
        this.context = context;
        this.arraysanpham = arraysanpham;
    }

    // tạo ra đối tượng ViewHolder, trong nó chứa View hiển thị dữ liệu
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanphammoinhat, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    // set data vào ViewHolder
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Sanpham sanpham = arraysanpham.get(position);
        holder.txttensanpham.setText(sanpham.getTensanpham());

        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(sanpham.getGiasanpham());
        holder.txtgiasanpham.setText("Giá: " + format + " Đ");

        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(holder.imghinhsanpham);
    }

    // cho biết số phần tử của dữ liệu
    @Override
    public int getItemCount() {
        return arraysanpham.size();
    }

    /**
     * Lớp nắm giữ cấu trúc view
     */
    public class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imghinhsanpham;
        public TextView txttensanpham, txtgiasanpham;

        public ItemHolder(View itemView) {
            super(itemView);
            imghinhsanpham = (ImageView) itemView.findViewById(R.id.imageviewsanpham);
            txtgiasanpham = (TextView) itemView.findViewById(R.id.textviewgiasanpham);
            txttensanpham = (TextView) itemView.findViewById(R.id.textviewtensanpham);

            // Xử lý khi item được bấm
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChiTietSanPham.class);
                    intent.putExtra("thongtinsanpham", arraysanpham.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CheckConnection.ShowToast_Short(context, arraysanpham.get(getPosition()).getTensanpham());
                    context.startActivity(intent);
                }
            });
        }
    }
}
