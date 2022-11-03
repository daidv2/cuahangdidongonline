package com.example.cuahangdidongonline.model;

public class Giohang {
    //constructor
    public Giohang(int idsp, String tensp, long giasp, String hinhsp, int soluongsp) {
        this.idsp = idsp;//khai báo các thuộc tính có trong giỏ hàng
        this.tensp = tensp;
        this.giasp = giasp;
        this.hinhsp = hinhsp;
        this.soluongsp = soluongsp;
    }

    //khai báo các thuộc tính có trong giỏ hàng
    public int idsp;
    public String tensp;
    public long giasp;
    public String hinhsp;
    public int soluongsp;

    // các phương thức get, set

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiasp() {
        return giasp;
    }

    public void setGiasp(long giasp) {
        this.giasp = giasp;
    }

    public String getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(String hinhsp) {
        this.hinhsp = hinhsp;
    }

    public int getSoluongsp() {
        return soluongsp;
    }

    public void setSoluongsp(int soluongsp) {
        this.soluongsp = soluongsp;
    }
}
