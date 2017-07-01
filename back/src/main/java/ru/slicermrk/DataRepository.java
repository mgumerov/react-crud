package ru.slicermrk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.slicermrk.dto.Department;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.MockData;
import ru.slicermrk.dto.Page;
import ru.slicermrk.model.EntityNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataRepository {
    private final Map<Long,Integer> employeesMap;
    private final List<Employee> employees; //we want List, so making "employees" LinkedHashSet would not be enough

    private final Map<Long,Integer> departmentsMap;
    private final List<Department> departments;

    private final ReentrantLock employeesLock = new ReentrantLock();
    private final ReentrantLock departmentsLock = new ReentrantLock();

    public DataRepository(@Value("classpath:data.json") Resource jsonFile) {
        final ObjectMapper mapper = new ObjectMapper();
        final MockData mockData;
        try {
            mockData = mapper.readValue(jsonFile.getInputStream(), MockData.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize DataService", e);
        }

        employees = mockData.employees.stream().sorted(Comparator.comparing(x -> x.fullname)).collect(Collectors.toList());
        employeesMap = new HashMap<>();
        for(int i=0; i < employees.size(); i++) {
            employeesMap.put(employees.get(i).id, i);
        }

        departments = mockData.departments.stream().sorted(Comparator.comparing(x -> x.name)).collect(Collectors.toList());
        departmentsMap = new HashMap<>();
        for(int i=0; i < departments.size(); i++) {
            departmentsMap.put(departments.get(i).id, i);
        }
    }

    public Page<Employee> getEmployees(int pageIdx, int pageSize) {
        Page<Employee> result = getListPage(employees, pageIdx, pageSize);
        result.page.forEach(x -> {
            int iDep = departmentsMap.get(x.depId);
            x.department = departments.get(iDep).name;
        });
        return result;
    }

    public Page<Department> getDepartments(int pageIdx, int pageSize) {
        return getListPage(departments, pageIdx, pageSize);
    }

    private static <T> Page<T> getListPage(List<T> list, int pageIdx, int pageSize) {
        Page<T> p = new Page<>();
        p.total = list.size();
        if (pageSize * (pageIdx - 1) >= list.size()) {
            p.page = Collections.emptyList();
        } else {
            p.page = new ArrayList<T>(list.subList(
                    pageSize * (pageIdx - 1), Math.min(pageSize * pageIdx, list.size())));
        }
        return p;
    }

    //This is data layer; depending on data engine in the app, this operation can be implemented
    //  as single "update", or as "read-modify-write" (though in that case we would probably
    //  not declare this method, and declare "get" and "save" instead)
    public void editEmployee(long id, String fullname, Long depId) throws EntityNotFoundException {
        Integer idx = employeesMap.get(id);
        if (idx == null) {
            throw new EntityNotFoundException("Cannot find Employee with ID=" + id);
        }

        Employee eold = employees.get(idx);
        Employee enew = new Employee();
        enew.id = eold.id;
        enew.depId = depId;
        enew.fullname = fullname;
        employees.set(idx, enew);
    }

    public void lockEmployees() {
        employeesLock.lock();
    }

    public void unlockEmployees() {
        employeesLock.unlock();
    }

    public void lockDepartments() {
        departmentsLock.lock();
    }

    public void unlockDepartments() {
        departmentsLock.unlock();
    }
}
