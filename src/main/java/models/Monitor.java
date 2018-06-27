package models;

public class Monitor extends Product<Monitor> {
    private Integer diagonal;
    private Integer xPixels;
    private Integer yPixels;

    public Monitor(Integer diagonal,
                   Integer xPixels,
                   Integer yPixels) {

        this.diagonal = diagonal;
        this.xPixels = xPixels;
        this.yPixels = yPixels;
    }

    public Monitor(String name, Double price,
                   Integer diagonal,
                   Integer xPixels,
                   Integer yPixels) {

        super(name, price);
        this.diagonal = diagonal;
        this.xPixels = xPixels;
        this.yPixels = yPixels;
    }

    public Monitor(Long id, String name, Double price,
                   Integer diagonal,
                   Integer xPixels,
                   Integer yPixels) {

        super(id, name, price);
        this.diagonal = diagonal;
        this.xPixels = xPixels;
        this.yPixels = yPixels;
    }

    public Integer getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(Integer diagonal) {
        this.diagonal = diagonal;
    }

    public Integer getXPixels() {
        return xPixels;
    }

    public void setXPixels(Integer xPixels) {
        this.xPixels = xPixels;
    }

    public Integer getYPixels() {
        return yPixels;
    }

    public void setYPixels(Integer yPixels) {
        this.yPixels = yPixels;
    }
}