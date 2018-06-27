package dao;

import dao.interfaces.ProductDao;
import models.Product;

import java.sql.Connection;

public class ProductDaoImpl
        extends AbstractDao<Product, Long>
        implements ProductDao {

    public ProductDaoImpl(Connection connection) {
        super(connection);
    }

    public Connection getConnection() {
        return super.getConnection();
    }
}