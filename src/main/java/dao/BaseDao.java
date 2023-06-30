package dao;

import annotation.DaoName;
import annotation.DaoObject;
import util.JdbcUtil;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javaok
 * 2023/6/21 10:24
 */

public abstract class BaseDao<T> {
    private final Class<?> entityClass;

    public BaseDao() {
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        try {
            entityClass = Class.forName(actualTypeArguments[0].getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findColumn(ResultSet resultSet, String columnName) {
        try {
            return resultSet.findColumn(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    private static Object generateByGeneralClass(Class<?> entityClass, ResultSet fields) throws NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, InvocationTargetException {
        Object ret = entityClass.getConstructor().newInstance();
        for (Field declaredField : entityClass.getDeclaredFields()) {
            int modifiers = declaredField.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            declaredField.setAccessible(true);

            DaoObject daoObject = declaredField.getType().getAnnotation(DaoObject.class);
            if (daoObject != null) {
                Object object = generateByGeneralClass(declaredField.getType(), fields);
                declaredField.set(ret, object);
                continue;
            }

            DaoName daoName = declaredField.getAnnotation(DaoName.class);
            String sqlColumnName = daoName == null ? declaredField.getName() : daoName.value();

            int i = findColumn(fields, sqlColumnName);
            if (i > 0) {
                declaredField.set(ret, fields.getObject(i));
            }
        }
        return ret;
    }


    @SuppressWarnings("unchecked")
    private T generateByClass(Class<?> entityClass, ResultSet fields) throws NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException, InvocationTargetException {
        return (T) generateByGeneralClass(entityClass, fields);
    }

    public Integer update(String sql, Object... params) {
        Connection connection = JdbcUtil.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T query(String sql, Object... params) {
        Connection connection = JdbcUtil.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return generateByClass(entityClass, resultSet);
            }
            return null;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> queries(String sql, Object... params) {
        Connection connection = JdbcUtil.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<T> rets = new ArrayList<>();

            while (resultSet.next()) {
                rets.add(generateByClass(entityClass, resultSet));
            }

            return rets;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <E> E getValue(String sql, Object... params) throws SQLException {
        Connection connection = JdbcUtil.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }

            return null;
        }
    }
}
