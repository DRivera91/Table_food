package com.example.daniel.table_food;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.daniel.table_food.Database.Database;
import com.example.daniel.table_food.Model.Food;
import com.example.daniel.table_food.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView NamefoodDetail;
    TextView PricefoodDetail;
    TextView DescfoodDetail;
    ImageView Imgfood;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";
    FirebaseDatabase databaseFDetail;
    DatabaseReference foodsFDetail;
    Food currentFoodFDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        databaseFDetail = FirebaseDatabase.getInstance();
        foodsFDetail = databaseFDetail.getReference("Food");

        numberButton=findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFoodFDetail.getName(),
                        numberButton.getNumber(),
                        currentFoodFDetail.getPrice(),
                        currentFoodFDetail.getDiscount()

                ));
                Toast.makeText(FoodDetail.this, R.string.addCar,Toast.LENGTH_SHORT).show();
            }
        });

        DescfoodDetail=findViewById(R.id.food_description);
        NamefoodDetail=findViewById(R.id.name_food);
        PricefoodDetail=findViewById(R.id.food_price);
        Imgfood=findViewById(R.id.img_sfood);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);

        if(getIntent()!=null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty()){
            getDetailFood(foodId);
        }

    }

    private void getDetailFood(final String foodId) {
        foodsFDetail.child(foodId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFoodFDetail = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFoodFDetail.getImage()).into(Imgfood);
                collapsingToolbarLayout.setTitle(currentFoodFDetail.getName());
                PricefoodDetail.setText(currentFoodFDetail.getPrice());
                NamefoodDetail.setText(currentFoodFDetail.getName());
                DescfoodDetail.setText(currentFoodFDetail.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}