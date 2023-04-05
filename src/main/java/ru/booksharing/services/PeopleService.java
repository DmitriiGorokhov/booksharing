package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;
import ru.booksharing.models.images.PersonImage;
import ru.booksharing.repositories.PeopleRepository;
import ru.booksharing.security.PersonDetails;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<Person> findAll() {
        return new TreeSet<>(peopleRepository.findAll());
    }

    public Set<Person> findAllByRoleNotContains(String role) {
        return peopleRepository.findAllByRoleNotContains(role);
    }

    public Optional<Person> findOne(long id) {
        return peopleRepository.findById(id);
    }

    public Optional<Person> findOne(String email) {
        return peopleRepository.findByEmail(email);
    }

    @Transactional
    public Person save(Person person) {
        return peopleRepository.save(person);
    }

    @Transactional
    public Person save(Person person, PersonImage personImage) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setImage(personImage);
        person.setCreatedAt(LocalDateTime.now());
        return peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, Cart cart) {
        updatedPerson.setCart(cart);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public Person changeRole(Person person, String targetRole) {
        Optional<Person> personOpt = peopleRepository.findById(person.getId());
        if (personOpt.isPresent()) {
            Person updatedPerson = personOpt.get();
            updatedPerson.setRole(targetRole);
            return peopleRepository.save(updatedPerson);
        }
        return person;
    }

    public Map<Boolean, Object> checkForAuthentication() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isAuthenticated = principal instanceof PersonDetails;
        return Collections.singletonMap(isAuthenticated, principal);
    }

    public String getPathToImage(Person person) {
        try {
            String pathToURL = Paths.get(person.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}