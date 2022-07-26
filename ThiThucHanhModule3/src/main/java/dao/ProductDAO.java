package dao;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/module3?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Thanhtung913";
    private final String INSERT_PRODUCT_SQL = "INSERT INTO `Products` (`name`, `price`, `quantity`, `color`, `description`,`category`) VALUES (?,?, ?, ?, ?,?);";
    private final String SELECT_PRODUCT_BY_ID = "SELECT id, name, price, quantity, color, description, category FROM Products WHERE id = ?;";
    private final String SELECT_ALL_PRODUCT = "SELECT * FROM Products ;";
    private final String DELETE_PRODUCT_SQL = "DELETE FROM Products WHERE id =?;";
    private final String UPDATE_PRODUCT_SQL = "UPDATE Products SET name = ?, price=? ,quantity =?  ,color=? ,description= ?,category=? WHERE id =?;";

    private int noOfRecords;

    public ProductDAO() {
    }

    protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        return connection;
    }

    @Override
    public void inserProduct(Product product) throws SQLException, ClassNotFoundException {
        System.out.println(INSERT_PRODUCT_SQL);
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setInt(2, product.getPrice());
        preparedStatement.setInt(3, product.getQuantity());
        preparedStatement.setString(4, product.getColor());
        preparedStatement.setString(5, product.getDescription());
        preparedStatement.setInt(6, product.getCategory());
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException, ClassNotFoundException {
        boolean rowUpdate = false;
        boolean success = false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getColor());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getCategory());
            preparedStatement.setInt(7, product.getId());
            rowUpdate = preparedStatement.executeUpdate() > 0;
            connection.close();
            success = true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return success;
    }

    @Override
    public Product selectProductById(int id) throws SQLException, ClassNotFoundException {
        Product product = null;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int price = Integer.parseInt(resultSet.getString("price"));
                int quantity = Integer.parseInt(resultSet.getString("quantity"));
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                int category = Integer.parseInt(resultSet.getString("category"));
                product = new Product(id, name, price, quantity, color, description, category);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    @Override
    public List<Product> selectAllProduct() throws SQLException, ClassNotFoundException {
        List<Product> listProduct = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                int category = resultSet.getInt("category");
                listProduct.add(new Product(id, name, price, quantity, color, description, category));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return listProduct;
    }

    @Override
    public boolean deleteProduct(int id) throws SQLException, ClassNotFoundException {
        boolean rowDelete = false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_SQL);
            preparedStatement.setInt(1, id);
            rowDelete = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }

        return rowDelete;
    }


    @Override
    public int getNoOfRecords() {
        return noOfRecords;
    }

    @Override
    public List<Product> searchByName(String name) throws SQLException, ClassNotFoundException {
        List<Product> listProduct = selectAllProduct();
        List<Product> listSearch = new ArrayList<>();

        for (Product product : listProduct) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                listSearch.add(product);
            }
        }
        return listSearch;
    }

    @Override
    public List<Product> getNumberPage(int offset, int noOfRecords, String searchproduct) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        System.out.println("numberpage");

        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM Products where name LIKE ? limit " + offset + ", " + noOfRecords;
        List<Product> listSearchProduct = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, '%' + searchproduct + '%');
        System.out.println(this.getClass() + " getNumberPage() query: " + ps);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getInt("price"));
            product.setQuantity(rs.getInt("quantity"));
            product.setColor(rs.getString("color"));
            product.setDescription(rs.getString("description"));
            product.setCategory(rs.getInt("category"));
            listSearchProduct.add(product);

        }
        rs = ps.executeQuery("SELECT FOUND_ROWS()");
        if (rs.next()) {
            this.noOfRecords = rs.getInt(1);
        }
        connection.close();
        return listSearchProduct;
    }

    @Override
    public boolean checkDuplicateNameProduct(String name) throws SQLException, ClassNotFoundException {
        List<Product> listProduct = selectAllProduct();
        for (Product product : listProduct) {
            if (product.getName().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkDuplicateById(int id) throws SQLException, ClassNotFoundException {
        List<Product> listProduct = selectAllProduct();
        for (Product product : listProduct) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void printSQLException(SQLException exception) {
        for (Throwable e : exception) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = exception.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
