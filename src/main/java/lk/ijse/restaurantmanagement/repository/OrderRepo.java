package lk.ijse.restaurantmanagement.repository;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import lk.ijse.restaurantmanagement.db.DbConnection;
import lk.ijse.restaurantmanagement.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRepo {
    public static String currentId() throws SQLException {
        String sql = "SELECT orderId FROM orders ORDER BY orderId desc LIMIT 1";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?, ?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, order.getOrderId());
        pstm.setString(2, order.getOrderType());
        pstm.setString(3, order.getCusId());
        pstm.setString(4, order.getDate());
        pstm.setString(5, String.valueOf(order.getTotal()));
        return pstm.executeUpdate() > 0;
    }


    public static Order searchById(String orderId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE orderId = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, orderId);
        ResultSet resultSet = pstm.executeQuery();
        Order orders = null;

        if (resultSet.next()) {
            String orderid = resultSet.getString(1);
            String orderType = resultSet.getString(2);
            String cusId = resultSet.getString(3);
            String date = resultSet.getString(4);
            double total = Double.parseDouble(resultSet.getString(5));

            orders = new Order(orderid, orderType, cusId, date, total);
        }
        return orders;

    }

    public String autoGenerateOrderId() throws SQLException {
        String sql = "SELECT orderId from Orders order by orderId desc limit 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            String orderId = resultSet.getString("orderId");
            String numericPart = orderId.replaceAll("\\D+", "");
            int newOrderId = Integer.parseInt(numericPart) + 1;
            return String.format("Od%03d", newOrderId);
        } else {
            return "Od001";
        }
    }

    public static void ordersCount(BarChart<String,Number> barChartOrders) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT date AS order_date, COUNT(*) AS order_count FROM Orders GROUP BY date ORDER BY order_date";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        while (resultSet.next()) {
            String date = resultSet.getString("order_date");
            int ordersCount = resultSet.getInt("order_count");
            series.getData().add(new XYChart.Data<>(date, ordersCount));
        }
        barChartOrders.getData().add(series);
        }
    }

