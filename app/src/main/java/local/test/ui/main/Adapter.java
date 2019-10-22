package local.test.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import local.test.R;
import local.test.models.VisitorCard;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    private static final String TAG = "Adapter";
    
    private ArrayList<VisitorCard> visitors;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView id;
        public TextView name;

        public AdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.thumb);
            name = itemView.findViewById(R.id.supertext);
            id = itemView.findViewById(R.id.subtext);

            itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public Adapter(ArrayList<VisitorCard> visitors){
        this.visitors = visitors;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards,viewGroup,false);
        AdapterViewHolder avh = new AdapterViewHolder(v,mlistener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder adapterViewHolder, int i) {
        VisitorCard item = visitors.get(i);

        adapterViewHolder.image.setImageResource(item.getImage());
        adapterViewHolder.id.setText(item.getId());
        adapterViewHolder.name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

}
