package ru.slicermrk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.slicermrk.dto.Employee;

import java.util.Collection;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private DataService dataService;

    @RequestMapping(method = RequestMethod.GET, path = "")
    public Collection<Employee> getEmployees() {
        return dataService.getEmployees();
    }
}
