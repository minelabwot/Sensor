package com.yyn.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.model.User;
import com.yyn.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	@RequestMapping("/userLogin.do")
	public String userLoginVarify(HttpServletRequest request, Model model) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = userService.getUser(username);
		if (password.equals(user.getPassword())) {
			request.getSession().setAttribute("userInfo", user);
			System.out.println(request.getSession().getAttribute("userInfo").toString());
			return "redirect:/index.jsp";
		}
		else
			return "login/wrong.html";
	}
	
	@RequestMapping("/userRegister.do")
	public String userRegister(HttpServletRequest request, Model model) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirm = request.getParameter("repassword");
		String email = request.getParameter("email");

		User user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setEmail(email);
		
		if (password.equals(confirm) && username != null) {
			userService.insertNewUser(user);
			return "login/success.jsp";
		}
		return "login/wrong.html";
	}
	
	@RequestMapping("/userLogout.do")
	public String userLogout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userInfo");
		return "redirect:/views/login/login.html";
	}

	@RequestMapping("/userChangePwd.do")
	public String changePwd(String oldpwd,String newpwd, String confirmpwd, HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("userInfo");
		if(user.getPassword().equals(oldpwd) && newpwd.equals(confirmpwd)) {
			userService.updatePwd(user.getName(), newpwd);
			return "redirect:views/userInteractive/userInformation.jsp";
		}
		return "login/wrong.html";
	}
	
	@RequestMapping("/userUpdateInfo.do") 
	public String updateInfo(String alias,String date,String sex, HttpServletRequest request) throws ParseException {
		User user = (User)request.getSession().getAttribute("userInfo");
		user.setAlias(alias);
		user.setBirthday(date);
		System.out.println(user.getBirthday().toString());
		user.setSex(sex);
		userService.updateUser(user);
		return "redirect:/views/userInteractive/userInformation.jsp";
	}

}
