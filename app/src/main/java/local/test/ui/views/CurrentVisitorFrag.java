package local.test.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import local.test.R;
import local.test.models.Visitor;

public class CurrentVisitorFrag extends Fragment{

    private static final String TAG = "CurrentVisitorFrag";
    
    private RecyclerView                recyclerView;
    private Adapter                     adapter;
    private RecyclerView.LayoutManager  layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag1_layout,container,false);

        final ArrayList<Visitor> visitors = new ArrayList<>();
        createRecyclerView(rootView,visitors);

        return rootView;
    }

    private void createRecyclerView(View rootView, final ArrayList<Visitor> visitors){

        recyclerView = rootView.findViewById(R.id.recycle1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());

        adapter = new Adapter(visitors);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                visitors.get(position);
            }
        });
    }


}
