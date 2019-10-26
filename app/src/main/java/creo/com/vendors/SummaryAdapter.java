package creo.com.vendors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.squareup.picasso.Picasso;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {


  public List<CartPojo> productPojo;
  Context context1 ;

  public SummaryAdapter(List<CartPojo> productPojo, Context context) {
    this.productPojo = productPojo;
    this.context1 = context;
  }

  @Override
  public SummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    View listItem= layoutInflater.inflate(R.layout.order_item, parent, false);
    SummaryAdapter.ViewHolder viewHolder = new SummaryAdapter.ViewHolder(listItem);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(SummaryAdapter.ViewHolder holder, int position) {


    holder.name.setText(productPojo.get(position).getName());
    holder.price.setText(productPojo.get(position).getPrice());
    holder.quantity.setText(productPojo.get(position).getQuantity());
//        holder.imageView.setImageResource((Picasso.with(context).load(productPojo[position].getImage())));
    Picasso.with(context1).load(productPojo.get(position).getImage()).into(holder.imageView);

  }

  @Override
  public int getItemCount() {
    return productPojo.size();
  }


  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView name,price,quantity,remove;

    public ViewHolder(View itemView) {
      super(itemView);
      this.imageView =  itemView.findViewById(R.id.img);
      this.name =  itemView.findViewById(R.id.title);
      this.quantity = itemView.findViewById(R.id.quantity);
      this.price = itemView.findViewById(R.id.price);
    }

  }


}
