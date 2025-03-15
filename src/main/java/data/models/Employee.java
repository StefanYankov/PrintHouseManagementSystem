package data.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an employee in the print house.
 */
public class Employee implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String egn;
    private EmployeeType employeeType;

    public Employee() {
    }

    public Employee(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Employee(String egn, EmployeeType employeeType) {
        this.egn = egn;
        this.employeeType = employeeType;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return egn.equals(employee.egn);
    }

    @Override
    public int hashCode() {
        return egn.hashCode();
    }

    @Override
    public String toString() {
        return "Employee{egn=" + egn + ", type=" + employeeType + "}";
    }

}