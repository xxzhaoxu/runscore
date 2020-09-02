package me.zohar.runscore.role.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import me.zohar.runscore.role.domain.JbPermission;
import me.zohar.runscore.role.repo.JbPermissionRepo;
import me.zohar.runscore.role.vo.MenuVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 *
 * Permission 表数据服务层接口
 *
 */
@Validated
@Service
public class IJbPermissionService  {

	private @Autowired EntityManager em;

	@Autowired
	private JbPermissionRepo jbPermissionRepo;

	public List<JbPermission> findAll() {
		return jbPermissionRepo.findAll();
	}

	@SuppressWarnings("unchecked")
	public List<MenuVO> selectMenuVOByUserId(String userId) {
		String sql = "SELECT id, p.pid,sort,type,state, title, url, perm_code FROM jb_permission p "
				+ "RIGHT JOIN(SELECT DISTINCT r.pid FROM jb_role_permission r WHERE "
				+ "r.rid = '"+userId+"') a ON p.id=a.pid "
				+ "WHERE p.pid = '0' AND type=0 ORDER BY sort";
		Query query = em.createNativeQuery(sql, JbPermission.class);
		List<JbPermission> bookList = query.getResultList();
		if (bookList == null || bookList.isEmpty()) {
			return null;
		}

		//List<JbPermission> list = jbPermissionRepo.findByPid("0");

		List<MenuVO> mvList = new ArrayList<MenuVO>();
		for (JbPermission mv : bookList) {
			MenuVO v = new MenuVO();
			v.setId(mv.getId());
			v.setPermCode(mv.getPermCode());
			v.setTitle(mv.getTitle());
			v.setUrl(mv.getUrl());
			sql = "SELECT id, p.pid,sort,type,state, title, url, perm_code FROM jb_permission p "
					+ "RIGHT JOIN(SELECT DISTINCT r.pid FROM jb_role_permission r WHERE "
					+ "r.rid = '"+userId+"') a ON p.id=a.pid "
					+ "WHERE p.pid = '"+mv.getId()+"' AND type=0 ORDER BY sort";
			 query = em.createNativeQuery(sql, JbPermission.class);
			 bookList = query.getResultList();
			//List<JbPermission> jbList = jbPermissionRepo.findByPid(mv.getId());
			v.setSubMenus(bookList);
			mvList.add(v);
		}
		return mvList;
	}

	@SuppressWarnings("unchecked")
	public List<JbPermission> selectAllByUserId(String userId) {
		String sql = "SELECT id, title, url, permCode, icon FROM jb_permission p RIGHT JOIN(SELECT DISTINCT r.pid "
				+ "FROM jb_role_permission r WHERE EXISTS"
				+ " (SELECT 1 FROM jb_user_role u WHERE u.uid="+userId+" AND r.rid=u.rid )) a ON p.id=a.pid";
		Query query = em.createNativeQuery(sql, JbPermission.class);
		List<JbPermission> bookList = query.getResultList();
		return bookList;
	}

}