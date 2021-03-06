package net.itaem.user.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itaem.autogeneratecode.privilege.GeneratePrivilege;
import net.itaem.base.controller.BaseController;
import net.itaem.user.entity.User;
import net.itaem.user.service.IUserService;
import net.itaem.util.JsonUtil;
import net.itaem.util.MD5Util;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录用户Controller
 * @author luohong
 * @date 2015-01-28
 * @email 846705189@qq.com
 * */
@Controller
public class UserLoginController extends BaseController {

	@Autowired
	private IUserService userService;

	/**
	 * 登录
	 * @param loginName
	 * @param password
	 * @param randomCode
	 * @param req
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 * */
	 @GeneratePrivilege(name="登录",type="用户管理", uri="/user/login.do", desc="无")
		
	@RequestMapping("/user/login.do")
	public void login(User user, String randomCode, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{

		if(StringUtils.isEmpty(user.getLoginName())){
			req.setAttribute("error", "用户名不能为空");
			resp.sendRedirect(getBaseURL(req));
			return;
		}

		if(StringUtils.isEmpty(user.getPassword())){
			req.setAttribute("error", "密码不能为空");
			resp.sendRedirect(getBaseURL(req));
			return;
		}

		String rand = (String) req.getSession().getAttribute("rand");

		if(StringUtils.isEmpty(rand)){
			req.setAttribute("error", "验证码不能为空");
			resp.sendRedirect(getBaseURL(req));
			return;
		}

		if(StringUtils.isEmpty(randomCode)){
			req.setAttribute("error", "验证码不能为空");
			
			resp.sendRedirect(getBaseURL(req));
			return;
		}

		if(!rand.equalsIgnoreCase(randomCode)){
			req.setAttribute("error", "验证码不正确");
			
			resp.sendRedirect(getBaseURL(req));
			return;
		}

		user.setPassword(MD5Util.strToMD5(user.getPassword()));
		User u = userService.exists(user); 

		if(u != null && u.getId() != null && !"".equals(u.getId())){
			/**
			 * 设置自动登录
			 * */
			String chkRememberMe = req.getParameter("chkRememberMe");
			if(chkRememberMe != null && chkRememberMe.equals("on")){

				//将用户名和密码存放在cookie中
				Cookie cookie = new Cookie("user", 
						user.getLoginName()+"=="+ user.getPassword());  

				//设置cookie的有效范围，这里设置为根目录
				cookie.setPath("/");

				//一周内有效
				int seconds = 7 * 24 * 60 * 60;  
				cookie.setMaxAge(seconds);                  

				//设置cookie
				resp.addCookie(cookie);  
			}


		}

		//注册用户到session中
		if(u != null && u.getId() != null && !"".equals(u.getId())){
			//将用户保存在session中
			req.getSession().setAttribute("user", u);
			
			req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
			return;
		}else{
			req.setAttribute("error", "用户不存在");			
			resp.sendRedirect(getBaseURL(req));
			return;
		}
	}

	/**
	 * 移动端登录
	 * @throws IOException 
	 * */
	 
	@RequestMapping("/user/loginByAndroid.do")
	public void loginByAndroid(User user, HttpServletResponse resp) throws IOException{

		if(StringUtils.isEmpty(user.getLoginName())){
			println(resp, JsonUtil.createJson("error", "用户名不能为空"));
			return;
		}

		if(StringUtils.isEmpty(user.getPassword())){
			println(resp, JsonUtil.createJson("error", "密码不能为空"));
			return;
		}

		user.setPassword(MD5Util.strToMD5(user.getPassword()));
		User u = userService.exists(user);
		if(u != null){
			JSONObject json = new JSONObject();
			json.put("status", "success");
			json.put("id", u.getId());
			json.put("name", "name");
			json.put("loginName", u.getLoginName());
			System.out.println(json.toString());
			println(resp, json.toString());
		}else{
			println(resp, JsonUtil.createJson("error", "用户不存在"));
		}
	}

	/**
	 * 退出登录，跳转到登录界面
	 * 清除掉cookie
	 * 
	 * @param req 
	 * @param resp
	 * */
	
	@GeneratePrivilege(name="退出登录，跳转到登录界面",type="用户管理", uri="/user/logout.do", desc="无")
	
	@RequestMapping("/user/logout.do")
	public void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getSession().removeAttribute("user");
		clearCookie(req, resp);
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}

	/**
	 * 清除网站cookie
	 * 
	 * */
	private void clearCookie(HttpServletRequest req, HttpServletResponse resp){
		Cookie[] cookies = req.getCookies();  
		if (cookies != null && cookies.length > 0) {  
			for (Cookie cookie : cookies) {  
				// 设置生存期为0  
				cookie.setMaxAge(-1);  
				cookie.setPath("/");
				// 设回Response中生效  
				resp.addCookie(cookie);  
			}
		}  
	}
}

