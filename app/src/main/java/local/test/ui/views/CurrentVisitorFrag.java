package local.test.ui.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import local.test.CameraActivity;
import local.test.R;
import local.test.VisitorsActivity;
import local.test.models.Visitor;
import local.test.viewmodels.VisitorViewModel;

import static android.app.Activity.RESULT_OK;

public class CurrentVisitorFrag extends Fragment{

    private static final String         TAG = "CurrentVisitorFrag";
    public static final int             ADD_VISITOR_REQUEST = 1;
    
    private RecyclerView                recyclerView;
    private Adapter                     adapter;
    private RecyclerView.LayoutManager  layoutManager;
    private VisitorViewModel            visitorViewModel;
    private FloatingActionButton        mButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag1_layout,container,false);

        final ArrayList<Visitor> visitors = new ArrayList<>();
        createRecyclerView(rootView,visitors);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_VISITOR_REQUEST && resultCode == RESULT_OK) {
            String id = data.getStringExtra(CameraActivity.NEW_ID);
            String name = data.getStringExtra(CameraActivity.NEW_NAME);
            String dob = data.getStringExtra(CameraActivity.NEW_DOB);
            String sex = data.getStringExtra(CameraActivity.NEW_SEX);

            VisitorViewModel visitorViewModel = ViewModelProviders.of(this).get(VisitorViewModel.class);
            Visitor visitor = new Visitor(id, name, dob, sex);
            visitorViewModel.insert(visitor);
        }
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

        visitorViewModel = ViewModelProviders.of(this).get(VisitorViewModel.class);
        visitorViewModel.getCurrVisitors().observe(this, new Observer<List<Visitor>>(){
            @Override
            public void onChanged(List<Visitor> visitors){
                //update RecyclerView
                adapter.setVisitors(visitors);
            }
        });


    }


}
