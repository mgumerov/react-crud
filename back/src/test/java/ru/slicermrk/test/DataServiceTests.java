package ru.slicermrk.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.slicermrk.DataRepository;
import ru.slicermrk.DataService;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataServiceTests.TestConfig.class)
public class DataServiceTests {
    @Autowired
    private DataService dataService;

    @Autowired
    private DataRepository dataRepositoryMock;

    @Test
    public void shouldFindOneEmployeeOnSecondPage() {
        Page<Employee> mockResult = new Page<>();
        mockResult.total = 1;
        mockResult.page = Collections.singletonList(new Employee());
        mockResult.page.iterator().next().id = 123L;
        Mockito.when(dataRepositoryMock.getEmployees(1,1)).thenReturn(mockResult);

        final Page<Employee> page = dataService.getEmployees(1,1);
        assertEquals(1, page.total);
        assertEquals(123L, page.page.iterator().next().id.longValue());
    }

    static class TestConfig {
        @Bean
        public DataRepository getDataRepository() {
            return Mockito.mock(DataRepository.class);
        }

        @Bean
        public DataService getDataService() {
            return new DataService();
        }
    }
}
