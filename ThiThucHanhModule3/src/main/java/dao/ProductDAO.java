package dao;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/manager_products?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";


    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<Product> findAll() {
        String SELECT_ALL_PRODUCTS = "SELECT * FROM products ;";
        List<Product> productsList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name_products = rs.getString("name_product");
                int price = rs.getInt("price");
                Integer quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description_product");
                int category = rs.getInt("category");
                productsList.add(new Product(id, name_products, price, quantity, color, description, category));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return productsList;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String INSERT_PRODUCT = "INSERT INTO products(name_product,price,quantity,color,description_product,category)" +
                "VALUES(?,?,?,?,?,?)";
        boolean isInsert = false;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getColor());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getCategory());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            isInsert = true;
            connection.commit();
        } catch (SQLException e) {
            printSQLException(e);
            connection.rollback();
            isInsert = false;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return isInsert;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String UPDATE_PRODUCT_SQL = "UPDATE products SET name_product = ? ,price = ?, quantity =?, color = ?, description_product =?, category = ? WHERE id = ?";
        boolean rowUpdated = false;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getColor());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getCategory());
            preparedStatement.setInt(7, product.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            rowUpdated = false;
        } finally {
            connection.close();
        }
        return rowUpdated;
    }


    @Override
    public Product findById(int id) {
        String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
        Product product = null;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name_products = rs.getString("name_product");
                int price = rs.getInt("price");
                Integer quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description_product");
                int category = rs.getInt("category");
                product = new Product(id, name_products, price, quantity, color, description, category);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    @Override
    public List<Product> searchByName(String keySearch) {
        String SEARCH_BY_NAME = "SELECT * FROM products WHERE name_product LIKE ?";
        List<Product> productsList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BY_NAME);
            keySearch = "%" + keySearch + "%";
            preparedStatement.setString(1, keySearch);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name_products = rs.getString("name_product");
                int price = rs.getInt("price");
                Integer quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description_product");
                int category = rs.getInt("category");
                productsList.add(new Product(id, name_products, price, quantity, color, description, category));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return productsList;
    }

    public boolean remove(int id) throws SQLException {
        String DELETE = "DELETE FROM products  WHERE id = ?";

        Connection connection = null;
        boolean isRemoveData = false;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            isRemoveData = preparedStatement.executeUpdate() > 0;

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return isRemoveData;
    }
}
