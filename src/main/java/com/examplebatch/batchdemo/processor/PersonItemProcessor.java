package com.examplebatch.batchdemo.processor;

import com.examplebatch.batchdemo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigInteger;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final Integer personId = person.getPersonId();

        final Person transformedPerson = new Person(personId,firstName, lastName);
        LOGGER.info("Converting ({}) into ({})",person, transformedPerson);

        return transformedPerson;
    }
}
