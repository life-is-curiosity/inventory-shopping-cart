package store.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import store.entity.User;

public interface UserDAO extends CrudRepository<User, Integer> {
  @Query("select u from User u where u.id = :id ")
  User getUser(@Param("id") int id);
}
