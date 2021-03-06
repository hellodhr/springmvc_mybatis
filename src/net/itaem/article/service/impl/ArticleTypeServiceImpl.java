package net.itaem.article.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.itaem.article.dao.IArticleTypeDao;
import net.itaem.article.entity.ArticleType;
import net.itaem.article.service.IArticleTypeService;

@Service
public class ArticleTypeServiceImpl implements IArticleTypeService{
    
	@Autowired
	IArticleTypeDao articleTypeDao;
    
	@Override
	public List<ArticleType> listAll() {
		return articleTypeDao.listAll();
	}

	@Override
	public void add(ArticleType articleType) {
		articleTypeDao.add(articleType);
	}

	@Override
	public void delete(String[] ids) {
		articleTypeDao.delete(ids);
	}

	@Override
	public void update(ArticleType articleType) {
		articleTypeDao.update(articleType);		
	}

	@Override
	public List<ArticleType> listByUserId(String userId) {
		return articleTypeDao.listByUserId(userId);
	}

	@Override
	public ArticleType findById(String id) {
		return articleTypeDao.findById(id);
	}

	@Override
	public List<ArticleType> listByArticleId(String articleId) {
		return articleTypeDao.listByArticleId(articleId);
	}

	@Override
	public int maxSortFlag() {
		return articleTypeDao.maxSortFlag();
	}
}
