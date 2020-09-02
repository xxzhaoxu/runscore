package me.zohar.runscore.useraccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.UserAccount;


public interface UserAccountRepo extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {

	UserAccount findByIdAndDeletedFlagIsFalse(String id);

	UserAccount findByUserNameAndDeletedFlagIsFalse(String userName);

	UserAccount findBySecretKeyAndDeletedFlagIsFalse(String secretKey);

	List<UserAccount> findByInviterId(String inviterId);

	List<UserAccount> findByAccountLevelPathLikeAndDeletedFlagIsFalse(String accountLevelPath);
}
