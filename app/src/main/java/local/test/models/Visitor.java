package local.test.models;

import android.icu.text.SimpleDateFormat;

import java.time.LocalDateTime;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visitors")
public class Visitor {

    private String birthdate;
    private String entry;
    private String exit;
    private String name;

    @PrimaryKey
    private String id;

    private String sex;

    public Visitor(String id, String name, String birthdate, String sex){
        this.birthdate = birthdate;
        this.name = name;
        this.id = id;
        this.sex = sex;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        this.entry = sdf.format(LocalDateTime.now());
        this.exit = "";
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEntry() {
        return entry;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String time){
        this.exit = time;
    }

    public String getSex() {
        return sex;
    }
}
