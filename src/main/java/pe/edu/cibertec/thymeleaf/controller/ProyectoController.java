package pe.edu.cibertec.thymeleaf.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.edu.cibertec.web.model.Role;
import pe.edu.cibertec.web.model.User;
import pe.edu.cibertec.web.repository.IPersonRepository;
import pe.edu.cibertec.web.repository.IRoleRepository;
import pe.edu.cibertec.web.repository.IUserRepository;
import pe.edu.cibertec.web.service.UserService;

@Controller
public class ProyectoController {
	
	@Autowired
	private IRoleRepository repos;
	
	@Autowired
	private IPersonRepository reposPerson;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/greeting")
	public String greeting (@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);	
		return "greeting";
	}
	
	@GetMapping("/listar")
	public String listRole(Model model) {
		try {
			model.addAttribute("ltsRole", repos.findAll());
			model.addAttribute("ltsPerson", reposPerson.findAll());
			model.addAttribute("ltsUser", userService.listadoUsu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "listado";
	}
	
	@GetMapping("/login")
	public String loginView(Model model) {
		System.out.println("Mostrando login");
		User Unew = new User();
		Unew.setEmail("correito.com");
		model.addAttribute("userLogin", Unew);
		return "login";
	}
	@PostMapping("/login")
	public String login(@ModelAttribute User user, Model model) {
		System.out.println("Validando login");		
		String redirect = "login";
		User u = userService.validateUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (u != null) {
			u.setLastlogin(new Date());
			System.out.println("Actualizando usuario");
			User updUser = userService.updateUserLogin(u);
			model.addAttribute("userLogin", updUser);
			redirect = "greeting";
		} else {
			model.addAttribute("errors", "Usuario o clave incorrectos");
			model.addAttribute("userLogin", new User());
		}
		
		return redirect;
	}
	
	@GetMapping("/login2")
	public String login2View(Model model) {
		System.out.println("Mostrando login2");
		User Unew = new User();
		model.addAttribute("usrLogin", Unew);
		return "login2";
	}
	@PostMapping("/login2")
	public String login2(@ModelAttribute User user, Model model) {
		System.out.println("Validando login2");		
		String redirect = "login2";
		User u = userService.validateUserByNameAndPassword(user.getName(), user.getPassword());
		if (u != null) {
			u.setLastlogin(new Date());
			System.out.println("Actualizando usuario - Login2");
			User updUser = userService.updateUserLogin(u);
			model.addAttribute("usrLogin", updUser);
			redirect = "greeting";
		} else {
			model.addAttribute("errors", "Usuario o clave incorrectos");
			model.addAttribute("usrLogin", new User());
		}
		return redirect;
	}
	
	@GetMapping("/rolsito")
	public String rolsitoView(Model model) {
		System.out.println("Iniciando creación de rol");
		model.addAttribute("rolOb", new Role());
		return "rolsito";
	}
	@PostMapping("/rolsito")
	public String rolsito(@ModelAttribute Role role, Model model) {
		if (role != null) {
			repos.save(role);
		} else {
			model.addAttribute("errors", "Error");
			model.addAttribute("rolOb", new Role());
		}
		
		return "rolsito";
	}
}
