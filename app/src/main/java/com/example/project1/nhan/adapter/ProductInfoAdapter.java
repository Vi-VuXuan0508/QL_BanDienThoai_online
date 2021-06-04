package com.example.project1.nhan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Model.ProductInformation;
import com.example.project1.R;
import com.example.project1.nhan.DAO.ProductInfoDAO;
import com.example.project1.nhan.Main2Activity;
import com.example.project1.nhan.asynctask.ShowAsyncTask;

import java.util.ArrayList;

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.MyViewHolder>{
    Context context;
    ArrayList<ProductInformation> list;
    RecyclerView rcv;
    public ProductInfoAdapter(Context context,ArrayList<ProductInformation> list, RecyclerView rcv)
    {
        this.context=context;
        this.list=list;
        this.rcv=rcv;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_product_info,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.info1.setText("Màn Hình: "+list.get(position).getScreen());
        holder.info2.setText("Camera Sau: "+list.get(position).getCamera());
        holder.info3.setText("Camera Trước: "+list.get(position).getCameraselfie());
        holder.info4.setText("Ram: "+list.get(position).getRam());
        holder.info5.setText("Rom: "+list.get(position).getRom());
        holder.info6.setText("CPU: "+list.get(position).getCpu());
        holder.info7.setText("GPU: "+list.get(position).getGpu());
        holder.info8.setText("Pin: "+list.get(position).getPin());
        holder.info9.setText("Sim: "+list.get(position).getSim());
        holder.info10.setText("Hệ Điều Hành: "+list.get(position).getSystem());
        holder.info11.setText("Origin: "+list.get(position).getOrigin());
        holder.info12.setText("Năm Phát Hành: "+list.get(position).getYearofmanufacture());

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialogUpdate(list.get(position));
                return false;
            }
        });


    }

    public void dialogUpdate(final ProductInformation c)
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_product_info_update,null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        dialog.show();

        final EditText info1=view.findViewById(R.id.info1Update);
        final EditText info2=view.findViewById(R.id.info2Update);
        final EditText info3=view.findViewById(R.id.info3Update);
        final EditText info4=view.findViewById(R.id.info4Update);
        final EditText info5=view.findViewById(R.id.info5Update);
        final EditText info6=view.findViewById(R.id.info6Update);
        final EditText info7=view.findViewById(R.id.info7Update);
        final EditText info8=view.findViewById(R.id.info8Update);
        final EditText info9=view.findViewById(R.id.info9Update);
        final EditText info10=view.findViewById(R.id.info10Update);
        final EditText info11=view.findViewById(R.id.info11Update);
        final EditText info12=view.findViewById(R.id.info12Update);

        info1.setText(c.getScreen());
        info2.setText(c.getCamera());
        info3.setText(c.getCameraselfie());
        info4.setText(c.getRam());
        info5.setText(c.getRom());
        info6.setText(c.getCpu());
        info7.setText(c.getGpu());
        info8.setText(c.getPin());
        info9.setText(c.getSim());
        info10.setText(c.getSystem());
        info11.setText(c.getOrigin());
        info12.setText(c.getYearofmanufacture());

        Button btnThem=view.findViewById(R.id.btnUpdateInfo);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screen=info1.getText().toString();
                String camera=info2.getText().toString();
                String cameraselfie=info3.getText().toString();
                String ram=info4.getText().toString();
                String rom=info5.getText().toString();
                String cpu=info6.getText().toString();
                String gpu=info7.getText().toString();
                String pin=info8.getText().toString();
                String sim=info9.getText().toString();
                String system=info10.getText().toString();
                String origin=info11.getText().toString();
                String yearofmanufacture=info12.getText().toString();

                ProductInformation info=new ProductInformation(c.getIdproduct(),screen,camera,cameraselfie,ram,rom,cpu,gpu,pin,sim,system,origin,yearofmanufacture);
                ProductInfoDAO.updateInfo(context,c.getIdproductInformation(),info);
                new ShowAsyncTask(context,rcv,c.getIdproduct()).execute(3);
                /*
                Intent intent=new Intent(context, Main2Activity.class);
                intent.putExtra("code","info");
                context.startActivity(intent);*/
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView item;
        TextView info1, info2, info3, info4, info5, info6, info7, info8, info9, info10, info11, info12;
        public MyViewHolder(View view)
        {
            super(view);
            item=view.findViewById(R.id.item_product_info);
            info1=view.findViewById(R.id.info1);
            info2=view.findViewById(R.id.info2);
            info3=view.findViewById(R.id.info3);
            info4=view.findViewById(R.id.info4);
            info5=view.findViewById(R.id.info5);
            info6=view.findViewById(R.id.info6);
            info7=view.findViewById(R.id.info7);
            info8=view.findViewById(R.id.info8);
            info9=view.findViewById(R.id.info9);
            info10=view.findViewById(R.id.info10);
            info11=view.findViewById(R.id.info11);
            info12=view.findViewById(R.id.info12);
        }
    }
}
