package estore.repository.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import estore.admin.bean.AccountFilter;
import estore.repository.Account;
import estore.repository.AccountDAO;
import estore.repository.Authority;
import estore.repository.AuthorityDAO;
import estore.repository.Role;
import estore.repository.RoleDAO;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountDAO dao;
	
	@Autowired
	AuthorityDAO authorityDao;
	
	@Autowired
	RoleDAO roleDao;

	@Override
	public Account getByUsername(String username) {
		return dao.getById(username);
	}

	@Override
	public void deleteByUsername(String username) {
		dao.deleteById(username);
	}

	@Transactional
	@Override
	public void create(Account item, List<String> roleIds) {
//		List<Authority> authorities = roleIds.stream().map(rid -> {
//			Role role = roleDao.getById(rid);
//			Authority authority = new Authority(null, item, role);
//			return authority;
//		}).toList();
		List<Authority> authorities = new ArrayList<>();
		for(String roleId: roleIds) {
			Role role = roleDao.getById(roleId);
			Authority authority = new Authority(null, item, role);
			authorities.add(authority);
		}
		item.setAuthorities(authorities);
		dao.save(item);
		authorityDao.saveAll(authorities);
	}

	@Transactional
	@Override
	public void update(Account item, List<String> roleIds) {
		authorityDao.deleteAll(dao.getById(item.getUsername()).getAuthorities());
		dao.save(item);
		if(!roleIds.isEmpty()) {
			List<Authority> authorities = roleIds.stream().map(rid -> {
				Role role = roleDao.getById(rid);
				Authority authority = new Authority(null, item, role);
				return authority;
			}).toList();
			authorityDao.saveAll(authorities);
			item.setAuthorities(authorities);
		}
	}
	
	@Override
	public Page<Account> findPageByFilter(AccountFilter filter, Pageable pageable) {
		String keyword = "%"+filter.getKeyword()+"%";
		// no role
		if(filter.getRole() == 2) { 
			if(filter.getActivated() == 2) {
				return dao.findAccountByKeyword(keyword, pageable);
			}
			return dao.findAccountByKeywordAndActivated(keyword, filter.getActivated() == 1, pageable);
		}
		// customer
		if(filter.getRole() == 0) { 
			if(filter.getActivated() == 2) {
				return dao.findCustomerByKeyword(keyword, pageable);
			}
			return dao.findCustomerByKeywordAndActivated(keyword, filter.getActivated() == 1, pageable);
		}
		// master
		if(filter.getActivated() == 2) {
			return dao.findMasterByKeyword(keyword, pageable);
		}
		return dao.findMasterByKeywordAndActivated(keyword, filter.getActivated() == 1, pageable);
	}
}