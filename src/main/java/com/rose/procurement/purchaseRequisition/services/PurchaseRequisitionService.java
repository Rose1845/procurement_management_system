package com.rose.procurement.purchaseRequisition.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.ContractStatus;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisitionDto;
import com.rose.procurement.purchaseRequisition.mappers.PurchaseRequisitionMapper;
import com.rose.procurement.purchaseRequisition.repository.PurchaseRequisitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseRequisitionService {

    private final PurchaseRequisitionRepository purchaseRequisitionRepository;

    public PurchaseRequisitionService(PurchaseRequisitionRepository purchaseRequisitionRepository
    ) {
        this.purchaseRequisitionRepository = purchaseRequisitionRepository;
    }

    public PurchaseRequisitionDto createPurchaseRequistion(PurchaseRequisitionDto purchaseRequisitionDto) {
        PurchaseRequisition purchaseRequisition = PurchaseRequisitionMapper.MAPPER.toEntity(purchaseRequisitionDto);
        purchaseRequisition.setRequisitionTitle(purchaseRequisitionDto.getRequisitionTitle());
        purchaseRequisition.setDescription(purchaseRequisitionDto.getDescription());
        purchaseRequisition.setDateNeeded(purchaseRequisitionDto.getDateNeeded());
        purchaseRequisition.setItems(purchaseRequisitionDto.getItems());
        purchaseRequisition.setApprovalStatus(ApprovalStatus.PENDING);
        PurchaseRequisition savedRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
        return PurchaseRequisitionMapper.MAPPER.toDto(savedRequisition);
    }

    public PurchaseRequisitionDto updatePurchaseRequisition(PurchaseRequisitionDto purchaseRequisitionDto, Long requisitionId) {
        PurchaseRequisition existingPurchaseRequisition = purchaseRequisitionRepository.findByRequisitionId(requisitionId);
        existingPurchaseRequisition.setApprovalStatus(purchaseRequisitionDto.getApprovalStatus());
        existingPurchaseRequisition.setRequisitionTitle(purchaseRequisitionDto.getRequisitionTitle());
        existingPurchaseRequisition.setDateNeeded(purchaseRequisitionDto.getDateNeeded());
        existingPurchaseRequisition.setDescription(purchaseRequisitionDto.getDescription());
        PurchaseRequisition updatedPR = purchaseRequisitionRepository.save(existingPurchaseRequisition);
        return PurchaseRequisitionMapper.MAPPER.toDto(updatedPR);
    }
    public PurchaseRequisition updateApprovalStatus(Long requisitionId, ApprovalStatus approvalStatus) throws ProcureException {
        // Retrieve the existing contract from the database
        PurchaseRequisition existingContract = purchaseRequisitionRepository.findByIdWithItems(requisitionId).orElseThrow(()->new ProcureException("id already exists"));
        // Update the approval status
        existingContract.setApprovalStatus(approvalStatus);
        // Save the updated contract in the database
        return purchaseRequisitionRepository.save(existingContract);
    }

    public List<PurchaseRequisition> getAllPurchaseRequisitions() {
        return purchaseRequisitionRepository.findAll();
    }

    public Optional<PurchaseRequisition> getPurchaseRequisitionById(Long requisitionId) {
        return purchaseRequisitionRepository.findByIdWithItems(requisitionId);
    }
}

