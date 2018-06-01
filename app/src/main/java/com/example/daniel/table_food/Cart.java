package com.example.daniel.table_food;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.table_food.Common.Common;
import com.example.daniel.table_food.Database.Database;
import com.example.daniel.table_food.Model.Order;
import com.example.daniel.table_food.Model.Request;
import com.example.daniel.table_food.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerViewCart;
    RecyclerView.LayoutManager layoutManagerCart;

    FirebaseDatabase databaseCart;
    DatabaseReference requestsCart;

    TextView txtTotalPriceCart;
    Button btnPlaceCart;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        databaseCart = FirebaseDatabase.getInstance();
        requestsCart=databaseCart.getReference("Requests");

        //Firebase
        databaseCart = FirebaseDatabase.getInstance();
        requestsCart=databaseCart.getReference("Requests");

        recyclerViewCart= findViewById(R.id.listCart);
        recyclerViewCart.setHasFixedSize(true);
        layoutManagerCart = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManagerCart);

        txtTotalPriceCart=findViewById(R.id.total);
        btnPlaceCart= findViewById(R.id.btnPlaceOrder);

        btnPlaceCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.size()>0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this,R.string.empty,Toast.LENGTH_SHORT).show();
            }


        });

        loadListFood();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("¡un paso mas!");
        alertDialog.setMessage("Ingrese su número de mesa: ");
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getEmail(),
                        edtAddress.getText().toString(),
                        txtTotalPriceCart.getText().toString(),
                        cart
                );
                //registro en firebase
                requestsCart.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,R.string.thanks,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerViewCart.setAdapter(adapter);

        //calculo del precio total
        int total=0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("es", "CO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPriceCart.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int posicion) {
        cart.remove(posicion);
        new Database(this).cleanCart();
        for(Order item:cart)
            new Database(this).addToCart(item);
        loadListFood();
    }
}
