package service;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {

    public ProductDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/demo20062?useSSL=false", "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                     ("select  * from product")) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                list.add(new Product(id, name, price, quantity));
            }
        } catch (SQLException e) {
            System.out.println("");
        }
        return list;
    }

    @Override
    public void add(Product product) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                     ("insert into product(id,name, price, quantity)  values (?,?,?,?) ");) {
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error");
        }
    }

    @Override
    public Product findById(int id) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                     ("select * from product where id =?")) {
            System.out.println(preparedStatement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = Double.parseDouble(resultSet.getString("price"));
                int quantity = Integer.parseInt(resultSet.getString("quantity"));
                product = new Product(id, name, price, quantity);
            }
        } catch (SQLException ignored) {
        }
        return product;
    }


    @Override
    public boolean delete(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from product where id = ?")) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                     ("update product set name = ?,price =?, quantity=? where id = ?;")) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
