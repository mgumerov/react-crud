package ru.slicermrk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private DataService dataService;

    @RequestMapping(method = RequestMethod.GET, path = "")
    public Page<Employee> getEmployees(@RequestParam final int page, @RequestParam final int size) {
        return dataService.getEmployees(page, size);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    //todo optimistic locks
    public void editEmployee(@RequestBody final Employee employee, @PathVariable final long id) {
        if (employee.id.longValue() != id) {
            throw new RuntimeException("Employee id mismatch");
        }
        dataService.editEmployee(id, employee.fullname, employee.depId);
    }
}
