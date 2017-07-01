package ru.slicermrk.test;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import ru.slicermrk.DataRepository;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;
import ru.slicermrk.model.EntityNotFoundException;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class DataRepositoryTests {
    //просто чтобы показать что можно и из строки
    final static String theOnlyEmployee = "{ \"departments\": [ {\"id\": 1, \"name\": \"D1\"}, {\"id\": 2, \"name\": \"D2\"} ], " +
            "\"employees\": [ {\"id\": 1, \"fullname\": \"ФИО-1\", \"depId\": 1}, " +
                               "{\"id\": 2, \"fullname\": \"ФИО-2\", \"depId\": 1} ]}";
    final static Charset UTF8 = Charset.forName("UTF-8");

    final DataRepository dataRepository = new DataRepository(new ByteArrayResource(theOnlyEmployee.getBytes(UTF8)));

    @Test
    public void shouldFindOneEmployeeOnSecondPage() {
        final Page<Employee> page = dataRepository.getEmployees(2,1);
        assertEquals(2, page.total);
        assertEquals(1, page.page.size());
        assertEquals(2, page.page.iterator().next().id.longValue());
    }

    @Test
    public void shouldFillDepartmentName() {
        final Page<Employee> page = dataRepository.getEmployees(1,1);
        assertEquals("D1", page.page.iterator().next().department);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowCheckedOnNotFound() throws EntityNotFoundException {
        dataRepository.editEmployee(3, "fn", 1L);
    }

    @Test
    public void shouldApplyOnEmployeeEdit() throws EntityNotFoundException {
        dataRepository.editEmployee(1, "fn", 2L);
        assertEquals("fn", dataRepository.getEmployees(1, 1).page.iterator().next().fullname);
        assertEquals(Long.valueOf(2), dataRepository.getEmployees(1, 1).page.iterator().next().depId);
        assertEquals("D2", dataRepository.getEmployees(1, 1).page.iterator().next().department);
    }
}
