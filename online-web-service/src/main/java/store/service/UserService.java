package store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.dao.UserDAO;
import store.entity.User;

@Service
public class UserService {

  @Autowired UserDAO userDao;

  public User getUser(int id) {
    return userDao.getUser(id);
  }
}
