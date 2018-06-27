import dao.ProductDaoImpl;
import models.Product;

public class Main {
    public static void main(String[] args) throws Exception {

        ProductDaoImpl dao = Factory.getProductDao();
        System.out.println(dao.readAll());
        System.out.println(dao.read(33L));
        System.out.println(dao.update(new Product("iPhone", 22.22)));
        dao.delete(34L);
        System.out.println(dao.readAll());
        System.out.println(dao.create(new Product("iPhoneeshe", 2222.2222)));
        System.out.println(dao.readAll());
    }
}