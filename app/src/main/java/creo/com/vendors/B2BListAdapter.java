package creo.com.vendors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import creo.com.vendors.utils.ApiClient;


public class B2BListAdapter extends RecyclerView.Adapter<B2BListAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    public int quantity = 0;
    private ArrayList<ProductB2BPojo> bookingPojos;
    private Context context;

    public B2BListAdapter(Context ctx, ArrayList<ProductB2BPojo> bookingPojos){

        inflater = LayoutInflater.from(ctx);
        this.bookingPojos = bookingPojos;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.b2blist_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        context=parent.getContext();
        return holder;

//        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.minus.setVisibility(View.VISIBLE);
//                quantity++;
//                viewHolder.add.setText(String.valueOf(quantity));
//            }
//        });
//        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                quantity--;
//                viewHolder.add.setText(String.valueOf(quantity));
//            }
//        });


    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.product_name.setText(bookingPojos.get(position).getProduct_name());
        quantity = Integer.parseInt(bookingPojos.get(position).getQuantity());
        holder.price.setText(bookingPojos.get(position).getPrice());
        ApiClient.directsell_cartcount = String.valueOf(quantity);
        Picasso.with(context).load(bookingPojos.get(position).getImage()).into(holder.imageView);
        //holder.dest.setText(bookingPojos.get(position).getDestination());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>=0) {

                    holder.minus.setVisibility(View.VISIBLE);
                    quantity = Integer.parseInt(bookingPojos.get(position).getQuantity()) + 1;
                    bookingPojos.get(position).setQuantity(String.valueOf(quantity));
                    ApiClient.directsell_cartcount = String.valueOf(quantity);
                    holder.add.setText(bookingPojos.get(position).getQuantity());
                    ApiClient.productB2BPojo = bookingPojos;

                    ApiClient.ids.add(bookingPojos.get(position).getId());
                    ApiClient.quantity.add(holder.add.getText().toString());

//                ApiClient.ids.add(productPojo.get(position).getId());
//                ApiClient.quantity.add(holder.add.getText().toString());
//                Log.d("beffrrr","hgghfgh"+ApiClient.quantity.get(position));
                    // if (!(ApiClient.ids.contains(productPojo.get(position).getId()))){

                }
                   // Toast.makeText(context1,"gfghf"+ApiClient.productB2BPojo.get(position).getPrice()+productPojo.get(position).getQuantity(),Toast.LENGTH_SHORT).show();
               // }
//                Log.d("hbhjbvjhbjh","jjbhjbbhj"+ApiClient.quantity.get(position)+productPojo.get(position).getQuantity());
//                if (ApiClient.ids.contains(productPojo.get(position).getId())){
//                    ApiClient.ids.remove(productPojo.get(position).getId());
//                    ApiClient.prices.remove(productPojo.get(position).getPrice());
//                    ApiClient.quantity.remove(productPojo.get(position).getQuantity());
//                    ApiClient.name.remove(productPojo.get(position).getProduct_name());
//                    ApiClient.images.remove(productPojo.get(position).getImage());
//
//                    ApiClient.ids.add(productPojo.get(position).getId());
//                    ApiClient.prices.add(productPojo.get(position).getPrice());
//                    ApiClient.quantity.add(productPojo.get(position).getQuantity());
//                    ApiClient.name.add(productPojo.get(position).getProduct_name());
//                    ApiClient.images.add(productPojo.get(position).getImage());
//                    Toast.makeText(context1,"Already present"+productPojo.get(position).getQuantity(),Toast.LENGTH_SHORT).show();
//                }
//                if (!(ApiClient.ids.contains(productPojo.get(position).getId()))) {
//                    ApiClient.ids.add(productPojo.get(position).getId());
//                    ApiClient.prices.add(productPojo.get(position).getPrice());
//                    ApiClient.quantity.add(productPojo.get(position).getQuantity());
//                    ApiClient.name.add(productPojo.get(position).getProduct_name());
//                    ApiClient.images.add(productPojo.get(position).getImage());
//                }
               // Log.d("positionnnn","posss"+position+productPojo.get(position).getQuantity());
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>0) {

                    quantity = Integer.parseInt(bookingPojos.get(position).getQuantity())-1;
                    ApiClient.directsell_cartcount = String.valueOf(quantity);
                    bookingPojos.get(position).setQuantity(String.valueOf(quantity));
                    holder.add.setText(bookingPojos.get(position).getQuantity());
                    ApiClient.productB2BPojo = bookingPojos;
                }
                if (quantity==0){
                   ApiClient.directsell_cartcount = String.valueOf(quantity);
                   holder.add.setText("ADD");
                   holder.minus.setVisibility(View.GONE);
                   bookingPojos.get(position).setQuantity("0");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingPojos.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        TextView product_name,minus,add,plus;
        public TextView name,price;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.imageView =  itemView.findViewById(R.id.image);
            this.price = itemView.findViewById(R.id.price);
            this.product_name = itemView.findViewById(R.id.product_name);
            this.minus = itemView.findViewById(R.id.minus);
            this.add = itemView.findViewById(R.id.add);
            this.plus = itemView.findViewById(R.id.plus);


        }

    }

}
