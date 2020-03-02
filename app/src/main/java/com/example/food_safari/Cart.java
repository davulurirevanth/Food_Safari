package com.example.food_safari;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
    private int overallPrice=0;



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

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTotalAmount.setText("Total price= "+String.valueOf(overallPrice));

                Intent intent=new Intent(Cart.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overallPrice));
                startActivity(intent);
                finish();

            }
        });

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
                  holder.txtProductQuantity.setText("Quantity= "+model.getQuantity());
                  holder.txtProductName.setText("Item= "+model.getPname());
                  holder.txtProductPrice.setText("Price= "+model.getPrice());

                  int oneTyprProductTPrice=((Integer.valueOf(model.getPrice())))*((Integer.valueOf(model.getQuantity())));
                  overallPrice=overallPrice+oneTyprProductTPrice;

                  holder.itemView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          CharSequence options[] =new CharSequence[]{
                                  "Edit",
                                  "Remove"

                          };
                          AlertDialog.Builder builder= new AlertDialog.Builder(Cart.this);
                          builder.setTitle("Cart Options");
                          builder.setItems(options, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  if(i==0)
                                  {
//                                     Intent intent=new Intent(Cart.this,ProductDetailsActivity.class);
//                                      intent.putExtra("pid",model.getPid());
//                                      startActivity(intent);
                                  }
                                  if(i==1)
                                  {
                                     cartListRef.child("User View")
                                             .child("Products")
//                                             .child(model.getPid())  //25V
                                             .removeValue()
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful()){
                                                         Toast.makeText(Cart.this,"Item removed",Toast.LENGTH_SHORT).show();
                                                        // Intent intent=new Intent(Cart.this,Home.class);
//                                      startActivity(intent);
                                                     }

                                                 }
                                             });


                                  }

                              }
                          });
                          builder.show();


                      }
                  });


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
