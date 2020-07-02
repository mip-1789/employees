package company.service;

import company.model.Employee;
import company.model.EmployeeNode;
import company.repository.CompanyRepository;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CompanyService {

  private CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public List<EmployeeNode> getHierarchy() {
    List<EmployeeNode> result = new ArrayList<>();

    Hashtable<String, Employee> employees = companyRepository.getEmployees();

    Hashtable<String, EmployeeNode> employeeNodeTable = new Hashtable<>();

    employees.forEach(
        (id, employee) -> {
          EmployeeNode employeeNode = employeeNodeTable.get(id);

          if (employeeNode == null) {
            employeeNode = new EmployeeNode(employee.getName(), new ArrayList<>());
            employeeNodeTable.put(id, employeeNode);
          }

          if (employee.getManagerId() != null) {
            EmployeeNode managerNode = employeeNodeTable.get(employee.getManagerId());

            if (managerNode == null) {
              Employee manager = employees.get(employee.getManagerId());
              if (manager == null) {
                result.add(employeeNode);
                return;
              }
              managerNode = new EmployeeNode(manager.getName(), new ArrayList<>());
              employeeNodeTable.put(manager.getId(), managerNode);
            }

            managerNode.getSubordinates().add(employeeNode);
          } else {
            result.add(employeeNode);
          }
        });

    return result;
  }

  public void printHierarchy() {
    List<EmployeeNode> employeeNodes = getHierarchy();

    employeeNodes.forEach(
        employeeNode -> {
          printNode(employeeNode, 0);
        });
  }

  private void printNode(EmployeeNode employeeNode, int level) {
    String padding = StringUtils.repeat(' ', level * 5);

    System.out.println(String.format("%s%s", padding, employeeNode.getName()));

    employeeNode
        .getSubordinates()
        .forEach(
            subordinateNode -> {
              printNode(subordinateNode, level + 1);
            });
  }
}
