    package com.example.project1.Adapter.Payment;

    import android.app.Activity;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.project1.Model.CartModel;
    import com.example.project1.Model.Coupon;
    import com.example.project1.Model.OrderModel;
    import com.example.project1.Model.PaymentModel;
    import com.example.project1.Model.UserModel;
    import com.example.project1.R;
    import com.example.project1.work.Cart;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.Iterator;

    public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
        Activity context;
        ArrayList<PaymentModel> paymentModels;
        ArrayList<UserModel> userModels = new ArrayList<>();
        private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        public PaymentAdapter(Activity context, ArrayList<PaymentModel> paymentModels) {
            this.context = context;
            this.paymentModels = paymentModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = context.getLayoutInflater();
            View view = inflater.inflate(R.layout.admin_payment_item,null);
            return (new ViewHolder(view));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.txtprice.setText(String.valueOf(paymentModels.get(position).getPrice()));
            getdate(paymentModels.get(position).getIdorder(),holder);
            getnameuser(paymentModels.get(position).getIduser(),holder);
            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.child("payment").child(paymentModels.get(position).getIdpay()).removeValue();
                    notifyDataSetChanged();
                }
            });

           // ảnh

        }
        @Override
        public int getItemCount() {
            return paymentModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname,txtdate,txtprice;
            ImageView imgdelete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtname = itemView.findViewById(R.id.admin_payment_name);
                txtprice = itemView.findViewById(R.id.admin_payment_price);
                txtdate = itemView.findViewById(R.id.admin_payment_date);
                imgdelete = itemView.findViewById(R.id.admin_payment_delete);

            }
        }
        private void getnameuser(String iduser, final ViewHolder holder)
        {
            reference.child("user").child(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Iterable<DataSnapshot> iterable = snapshot.getChildren();
                        Iterator<DataSnapshot> iterator = iterable.iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot next = iterator.next();
                            UserModel model = next.getValue(UserModel.class);
                            holder.txtname.setText(model.getFullname());
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void getdate(String idorder, final ViewHolder holder)
        {
            reference.child("order").child(idorder).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshotIterable =  snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterable.iterator();



                        OrderModel model = snapshot.getValue(OrderModel.class);
    //                    holder.txtdate.setText(model.getTime());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
