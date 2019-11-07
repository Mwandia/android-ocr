package local.test.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import local.test.R;
import local.test.models.Visitor;
import local.test.viewmodels.VisitorViewModel;


public class CurrentVisitorFrag extends Fragment{

    protected static VisitorViewModel   mVisitorViewModel;

    private RecyclerView                recyclerView;
    private Adapter                     adapter;
    private RecyclerView.LayoutManager  layoutManager;
    private FloatingActionButton        mButton;

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
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                visitors.get(position);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mVisitorViewModel = ViewModelProviders.of(this).get(VisitorViewModel.class);
        mVisitorViewModel.getCurrVisitors().observe(this, new Observer<List<Visitor>>(){
            @Override
            public void onChanged(List<Visitor> visitors){
                //update RecyclerView
                adapter.setVisitors(visitors);
            }
        });


    }


}
