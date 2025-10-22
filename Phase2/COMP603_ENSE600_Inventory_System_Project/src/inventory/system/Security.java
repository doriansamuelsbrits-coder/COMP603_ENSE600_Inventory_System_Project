package inventory.system;

import java.util.Map;

public class Security {

    private String username;
    private String password;
    private String employeeName;
    private String statusPriority;
    //positions used to verify valid employee status
    private static final String[] positions = {"Employee", "Manager", "CEO", "Director", "Admin"};

    //This COnstructor will be called if a admin is setting that employees positition manually.
    public Security(String employeename, String userName, String passWord, String sp) {
        this.employeeName = employeename;
        this.username = userName;
        this.password = passWord;
        try {
            boolean valid = false;
            for (String p : positions) {
                if (sp.equals(p)) {
                    this.statusPriority = sp;
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Provided status is not in scope of manageable roles! Defaulting to 'Employee'.");
                this.statusPriority = "Employee";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //default constructor where employee has default permissions of "Employee"
    public Security(String employeename, String userName, String passWord) {
        this.employeeName = employeename;
        this.username = userName;
        this.password = passWord;
        this.statusPriority = "Employee";
    }
    
    //Function For Higher Employers to promote employees access, a Future Development.
    public static void PromoteEmployee(String employeeName, String newPosition, Map<String, Security> sec) {
        try {

            Security emp = sec.get(employeeName);
            if (emp == null) {
                System.out.println("Employee not found: " + employeeName);
                return;
            }
            boolean valid = false;
            for (String p : positions) {
                if (p.equalsIgnoreCase(newPosition)) {
                    emp.statusPriority = p;
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Provided status is not in scope of manageable roles! Defaulting to 'Employee'.");
                emp.statusPriority = "Employee";
            }
            sec.put(employeeName, emp);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Get Methods for simpler access
    public String getEmployeeName() {
        return employeeName;
    }
    public String getUserName() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPosition() {
        return statusPriority;
    }
}
