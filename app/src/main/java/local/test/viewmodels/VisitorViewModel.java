package local.test.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import local.test.models.Visitor;

public class VisitorViewModel extends AndroidViewModel {
    private VisitorRepository repository;
    private LiveData<List<Visitor>> allVisitors;

    public VisitorViewModel(@NonNull Application application) {
        super(application);
        repository = new VisitorRepository(application);
        allVisitors = repository.getAllVisitors();
    }

    public void insert(Visitor visitor){
        repository.insert(visitor);
    }

    public void update(Visitor visitor){
        repository.update(visitor);
    }

    public void delete(Visitor visitor){
        repository.delete(visitor);
    }


    public LiveData<List<Visitor>> getAllVisitors(){
        return allVisitors;
    }
}