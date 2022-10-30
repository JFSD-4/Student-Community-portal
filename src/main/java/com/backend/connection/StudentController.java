package com.backend.connection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Controller
public class StudentController {

	@RequestMapping("/validate")
	public void validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		List<Integer> flag = new ArrayList<Integer>();
		flag.add(0);
		String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
		MongoClient client = MongoClients.create(url);
		MongoDatabase database = client.getDatabase("Members");
		FindIterable<Document> students = database.getCollection("Student").find();
		students.forEach((student) -> {
			if (email.equals(student.get("Uname")) && pass.equals(student.get("Pass"))) {
				flag.set(0, 1);
				Cookie user = new Cookie("User", student.get("Uname").toString());
				Cookie sec = new Cookie("sec", student.get("Sec").toString());
				response.addCookie(user);
				response.addCookie(sec);
			}
		});
		if (flag.get(0) == 1) {
			response.sendRedirect("./Student");
		} else {
			response.sendRedirect("./Signin");
		}
	}

	@RequestMapping("/Tvalidate")
	public void tvalidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		List<Integer> flag = new ArrayList<Integer>();
		flag.add(0);
		String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
		MongoClient client = MongoClients.create(url);
		MongoDatabase database = client.getDatabase("Members");
		FindIterable<Document> teachers = database.getCollection("Teacher").find();
		teachers.forEach((teacher) -> {
			if (email.equals(teacher.get("Name")) && pass.equals(teacher.get("Pass"))) {
				flag.set(0, 1);
				Cookie user = new Cookie("User", teacher.get("Name").toString());
				response.addCookie(user);
			}
		});
		if (flag.get(0) == 1) {
			response.sendRedirect("./Teacher");
		} else {
			response.sendRedirect("./TSignin");
		}
	}

	@RequestMapping("/Student")
	public String home(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			response.sendRedirect("./SSignin");
		} else {
			String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
			MongoClient client = MongoClients.create(url);
			Set<String> list = new HashSet<String>();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("User")) {
					MongoDatabase database = client.getDatabase("Section");
					FindIterable<Document> name = database.getCollection("S2").find();
					name.forEach((stu) -> {
						if (stu.get("Rollno").toString().equals(cookie.getValue())) {
							model.addAttribute("name", stu.get("Name"));
						}
					});

				}
				if (cookie.getName().equals("sec")) {
					MongoDatabase database = client.getDatabase("Subject");
					database.listCollectionNames().forEach((sub) -> {
						FindIterable<Document> name = database.getCollection(sub).find();
						name.forEach((stu) -> {
							if (stu.get("Sec").toString().equals(cookie.getValue())) {
								list.add(sub);
							}
						});
					});
					model.addAttribute("subjects", list);
				}

			}

		}
		return "Subjects";

	}

	@RequestMapping("/Student/{id}")
	public String getassigments(@PathVariable("id") String subject, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			response.sendRedirect("../Signin");
		} else {
			String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
			MongoClient client = MongoClients.create(url);
			Set<String> list = new HashSet<String>();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("User")) {
					MongoDatabase database = client.getDatabase("Section");
					FindIterable<Document> name = database.getCollection("S2").find();
					name.forEach((stu) -> {
						if (stu.get("Rollno").toString().equals(cookie.getValue())) {
							model.addAttribute("name", stu.get("Name"));
							model.addAttribute("id", cookie.getValue());
						}
					});

				}
				if (cookie.getName().equals("sec")) {
					MongoDatabase database = client.getDatabase("Subject");
					database.listCollectionNames().forEach((sub) -> {
						FindIterable<Document> name = database.getCollection(sub).find();
						name.forEach((stu) -> {
							if (stu.get("Sec").toString().equals(cookie.getValue())) {
								list.add(sub);
							}
						});
					});
					model.addAttribute("subjects", list);
				}

			}

		}
		List<String> list = new ArrayList<String>();
		String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
		MongoClient client = MongoClients.create(url);
		client.getDatabase("Assigments").getCollection("S2").find().forEach((val) -> {
			if (val.get("Type").toString().equals("Upload") && val.get("Sub").equals(subject)) {
				list.add(val.get("Que").toString());
			}
		});
		model.addAttribute("assigment", list);
		return "Subjects";
	}

	@RequestMapping("/Teacher")
	public String teacherhome(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		if (cookies == null) {
			response.sendRedirect("TSignin");
		} else {
			String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
			MongoClient client = MongoClients.create(url);
			Set<String> list = new HashSet<String>();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("User")) {
					MongoDatabase database = client.getDatabase("Subject");
					database.listCollectionNames().forEach((sub) -> {
						System.out.println(sub);
						FindIterable<Document> name = database.getCollection(sub).find();
						name.forEach((stu) -> {
							if (stu.get("Name").toString().equals(cookie.getValue())) {
								list.add(sub);
							}
						});
					});
				}
			}
			System.out.println(list);
			model.addAttribute("name", cookies[0].getValue());
			model.addAttribute("subjects", list);
		}
		return "Teacher";
	}

	@RequestMapping("/Teacher/{id}")
	public String teacherassigment(@PathVariable("id") String subject, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		if (cookies == null) {
			response.sendRedirect("TSignin");
		} else {
			String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
			MongoClient client = MongoClients.create(url);
			Set<String> list = new HashSet<String>();
			List<String> subjects = new ArrayList<String>();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("User")) {
					MongoDatabase database = client.getDatabase("Subject");
					database.listCollectionNames().forEach((sub) -> {
						System.out.println(sub);
						FindIterable<Document> name = database.getCollection(sub).find();
						name.forEach((stu) -> {
							if (stu.get("Name").toString().equals(cookie.getValue())) {
								list.add(sub);
								subjects.add(stu.getString("Sec"));
							}
						});
					});
				}
			}
			System.out.println(list);
			List<String[]> assig = new ArrayList<String[]>();
			MongoDatabase database = client.getDatabase("Assigments");
			for (String sec : subjects) {
				System.out.println(sec);
				FindIterable<Document> name = database.getCollection(sec).find();
				name.forEach((e) -> {
					if (subject.equals(e.getString("Sub"))) {
						String names[] = { sec, e.getString("Que") };
						model.addAttribute("open",e.getBoolean("Open"));
						assig.add(names);
					}
				});
			}
			model.addAttribute("sub", subject);
			model.addAttribute("assigment", assig);
			model.addAttribute("name", cookies[0].getValue());
			model.addAttribute("subjects", list);
		}
		return "Teacher";
	}
	
	@RequestMapping("/Teacher/{subject}/edit")
	public void editassigment(@PathVariable("subject") String subject,@RequestParam("sec") String section,@RequestParam("que") String que,@RequestParam("nque") String nque,HttpServletResponse response) throws IOException {
		System.out.println(subject+"  "+section+"  "+que);
		String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
		MongoClient client = MongoClients.create(url);
		MongoDatabase database = client.getDatabase("Assigments");
		MongoCollection<Document> collection = database.getCollection(section);
		Document query = new Document();
        query.append("Sub",subject).append("Que", que);
        Document setData = new Document();
        setData.append("Que", nque);
        Document update = new Document();
        update.append("$set", setData);
        //To update single Document  
        collection.updateOne(query, update);
		response.sendRedirect("/Teacher/"+subject);
	}
	
	@RequestMapping("/Teacher/{subject}/close")
	public void deleteassigment(@PathVariable("subject") String subject,@RequestParam("sec") String section,@RequestParam("que") String que,HttpServletResponse response) throws IOException {
		System.out.println(subject+"  "+section+"  "+que);
		String url = "mongodb+srv://Manaswi:2010030325@cluster0.tw7qk.mongodb.net/?retryWrites=true&w=majority";
		MongoClient client = MongoClients.create(url);
		MongoDatabase database = client.getDatabase("Assigments");
		MongoCollection<Document> collection = database.getCollection(section);
		Document query = new Document();
        query.append("Sub",subject).append("Que", que);
        Document setData = new Document();
        setData.append("Open", false);
        Document update = new Document();
        update.append("$set", setData);
        //To update single Document  
        collection.updateOne(query, update);
		response.sendRedirect("/Teacher/"+subject);
	}
	
	@RequestMapping("/Signin")
	public String studentlongin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return "Signin";
	}

	@RequestMapping("/TSignin")
	public String teacherlongin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return "TSignin";
	}

	@RequestMapping("/Signup")
	public String signup() {
		return "Signup";
	}

}
