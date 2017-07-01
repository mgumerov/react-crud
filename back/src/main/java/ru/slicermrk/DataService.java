package ru.slicermrk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.slicermrk.dto.Department;
import ru.slicermrk.dto.Employee;
import ru.slicermrk.dto.Page;
import ru.slicermrk.model.EntityNotFoundException;

@Component
public class DataService {
    @Autowired
    private DataRepository dataRepository;

    //Since very simple data layer is used, we employ exclusive locks instead of mature transactions
    public Page<Employee> getEmployees(int pageIdx, int pageSize) {
        dataRepository.lockEmployees();
        try {
            return dataRepository.getEmployees(pageIdx, pageSize);
        } finally {
            dataRepository.unlockEmployees();
        }
    }

    public Page<Department> getDepartments(int pageIdx, int pageSize) {
        dataRepository.lockDepartments();
        try {
            return dataRepository.getDepartments(pageIdx, pageSize);
        } finally {
            dataRepository.unlockDepartments();
        }
    }

    public void editEmployee(long id, String fullname, Long depId) throws EntityNotFoundException {
        dataRepository.lockEmployees();
        try {
            dataRepository.editEmployee(id, fullname, depId);
        } finally {
            dataRepository.unlockEmployees();
        }
    }
}
