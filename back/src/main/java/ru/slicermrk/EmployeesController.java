package ru.slicermrk;

import org.springframework.beans.factory.annotation.Autowired;
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
}