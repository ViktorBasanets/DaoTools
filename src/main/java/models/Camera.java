package models;

public class Camera extends Product<Camera> {

    private Integer pix;

    public Camera(Integer pix) {
        this.pix = pix;
    }

    public Camera(String name, Double price,
                  Integer pix) {
        super(name, price);
        this.pix = pix;
    }

    public Camera(Long id, String name, Double price,
                  Integer pix) {
        super(id, name, price);
        this.pix = pix;
    }

    public Integer getPix() {
        return pix;
    }

    public void setPix(Integer pix) {
        this.pix = pix;
    }
}