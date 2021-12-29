package estore.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import estore.repository.RoleDAO;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleDAO dao;
}
