package company.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EmployeeNode {
  private String name;
  private List<EmployeeNode> subordinates;
}
