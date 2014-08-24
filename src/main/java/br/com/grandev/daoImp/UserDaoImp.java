package br.com.grandev.daoImp;

import java.util.List;

import br.com.grandev.dao.UserDao;
import br.com.grandev.model.User;

/**
 * @author Thiago Carvalho
 * 
 */
public class UserDaoImp extends DaoGenericoUsersImp<User, String> implements UserDao {

	private static final long serialVersionUID = 1L;

	@Override
	public List<User> todos(String ordem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
