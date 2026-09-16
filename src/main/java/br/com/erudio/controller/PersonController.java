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
	public Person findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping()
	public Person create(@RequestBody Person person) {
		return service.create(person);
	}

	@PutMapping()
	public Person update(@RequestBody Person person) {
		return service.update(person);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
