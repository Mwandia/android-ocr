package local.test.room;

import android.icu.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visitors")
public class Visitor {

    private String birthdate;
    private String entry;
    private String full_name;

    @PrimaryKey
    private int id;

    private String image; //path to image
    private String sex;

    public Visitor(int id, String full_name, String birthdate, String sex, String image){
        this.birthdate = birthdate;
        this.full_name = full_name;
        this.id = id;
        this.image = image;
        this.sex = sex;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.entry = sdf.format(LocalDateTime.now());
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getFull_name() {
        return full_name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getEntry() {
        return entry;
    }

    public String getSex() {
        return sex;
    }
}
