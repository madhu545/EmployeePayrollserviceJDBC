import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  EmployeePayrollDBService {
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    public static EmployeePayrollDBService getInstant() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String username = "root";
        String password = "1111";
        Connection connection = DriverManager.getConnection(jdbcURL,username,password);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = this.getConnection()){
            Statement  statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollDataList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet;
            resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = null;
                startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    //prepare Statement
    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateEmployeeData (String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> retrieveEmployeePayrollDataRange(String startDate, String endDate) {
        List<EmployeePayrollData> employeePayrollDataList = null;
        try {
            if (this.employeePayrollDataStatement == null)
                this.prepareStatementForRetrieveEmployeePayrollDateRange();
            employeePayrollDataStatement.setString(1, startDate);
            employeePayrollDataStatement.setString(2, endDate);
            ResultSet resultSet;
            resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.retrieveEmployeePayrollDataRange(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> retrieveEmployeePayrollDataRange(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void prepareStatementForRetrieveEmployeePayrollDateRange() {
        Connection connection = null;
        try {
            connection = this.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "SELECT * FROM employee_payroll WHERE start BETWEEN ? AND ?";
        try {
            assert connection != null;
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "SELECT gender,AVG(salary) FROM employee_payroll GROUP BY gender;";
        Map<String,Double> genderToAvgSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("AVG(salary)");
                genderToAvgSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAvgSalaryMap;
    }

    public Map<String, Double> getSumSalaryByGender() {
        String sql = "SELECT gender,SUM(salary) FROM employee_payroll GROUP BY gender;";
        Map<String,Double> genderToSumSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("SUM(salary)");
                genderToSumSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToSumSalaryMap;
    }

    public Map<String, Double> getMinSalaryByGender() {
        String sql = "SELECT gender,MIN(salary) FROM employee_payroll GROUP BY gender;";
        Map<String,Double> genderToMinSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("MIN(salary)");
                genderToMinSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToMinSalaryMap;
    }

    public Map<String, Double> getMaxSalaryByGender() {
        String sql = "SELECT gender,MAX(salary) FROM employee_payroll GROUP BY gender;";
        Map<String,Double> genderToMaxSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("MAX(salary)");
                genderToMaxSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToMaxSalaryMap;
    }

    public Map<String, Integer> getCountByGender() {
        String sql = "SELECT gender,COUNT(gender) FROM employee_payroll GROUP BY gender;";
        Map<String,Integer> genderCount = new HashMap<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("COUNT(gender)");
                genderCount.put(gender, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderCount;
    }
}
