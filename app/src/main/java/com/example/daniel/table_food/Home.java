package com.example.daniel.table_food;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.table_food.Common.Common;
import com.example.daniel.table_food.Interface.ItemClickListener;
import com.example.daniel.table_food.Model.Category;
import com.example.daniel.table_food.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public FirebaseDatabase database;
    public DatabaseReference category;
    public TextView txtFullName;

    public RecyclerView recyler_menu;
    public RecyclerView.LayoutManager layoutManager;
    public FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Menú");
        setSupportActionBar(toolbar);

        //inicializamos firebase
        database= FirebaseDatabase.getInstance();
        category= database.getReference("Category");

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });*/




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Home.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //mostramos el nombre del usuario
        View headerView = navigationView.getHeaderView(0);
        txtFullName=headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getEmail());
        //cargamos el menu
        recyler_menu =findViewById(R.id.recycler_menu);
        recyler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyler_menu.setLayoutManager(layoutManager);

        loadMenu();



    }

    private void loadMenu() {

        adapter= new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                //final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //capturamos el ID del restaurante
                        Intent foodIntent = new Intent(Home.this,FoodList.class);
                        foodIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodIntent);
                        overridePendingTransition(R.anim.goup,R.anim.godown);
                    }
                });
            }
        };
        recyler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Toast.makeText(Home.this, R.string.menu, Toast.LENGTH_SHORT).show();
        } else{
            if (id == R.id.nav_cart) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
                overridePendingTransition(R.anim.goup,R.anim.godown);
            } else {
                if (id == R.id.nav_orders) {
                    Intent orderIntent = new Intent(Home.this, OrderStatus.class);
                    startActivity(orderIntent);
                    overridePendingTransition(R.anim.goup,R.anim.godown);
                } else{
                    if (id == R.id.nav_log_out) {
                        Intent logout = new Intent(Home.this, SignIn.class);
                        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout);
                        overridePendingTransition(R.anim.goup,R.anim.godown);
                    }
                }
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
