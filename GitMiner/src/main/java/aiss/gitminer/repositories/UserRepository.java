package aiss.gitminer.repositories;

import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    default void userExists(String userId) {
        ValidationUtils.validateUserId(userId);
        if (!existsById(userId)) throw new UserNotFoundException();
    }
}
