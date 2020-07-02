package company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Employee {
  private String id;
  private String name;
  private String managerId;
}
