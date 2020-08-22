package ean.ecom.eanshopadmin.main;

public class MainFragmentModel {

    private int image;
    private String name;
    private int ID;

    public MainFragmentModel(int image, String name, int ID) {
        this.image = image;
        this.name = name;
        this.ID = ID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
