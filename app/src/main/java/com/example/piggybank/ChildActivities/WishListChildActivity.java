package com.example.piggybank.ChildActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.WishListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piggybank.R;

import java.util.ArrayList;

public class WishListChildActivity extends AppCompatActivity {

    private String childId;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_child);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        getSupportActionBar().setTitle("Wish List");
        this.childId = getIntent().getExtras().getString("childId");
        //setup recyclerview
        recyclerView = findViewById(R.id.wishlistRVChild2);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        makeRV();
    }


    public void openCreateWishListItemActivity(View view) {
        Intent intent = new Intent(WishListChildActivity.this, CreateWishListItemActivity.class);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    public void makeRV() {
        Get get = new Get();
        get.getWishListItemsByChildId(childId, new OnSuccessListener<ArrayList<WishListItem>>() {
            @Override
            public void onSuccess(ArrayList<WishListItem> items) {
                TextView text = findViewById(R.id.noWishlistItemsChild2);
                //check if there are no transactions and display the no transactions textview if there are none
                if(items.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapter = new WishListItemAdapter(items);
                recyclerView.setAdapter(adapter);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        makeRV();
    }

    private class WishListItemAdapter extends RecyclerView.Adapter<WishListItemAdapter.ItemHolder> {

        private ArrayList<WishListItem> items;
        public WishListItemAdapter(ArrayList<WishListItem> items) {
            this.items = items;
        }
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_wish_list_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            final WishListItem item = items.get(position);
            holder.item.setText(item.getItem());
            holder.cost.setText("$"+item.getCost());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WishListChildActivity.this, WishListItemDetailsChildActivity.class);
                    intent.putExtra("item", item.getItem());
                    intent.putExtra("cost", item.getCost());
                    intent.putExtra("id", item.getItemId());
                    intent.putExtra("childId", childId);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ItemHolder extends RecyclerView.ViewHolder {

            private TextView item;
            private TextView cost;
            private LinearLayout layout;

            public ItemHolder(@NonNull View itemView) {
                super(itemView);
                this.item = itemView.findViewById(R.id.wishListItem);
                this.cost = itemView.findViewById(R.id.wishListItemCost);
                this.layout = itemView.findViewById(R.id.wishListHolderLayout);
            }
        }
    }

}
