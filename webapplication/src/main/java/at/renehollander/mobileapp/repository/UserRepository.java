package at.renehollander.mobileapp.repository;

import at.renehollander.mobileapp.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}
