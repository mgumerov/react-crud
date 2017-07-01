package ru.slicermrk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;
import ru.slicermrk.model.EntityNotFoundException;

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
    public HttpEntity<Object> editEmployee(@RequestBody final Employee employee, @PathVariable final long id) {
        if (employee.id != id) {
            throw new RuntimeException("Employee id mismatch"); //=> 501
        }

        try {
            dataService.editEmployee(id, employee.fullname, employee.depId);
        } catch (EntityNotFoundException e) {
            return new HttpEntity<>(HttpStatus.NOT_FOUND);
        }

        return new HttpEntity<>(HttpStatus.NO_CONTENT);
    }
}
