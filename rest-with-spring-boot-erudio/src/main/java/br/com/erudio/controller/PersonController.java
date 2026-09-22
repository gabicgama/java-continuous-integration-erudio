package br.com.erudio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.model.Person;
import br.com.erudio.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	private final PersonService service;

	public PersonController(PersonService service) {
		this.service = service;
	}

	@GetMapping()
	public List<Person> findAll() {
		return service.findAll();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Person> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(service.findById(id));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping()
	public Person create(@RequestBody Person person) {
		return service.create(person);
	}

	@PutMapping()
	public ResponseEntity<Person> update(@RequestBody Person person) {
		try {
			return ResponseEntity.ok(service.update(person));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
