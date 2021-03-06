package com.example.daniel.table_food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.daniel.table_food.Interface.ItemClickListener;
import com.example.daniel.table_food.Model.Food;
import com.example.daniel.table_food.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    public RecyclerView recyler_food;
    public RecyclerView.LayoutManager layoutManagerFlist;
    public String categoryId="";
    public FirebaseDatabase databaseFlist;
    public DatabaseReference foodListFlist;
    public FirebaseRecyclerAdapter<Food, FoodViewHolder> adapterFlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        databaseFlist = FirebaseDatabase.getInstance();
        foodListFlist = databaseFlist.getReference("Food");

        recyler_food = findViewById(R.id.recycler_food);
        recyler_food.setHasFixedSize(true);
        layoutManagerFlist = new LinearLayoutManager(this);
        recyler_food.setLayoutManager(layoutManagerFlist);

        if(getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            loadListFood(categoryId);
        }

    }

    private void loadListFood(String categoryId) {
        adapterFlist= new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item, FoodViewHolder.class,
                foodListFlist.orderByChild("MenuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

                //final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapterFlist.getRef(position).getKey());
                        startActivity(foodDetail);
                        overridePendingTransition(R.anim.goup,R.anim.godown);
                    }
                });
            }
        };

        recyler_food.setAdapter(adapterFlist);
    }
}
