package local.test.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import local.test.room.Visitor;
import local.test.room.VisitorDao;
import local.test.room.VisitorDatabase;

public class VisitorRepository {

    private VisitorDao visitorDao;
    private LiveData<List<Visitor>> allVisitors;

    public VisitorRepository(Application application){
        VisitorDatabase database = VisitorDatabase.getInstance(application);
        visitorDao = database.visitorDao();
        allVisitors = visitorDao.getAllVisitors();
    }

    public void insert(Visitor visitor){
        new InsertVisitorAsyncTask(visitorDao).execute(visitor);
    }

    public void update(Visitor visitor){
        new UpdateVisitorAsyncTask(visitorDao).execute(visitor);
    }

    public void delete(Visitor visitor){
        new DeleteVisitorAsyncTask(visitorDao).execute(visitor);
    }

    public LiveData<List<Visitor>> getAllVisitors() {
        return allVisitors;
    }

    private static class InsertVisitorAsyncTask extends AsyncTask<Visitor,Void,Void>{
        private VisitorDao visitorDao;

        private InsertVisitorAsyncTask(VisitorDao visitorDao){
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.insert((visitors[0]));
            return null;
        }
    }

    private static class UpdateVisitorAsyncTask extends AsyncTask<Visitor,Void,Void>{
        private VisitorDao visitorDao;

        private UpdateVisitorAsyncTask(VisitorDao visitorDao){
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.update((visitors[0]));
            return null;
        }
    }

    private static class DeleteVisitorAsyncTask extends AsyncTask<Visitor,Void,Void>{
        private VisitorDao visitorDao;

        private DeleteVisitorAsyncTask(VisitorDao visitorDao){
            this.visitorDao = visitorDao;
        }

        @Override
        protected Void doInBackground(Visitor... visitors) {
            visitorDao.delete((visitors[0]));
            return null;
        }
    }
}