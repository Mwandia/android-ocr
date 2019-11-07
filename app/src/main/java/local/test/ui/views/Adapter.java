package local.test.ui.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import local.test.R;
import local.test.models.Visitor;

public class Adapter extends RecyclerView.Adapter<Adapter.VisitorHolder> {

    private List<Visitor> mVisitors = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class VisitorHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView id;
        public TextView name;

        public VisitorHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.entry);

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

    public Adapter(ArrayList<Visitor> visitors){
        this.mVisitors = visitors;
    }

    @NonNull
    @Override
    public VisitorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards,viewGroup,false);
        VisitorHolder vh = new VisitorHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorHolder visitorHolder, int i) {
        Visitor item = mVisitors.get(i);

        visitorHolder.id.setText(item.getId());
        visitorHolder.name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mVisitors.size();
    }

    public void setVisitors(List<Visitor> visitors){
        this.mVisitors = visitors;
        notifyDataSetChanged();
    }


}
