package lk.ijse.restaurantmanagement.repository;

import lk.ijse.restaurantmanagement.db.DbConnection;
import lk.ijse.restaurantmanagement.model.Salary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryRepo {
    public static List<Salary> getAll() throws SQLException {
        String sql = "SELECT * FROM Salary";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Salary> salaryList = new ArrayList<>();
        while (resultSet.next()) {
            String salaryId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            double amount = resultSet.getDouble(3);
            String date = resultSet.getString(4);

            Salary salary = new Salary(salaryId, employeeId, amount, date);
            salaryList.add(salary);
        }
        return salaryList;
    }

    public static boolean save(Salary salary) throws SQLException {
        String sql = "INSERT INTO Salary VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, salary.getSalaryId());
        pstm.setObject(2, salary.getEmployeeId());
        pstm.setObject(3, salary.getAmount());
        pstm.setObject(4, salary.getDate());


        return pstm.executeUpdate() > 0;
    }

    public static Salary searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Salary WHERE employeeId = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Salary salary = null;

        if (resultSet.next()) {
            String salaryId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            double amount = resultSet.getDouble(3);
            String date = resultSet.getString(4);


            salary = new Salary(salaryId, employeeId, amount, date);
        }
        return salary;
    }

    public static boolean deleteData(String id) throws SQLException {
        String sql = "DELETE FROM Salary WHERE salaryId=?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Salary salary) throws SQLException {
        String sql = "UPDATE Salary SET employeeId=?, amount=?, date=? WHERE salaryId=?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setObject(4, salary.getSalaryId());
        pstm.setObject(1, salary.getEmployeeId());
        pstm.setObject(2, salary.getAmount());
        pstm.setObject(3, salary.getDate());
        return pstm.executeUpdate() > 0;
    }

    public static List<Salary> findAllSalaryEmployee(String value) throws SQLException {
        String sql = "SELECT * FROM Salary WHERE employeeId=?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, value);
        ResultSet resultSet = pstm.executeQuery();

        List<Salary> salaryList = new ArrayList<>();
        while (resultSet.next()) {
            String salaryId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            double amount = resultSet.getDouble(3);
            String date = resultSet.getString(4);

            Salary salary = new Salary(salaryId, employeeId, amount, date);
            salaryList.add(salary);
        }
        return salaryList;
    }


    public String autoGenarateSalaryId() throws SQLException {
        String sql = "SELECT salaryId from Salary order by salaryId desc limit 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            String salaryId = resultSet.getString("salaryId");
            String numericPart = salaryId.replaceAll("\\D+", "");
            int newSalaryId = Integer.parseInt(numericPart) + 1;
            return String.format("Sal%03d", newSalaryId);
        } else {
            return "Sal001";

        }
    }

}
