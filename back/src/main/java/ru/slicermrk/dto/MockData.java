package ru.slicermrk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MockData {
    @JsonProperty
    public List<Department> departments;
    @JsonProperty
    public List<Employee> employees;
}
