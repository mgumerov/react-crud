package ru.slicermrk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.slicermrk.dto.Department;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.MockData;
import ru.slicermrk.dto.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataService {
    private final Map<Long,Employee> employees;
    private final List<Employee> employeesList; //we want List, so making "employees" LinkedHashSet would not be enough

    private final Map<Long,Department> departments;
    private final List<Department> departmentsList;

    public DataService(@Value("classpath:data.json") Resource jsonFile) {
        final ObjectMapper mapper = new ObjectMapper();
        final MockData mockData;
        try {
            mockData = mapper.readValue(jsonFile.getInputStream(), MockData.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize DataService", e);
        }
        employees = mockData.employees.stream().collect(Collectors.toMap(x -> x.id, Function.identity()));
        employeesList = mockData.employees.stream().sorted(Comparator.comparing(x -> x.fullname)).collect(Collectors.toList());
        departments = mockData.departments.stream().collect(Collectors.toMap(x -> x.id, Function.identity()));
        departmentsList = mockData.departments.stream().sorted(Comparator.comparing(x -> x.name)).collect(Collectors.toList());
    }

    public Page<Employee> getEmployees(int pageIdx, int pageSize) {
        synchronized (employees) {
            return getListPage(employeesList, pageIdx, pageSize);
        }
    }

    public Page<Department> getDepartments(int pageIdx, int pageSize) {
        synchronized (departments) {
            return getListPage(departmentsList, pageIdx, pageSize);
        }
    }

    private static <T> Page<T> getListPage(List<T> list, int pageIdx, int pageSize) {
        Page<T> p = new Page<>();
        p.total = list.size();
        if (pageSize * (pageIdx - 1) >= list.size()) {
            p.page = Collections.emptyList();
        } else {
            p.page = new ArrayList<>(list.subList(
                    pageSize * (pageIdx - 1),
                    Math.min(pageSize * pageIdx, list.size())));
        }
        return p;
    }
}
