package repository;

import model.Person;

public interface PersonRepository {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    void deleteByFirstNameAndLastName(String firstName, String lastName);
}
