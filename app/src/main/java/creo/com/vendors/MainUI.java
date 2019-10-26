package creo.com.vendors;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainUI extends AppCompatActivity {

    ImageView imageView;
    boolean doubleBackToExitPressedOnce = false;

    private List<CardRecyclerViewItem> carItemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        imageView=findViewById(R.id.img);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        initializeCarItemList();

        // Create the recyclerview.
        RecyclerView carRecyclerView = (RecyclerView)findViewById(R.id.card_view_recycler_list);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // Set layout manager.
        carRecyclerView.setLayoutManager(gridLayoutManager);

        // Create car recycler view data adapter with car item list.
        RecyclerViewDataAdapter carDataAdapter = new RecyclerViewDataAdapter(carItemList);
        // Set data adapter.
        carRecyclerView.setAdapter(carDataAdapter);

    }

    /* Initialise car items in list. */
    private void initializeCarItemList()
    {
        if(carItemList == null)
        {
            carItemList = new ArrayList<CardRecyclerViewItem>();
            carItemList.add(new CardRecyclerViewItem("Manual Orders", R.drawable.ic_add_shopping_cart_black_24dp));
            carItemList.add(new CardRecyclerViewItem("B", R.drawable.ic_add_shopping_cart_black_24dp));
            carItemList.add(new CardRecyclerViewItem("C", R.drawable.ic_add_shopping_cart_black_24dp));
            carItemList.add(new CardRecyclerViewItem("D", R.drawable.ic_add_shopping_cart_black_24dp));
            carItemList.add(new CardRecyclerViewItem("E", R.drawable.ic_add_shopping_cart_black_24dp));
            carItemList.add(new CardRecyclerViewItem("F", R.drawable.ic_add_shopping_cart_black_24dp));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainUI.this,"Press again to exit",Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}