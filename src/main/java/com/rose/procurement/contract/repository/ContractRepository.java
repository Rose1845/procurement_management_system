package com.rose.procurement.contract.repository;

import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<Contract,String> {
    @Query("SELECT c FROM Contract c LEFT JOIN  c.items LEFT JOIN c.supplier WHERE c.contractId = :contractId")
    Optional<Contract> findByIdWithItems(@Param("contractId") String contractId);
//    @Query("SELECT ci.item FROM Contract ci WHERE ci.contract.contractId = :contractId")
    @Query("SELECT co FROM Contract co WHERE MONTH(co.createdAt) = :month")
    List<Contract> findContractByMonth(@Param("month") int month);
}


