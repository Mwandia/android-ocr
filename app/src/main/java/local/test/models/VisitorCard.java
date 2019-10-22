package local.test.models;

public class VisitorCard {
    private int image;
    private String name;
    private String id;
    private String time;

    public VisitorCard(int imageResource, String name, String id, String time){
        image = imageResource;
        this.name = name;
        this.id = id;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }
}
