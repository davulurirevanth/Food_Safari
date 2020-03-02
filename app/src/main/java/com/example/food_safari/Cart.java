package com.example.food_safari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn=(Button) findViewById(R.id.next_process_btn);
        txtTotalAmount=(TextView) findViewById(R.id.total_price);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");


  FirebaseRecyclerOptions<CartModel> options=new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(cartListRef.child("UserView")
         .child("FoodItems"), CartModel.class).build();

  FirebaseRecyclerAdapter<CartModel,CartViewHolder> adapter=
          new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {
                  holder.txtProductQuantity.setText(model.getQuantity());
                  holder.txtProductName.setText(model.getPname());
                  holder.txtProductPrice.setText(model.getPrice());


              }

              @NonNull
              @Override
              public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                  View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                  CartViewHolder holder=new CartViewHolder(view);
                  return holder;
              }
          };
  recyclerView.setAdapter(adapter);
  adapter.startListening();





    }
}
