package me.zohar.runscore.useraccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.BankCard;

public interface BankCardRepo extends JpaRepository<BankCard, String>, JpaSpecificationExecutor<BankCard> {

	List<BankCard> findByUserAccountIdAndDeletedFlagFalse(String userAccountId);

	BankCard findByIdAndUserAccountId(String id, String userAccountId);

}
