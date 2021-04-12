package uk.ac.ucl.streats.data;

public class DynamicRVModel {

    String name;
    private int image;
    int pos;

    public DynamicRVModel(String name, int image, int pos) {
        this.name = name;
        this.image = image;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public int getImage(){
        return image;
    }
}
