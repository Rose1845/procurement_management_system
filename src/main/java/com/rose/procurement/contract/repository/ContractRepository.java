package com.rose.procurement.contract.repository;

import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.items.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<Contract,String> {

//    @Query("SELECT ci.item FROM Contract ci WHERE ci.contract.contractId = :contractId")
    Set<Item> findItemsByContractId(@Param("contractId") String contractId);
}
