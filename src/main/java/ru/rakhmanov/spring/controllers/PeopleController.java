package ru.rakhmanov.spring.controllers;

import ru.rakhmanov.spring.dto.PersonDTO;
import ru.rakhmanov.spring.models.Person;
import ru.rakhmanov.spring.security.PersonDetails;
import ru.rakhmanov.spring.services.AdminService;
import ru.rakhmanov.spring.services.BooksService;
import ru.rakhmanov.spring.services.PeopleService;
import ru.rakhmanov.spring.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Controller
@RequestMapping("/people")
public class PeopleController {
    PeopleService peopleService;
    BooksService booksService;
    PersonValidator personValidator;
    AdminService adminService;
    ModelMapper modelMapper;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute(
                "people",
                peopleService.findAll()
                        .stream().map(this::convertToPersonDTO)
                        .toList()
        );
        return "admin/people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        PersonDTO personDTO = convertToPersonDTO(peopleService.findOne(id));

        model.addAttribute("person", personDTO);
        model.addAttribute("currentBooks", personDTO.getBooks());

        return "admin/people/show";
    }

    @PatchMapping("/release")
    public String releaseTheBook(@ModelAttribute("bookId") int bookId,
                                 @ModelAttribute("personId") int personId) {
        booksService.release(bookId);
        return "redirect:/people/" + personId;
    }


    @PatchMapping("/{id}/lock")
    public String lockUser(@PathVariable("id") int id) {
        adminService.lockUser(id);
        return "redirect:/people/{id}";
    }
    @PatchMapping("/{id}/activate")
    public String activateUser(@PathVariable("id") int id) {
        adminService.activateUser(id);
        return "redirect:/people/{id}";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") PersonDTO personDTO) {
        return "admin/people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
             return "admin/people/new";
        }


        peopleService.save(
                convertToPerson(personDTO)
        );
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute(
                "person",
                convertToPersonDTO(
                        peopleService.findOne(id)
                )
        );
        return "admin/people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) return "admin/people/edit";

        peopleService.update(convertToPerson(personDTO));
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        String whatIsTheRole = peopleService.findOne(id).getRole();
        peopleService.delete(id);

        if (whatIsTheRole.equals("ROLE_ADMIN")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

            int authenticatedId = personDetails.person().getId();
            if (authenticatedId == id)
                return "redirect:/auth/login";
        }
        return "redirect:/people";
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
