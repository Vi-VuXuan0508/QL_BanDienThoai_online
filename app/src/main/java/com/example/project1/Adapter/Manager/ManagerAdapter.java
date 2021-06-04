package com.example.project1.Adapter.Manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.Main;
import com.example.project1.Model.OrderDetail;
import com.example.project1.Model.ProductInformation;
import com.example.project1.Model.ProductModel;
import com.example.project1.Model.UserModel;
import com.example.project1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ViewHolder> {
    Activity context;
    ArrayList<ProductModel> productModels;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public ManagerAdapter(Activity context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_manager,null);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txttitle.setText(productModels.get(position).getTitle());
        holder.txtprice.setText(String.valueOf(productModels.get(position).getPrice())+" VND");
        getcount(productModels.get(position).getIdproduct(),holder);
        getuser(Main.iduser,holder);
       // ảnh

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle,txtprice,txtcount,txtname,txtaddress,txtphone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txt_manager_title);
            txtprice = itemView.findViewById(R.id.txt_manager_price);
            txtname = itemView.findViewById(R.id.txt_manager_name);
            txtcount = itemView.findViewById(R.id.txt_manager_count);
            txtaddress = itemView.findViewById(R.id.txt_manager_address);
            txtphone = itemView.findViewById(R.id.txt_manager_phone);
        }
    }
    private void getuser(String iduser,final ViewHolder holder)
    {
        database.child("user").child(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel model = snapshot.getValue(UserModel.class);
                    holder.txtaddress.setText(model.getAddress());
                    holder.txtname.setText(model.getFullname());
                    holder.txtphone.setText(String.valueOf(model.getPhone()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getcount(String idproduct, final ViewHolder holder)
    {
        database.child("orderdetail").orderByChild("idproduct").equalTo(idproduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    DataSnapshot next = iterator.next();
                    OrderDetail model = next.getValue(OrderDetail.class);
                    holder.txtcount.setText(String.valueOf(model.getCount()+" Sản Phẩm "));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
