package ru.slicermrk.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import ru.slicermrk.DataService;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;
import ru.slicermrk.model.EntityNotFoundException;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class DataServiceTests {
    //просто чтобы показать что можно и из строки
    final static String theOnlyEmployee = "{ \"departments\": [ {\"id\": 1, \"name\": \"D1\"}, {\"id\": 2, \"name\": \"D2\"} ], " +
            "\"employees\": [ {\"id\": 1, \"fullname\": \"ФИО-1\", \"depId\": 1}, " +
                               "{\"id\": 2, \"fullname\": \"ФИО-2\", \"depId\": 1} ]}";
    final static Charset UTF8 = Charset.forName("UTF-8");

    final DataService dataService = new DataService(new ByteArrayResource(theOnlyEmployee.getBytes(UTF8)));

    @Test
    public void shouldFindOneEmployeeOnSecondPage() {
        final Page<Employee> page = dataService.getEmployees(2,1);
        assertEquals(2, page.total);
        assertEquals(1, page.page.size());
        assertEquals(2, page.page.iterator().next().id.longValue());
    }

    @Test
    public void shouldFillDepartmentName() {
        final Page<Employee> page = dataService.getEmployees(1,1);
        assertEquals("D1", page.page.iterator().next().department);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowCheckedOnNotFound() throws EntityNotFoundException {
        dataService.editEmployee(3, "fn", 1L);
    }

    @Test
    public void shouldApplyOnEmployeeEdit() throws EntityNotFoundException {
        dataService.editEmployee(1, "fn", 2L);
        assertEquals("fn", dataService.getEmployees(1, 1).page.iterator().next().fullname);
        assertEquals(Long.valueOf(2), dataService.getEmployees(1, 1).page.iterator().next().depId);
        assertEquals("D2", dataService.getEmployees(1, 1).page.iterator().next().department);
    }
}
