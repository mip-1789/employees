package company.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import company.model.Employee;
import company.model.EmployeeNode;
import company.repository.CompanyRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

  private CompanyRepository companyRepository;
  private CompanyService companyService;

  @Before
  public void setup() {
    companyRepository = mock(CompanyRepository.class);
    companyService = new CompanyService(companyRepository);
  }

  @Test
  public void oneLevelHierarchyAndOneSubordinate() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("100", new Employee("100", "Alan", null));
    mockEmployees.put("220", new Employee("220", "Martin", "100"));
    mockEmployees.put("275", new Employee("275", "Alex", "150"));
    mockEmployees.put("150", new Employee("150", "Jamie", null));

    when(companyRepository.getEmployees())
        .thenReturn(mockEmployees);

    List<EmployeeNode> result = companyService.getHierarchy();

    Assert.assertEquals(2, result.size());
    result.forEach(employeeNode -> {
      if (employeeNode.getName().equals("Alan")) {
        Assert.assertEquals(1, employeeNode.getSubordinates().size());
        Assert.assertEquals("Martin", employeeNode.getSubordinates().get(0).getName());
      } else if (employeeNode.getName().equals("Jamie"))  {
        Assert.assertEquals(1, employeeNode.getSubordinates().size());
        Assert.assertEquals("Alex", employeeNode.getSubordinates().get(0).getName());
      }
    });
  }

  @Test
  public void oneLevelHierarchyAndTwoSubordinates() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("100", new Employee("100", "Alan", null));
    mockEmployees.put("220", new Employee("220", "Martin", "100"));
    mockEmployees.put("275", new Employee("275", "Alex", "150"));
    mockEmployees.put("150", new Employee("150", "Jamie", null));
    mockEmployees.put("400", new Employee("400", "Steve", "150"));
    mockEmployees.put("190", new Employee("190", "David", "100"));

    when(companyRepository.getEmployees())
            .thenReturn(mockEmployees);

    List<EmployeeNode> result = companyService.getHierarchy();

    Assert.assertEquals(2, result.size());
    result.forEach(employeeNode -> {
      if (employeeNode.getName().equals("Alan")) {
        Assert.assertEquals(2, employeeNode.getSubordinates().size());
        Assert.assertEquals("Martin", employeeNode.getSubordinates().get(0).getName());
        Assert.assertEquals("David", employeeNode.getSubordinates().get(1).getName());
      } else if (employeeNode.getName().equals("Jamie"))  {
        Assert.assertEquals(2, employeeNode.getSubordinates().size());
        Assert.assertEquals("Alex", employeeNode.getSubordinates().get(0).getName());
        Assert.assertEquals("Steve", employeeNode.getSubordinates().get(1).getName());
      }
    });
  }

  @Test
  public void twoLevelHierarchyAndOneSubordinate() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("400", new Employee("400", "Steve", "275"));
    mockEmployees.put("100", new Employee("100", "Alan", null));
    mockEmployees.put("220", new Employee("220", "Martin", "100"));
    mockEmployees.put("275", new Employee("275", "Alex", "150"));
    mockEmployees.put("150", new Employee("150", "Jamie", null));
    mockEmployees.put("190", new Employee("190", "David", "220"));

    when(companyRepository.getEmployees())
            .thenReturn(mockEmployees);

    List<EmployeeNode> result = companyService.getHierarchy();

    Assert.assertEquals(2, result.size());
    result.forEach(employeeNode -> {
      if (employeeNode.getName().equals("Alan")) {
        Assert.assertEquals(1, employeeNode.getSubordinates().size());
        Assert.assertEquals("Martin", employeeNode.getSubordinates().get(0).getName());
        Assert.assertEquals(1, employeeNode.getSubordinates().get(0).getSubordinates().size());
        Assert.assertEquals("David", employeeNode.getSubordinates().get(0).getSubordinates().get(0).getName());
      } else if (employeeNode.getName().equals("Jamie"))  {
        Assert.assertEquals(1, employeeNode.getSubordinates().size());
        Assert.assertEquals("Alex", employeeNode.getSubordinates().get(0).getName());
        Assert.assertEquals(1, employeeNode.getSubordinates().get(0).getSubordinates().size());
        Assert.assertEquals("Steve", employeeNode.getSubordinates().get(0).getSubordinates().get(0).getName());
      }
    });
  }

  @Test
  public void noManagers() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("100", new Employee("100", "Alan", null));
    mockEmployees.put("220", new Employee("220", "Martin", null));
    mockEmployees.put("150", new Employee("150", "Jamie", null));

    Set<String> employeeNames = new HashSet<>(Arrays.asList("Alan", "Martin", "Jamie"));

    when(companyRepository.getEmployees())
        .thenReturn(mockEmployees);

    List<EmployeeNode> result = companyService.getHierarchy();

    Assert.assertEquals(3, result.size());
    result.forEach(employeeNode -> {
      Assert.assertEquals(0, employeeNode.getSubordinates().size());
      employeeNames.remove(employeeNode.getName());
    });

    Assert.assertEquals(0, employeeNames.size());
  }

  @Test
  public void invalidManager() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("100", new Employee("100", "Alan", null));
    mockEmployees.put("220", new Employee("220", "Martin", null));
    mockEmployees.put("150", new Employee("150", "Jamie", "221"));

    Set<String> employeeNames = new HashSet<>(Arrays.asList("Alan", "Martin", "Jamie"));

    when(companyRepository.getEmployees())
        .thenReturn(mockEmployees);

    List<EmployeeNode> result = companyService.getHierarchy();

    Assert.assertEquals(3, result.size());
    result.forEach(employeeNode -> {
      Assert.assertEquals(0, employeeNode.getSubordinates().size());
      employeeNames.remove(employeeNode.getName());
    });

    Assert.assertEquals(0, employeeNames.size());
  }

  @Test
  public void printHierarchy() {
    Hashtable<String, Employee> mockEmployees = new Hashtable<>();
    mockEmployees.put("100", new Employee("100", "Alan", "150"));
    mockEmployees.put("220", new Employee("220", "Martin", "100"));
    mockEmployees.put("150", new Employee("150", "Jamie", null));
    mockEmployees.put("275", new Employee("275", "Alex", "100"));
    mockEmployees.put("400", new Employee("400", "Steve", "150"));
    mockEmployees.put("190", new Employee("190", "David", "400"));


    when(companyRepository.getEmployees())
        .thenReturn(mockEmployees);

    companyService.printHierarchy();
  }
}
