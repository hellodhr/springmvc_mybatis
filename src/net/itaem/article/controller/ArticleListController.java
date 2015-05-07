package net.itaem.article.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itaem.article.entity.Article;
import net.itaem.article.service.IArticleService;
import net.itaem.base.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 获取全部文章controller
 * */
@Controller
public class ArticleListController extends BaseController {

	@Autowired
	IArticleService articleService;
	
	@RequestMapping("/article/list.do")
	public String list(){
		return "article/list";
	}
	
	/**
	 * 获取全部文章
	 * @throws IOException 
	 * */
	@RequestMapping("/article/listJson.do")
	public void listJson(HttpServletResponse resp) throws IOException{
		List<Article> articleList = articleService.listAll();
		System.out.println(articleList);
		println(resp, gridJson.articleListToGrid(articleList));
	}
	
	
	/**
	 * 列出某个文章类别下面的所有文章
	 * @throws IOException 
	 * */
	@RequestMapping("/article/listByType.do")
	public void listByTypeId(HttpServletRequest req, HttpServletResponse resp, String typeId) throws IOException{
		List<Article> articleList = articleService.listBy(typeId);
		
		println(resp, gridJson.articleListToGrid(articleList));
	}
}