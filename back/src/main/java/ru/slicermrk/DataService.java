package ru.slicermrk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.MockData;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataService {
    private final Map<Long,Employee> employees;

    public DataService(@Value("classpath:data.json") Resource jsonFile) {
        final ObjectMapper mapper = new ObjectMapper();
        final MockData mockData;
        try {
            mockData = mapper.readValue(jsonFile.getInputStream(), MockData.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize DataService", e);
        }
        employees = mockData.employees.stream().collect(Collectors.toMap(x -> x.id, Function.identity()));
    }

    public Collection<Employee> getEmployees() {
        return employees.values();
    }
}
