package company.repository;

import company.model.Employee;
import java.util.Hashtable;

public interface CompanyRepository {
  Hashtable<String, Employee> getEmployees();
}
