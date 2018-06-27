package dao;

import dao.annotations.Table;
import dao.annotations.fields.Id;
import dao.annotations.fields.Name;
import dao.annotations.fields.Price;
import dao.interfaces.GenericDao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, ID>
        implements GenericDao<T, ID> {

    protected final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public T create(T t) throws Exception {
        T entityOfT = setEntityDataFromInputEntity(t);
        Class<T> typeOfEntity = (Class<T>) entityOfT.getClass();
        Field[] fields = typeOfEntity.getDeclaredFields();
        String tableName = typeOfEntity.getAnnotation(Table.class).name();

        String query = "INSERT INTO " + tableName + " (" + fields[1].getName()
                + ", " + fields[2].getName() + ") VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Name) {
                    statement.setString(1, (String) field.get(entityOfT));
                } else if (annotation instanceof Price) {
                    statement.setDouble(2, (Double) field.get(entityOfT));
                }
            }
        }
        return entityOfT;
    }

    @Override
    public T read(ID id) throws Exception {
        T entityOfT = (T) this.getTypeOfParameter(0).newInstance();
        Class<T> typeOfEntity = (Class<T>) entityOfT.getClass();
        Field[] fields = typeOfEntity.getDeclaredFields();
        String tableName = entityOfT.getClass().getAnnotation(Table.class).name();

        String query =  "SELECT " + fields[0].getName() + ", " + fields[1].getName() + ", "
                + fields[2].getName() + " FROM " + tableName + " WHERE " + fields[0].getName() + " = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, (Long) id);
        ResultSet resultSet = statement.executeQuery();

        resultSet.first();
        return setEntityDataFromDb(resultSet);
    }

    @Override
    public List<T> readAll() throws Exception {
        T entityOfT = (T) this.getTypeOfParameter(0).newInstance();
        Class<T> typeOfEntity = (Class<T>) entityOfT.getClass();
        Field[] fields = typeOfEntity.getDeclaredFields();
        String tableName = entityOfT.getClass().getAnnotation(Table.class).name();

        String query = "SELECT " + fields[0].getName() + ", " + fields[1].getName() + ", "
                + fields[2].getName() + " FROM " + tableName;
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        List<T> listOfEntities = new ArrayList<>();
        while (resultSet.next()) {
            T tempEntity = this.setEntityDataFromDb(resultSet);
            listOfEntities.add(tempEntity);
        }
        return listOfEntities;
    }

    @Override
    public T update(T t) throws Exception {
        T entityOfT = setEntityDataFromInputEntity(t);
        Class<T> typeOfEntity = (Class<T>) entityOfT.getClass();
        Field[] fields = typeOfEntity.getDeclaredFields();
        String tableName = typeOfEntity.getAnnotation(Table.class).name();

        String query = "UPDATE " + tableName + " SET " + fields[2].getName() + " = ? WHERE "
                + fields[1].getName() + " = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Price) {
                    statement.setDouble(1, (Double) field.get(entityOfT));
                } else if (annotation instanceof Name) {
                    statement.setString(2, (String) field.get(entityOfT));
                }
            }
        }

        if (statement.executeUpdate() == 0) {
            return (T) t.getClass().newInstance();
        } else if (statement.executeUpdate() == 1) {
            query = "SELECT " + fields[0].getName() + ", " + fields[1].getName() + ", " + fields[2].getName()
                    + " FROM " + tableName + " WHERE " + fields[1].getName() + " = ?";
            statement = connection.prepareStatement(query);
            Field f = typeOfEntity.getDeclaredField(fields[1].getName());
            f.setAccessible(true);
            statement.setString(1, (String) f.get(entityOfT));
            ResultSet resultSet = statement.executeQuery();
            resultSet.first();
            return setEntityDataFromDb(resultSet);
        }
        return entityOfT;
    }

    @Override
    public void delete(ID id) throws Exception {
        T entityOfT = (T) this.getTypeOfParameter(0).newInstance();
        Class<T> typeOfEntity = (Class<T>) entityOfT.getClass();
        Field[] fields = typeOfEntity.getDeclaredFields();
        String tableName = entityOfT.getClass().getAnnotation(Table.class).name();

        String query = "DELETE " + " FROM " + tableName + " WHERE " + fields[0].getName() + " = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, (Long) id);
        statement.executeUpdate();
    }

    private Class<?> getTypeOfParameter(int index) {
        Class actualType = this.getClass();
        ParameterizedType typeOfSuperclass = (ParameterizedType) actualType.getGenericSuperclass();
        Type[] typesOfArguments = typeOfSuperclass.getActualTypeArguments();
        return  (Class<T>) typesOfArguments[index];
    }

    private T setEntityDataFromDb(ResultSet resultSet) throws Exception {
        T entityOfT = (T) this.getTypeOfParameter(0).newInstance();
        Field[] fields = entityOfT.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Id) {
                    field.set(entityOfT, resultSet.getLong(field.getName()));
                } else if (annotation instanceof Name) {
                    field.set(entityOfT, resultSet.getString(field.getName()));
                } else if (annotation instanceof Price) {
                    field.set(entityOfT, resultSet.getDouble(field.getName()));
                }
            }
        }
        return entityOfT;
    }

    private T setEntityDataFromInputEntity(T t) throws Exception {
        T entityOfT = (T) this.getTypeOfParameter(0).newInstance();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Field f = entityOfT.getClass().getDeclaredField(field.getName());
            f.setAccessible(true);
            f.set(entityOfT, f.get(t));
        }
        return entityOfT;
    }
}