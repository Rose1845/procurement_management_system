package com.rose.procurement.contract.repository;

import com.rose.procurement.contract.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract,String> {
}
