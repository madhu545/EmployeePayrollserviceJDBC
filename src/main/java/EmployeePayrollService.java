import java.util.List;
import java.util.Map;

public class EmployeePayrollService {

    public enum IOService {DB_IO}

    private List<EmployeePayrollData> employeePayrollDataList;
    private EmployeePayrollDBService employeePayrollDBService;

    public Map<String, Double> readSumSalaryByGender() {
        return employeePayrollDBService.getSumSalaryByGender();
    }

    public Map<String, Double> readMinSalaryByGender() {
        return employeePayrollDBService.getMinSalaryByGender();
    }

    public Map<String, Double> readMaxSalaryByGender() {
        return employeePayrollDBService.getMaxSalaryByGender();
    }

    public Map<String, Integer> getCountByGender() {
        return employeePayrollDBService.getCountByGender();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollDataList) {
        this();
        this.employeePayrollDataList = employeePayrollDataList;
    }

    EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstant();
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        this.employeePayrollDataList = employeePayrollDBService.readData();
        return employeePayrollDataList;
    }

    //sync with database
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollDataList.stream()
                .filter(employeePayrollData -> employeePayrollData.name.equals(name))
                .findFirst()
                .orElse(null);
    }
    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    public List<EmployeePayrollData> retrieveEmployyesForGivenDataRange(String startDate, String endDate) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.retrieveEmployeePayrollDataRange(startDate, endDate);
        return employeePayrollDataList;
    }
    public Map<String, Double> readAverageSalaryByGender() {
        return employeePayrollDBService.getAverageSalaryByGender();
    }
}
