package ru.slicermrk.dto;

//When used as dto it's mutable, when used as model class it's immutable :)
//todo make separate model classes
public class Employee {
    public Long id;
    public String fullname;
    public String department;
    public Long depId;
}
