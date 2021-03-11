package atc.Repositories;

import atc.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * from administrator where username = :name", nativeQuery = true)
    User findUserByUsername(@Param("name") String name);

}
