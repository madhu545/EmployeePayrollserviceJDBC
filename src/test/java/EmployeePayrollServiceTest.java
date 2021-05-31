import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertEquals(3,employeePayrollData.size());
    }

    @Test
    public void givenNewEmployeeSalaryShouldUpdateWithDatabase() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenDataRangeWhenRetrievedShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.retrieveEmployyesForGivenDataRange("2018-01-01", "2019-12-01");
        Assertions.assertEquals(2, employeePayrollData.size());
    }

    @Test
    public void givenPayrollDataWhenAverageSalaryRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> averageSalaryByGender  = employeePayrollService.readAverageSalaryByGender();
        System.out.println(averageSalaryByGender);
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(2000000.00)&&
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollDataWhenSumSalaryRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> sumSalaryByGender  = employeePayrollService.readSumSalaryByGender();
        System.out.println(sumSalaryByGender);
        Assertions.assertTrue(sumSalaryByGender.get("M").equals(4000000.00)&&
                sumSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollDataWhenMinSalaryRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> minSalaryByGender  = employeePayrollService.readMinSalaryByGender();
        System.out.println(minSalaryByGender);
        Assertions.assertTrue(minSalaryByGender.get("M").equals(1000000.00)&&
                minSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollDataWhenMaxSalaryRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> maxSalaryByGender  = employeePayrollService.readMaxSalaryByGender();
        System.out.println(maxSalaryByGender);
        Assertions.assertTrue(maxSalaryByGender.get("M").equals(3000000.00)&&
                maxSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollDataWhenCountRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Integer> countByGender  = employeePayrollService.getCountByGender();
        System.out.println(countByGender);
        Assertions.assertTrue(countByGender.get("M").equals(2)&&
                countByGender.get("F").equals(1));
    }
}