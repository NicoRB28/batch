package com.examplebatch.batchdemo.model;

import java.math.BigInteger;

public class Person {
    private Integer personId;
    private String firstName;
    private String lastName;

    public Person(Integer id, String firstName, String lastName) {
        this.personId = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person() {
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
