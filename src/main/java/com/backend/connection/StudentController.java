package com.backend.connection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class StudentController {
	
	@RequestMapping("/")
	public String home(Model model) {
		return "Home";
	}
	@RequestMapping("/Student")
	public String studentlongin() {
		return "student";
	}
	
}
