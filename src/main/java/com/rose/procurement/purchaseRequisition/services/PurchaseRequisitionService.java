package com.rose.procurement.purchaseRequisition.services;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
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

    public PurchaseRequisitionDto createPurchaseRequistion(PurchaseRequisitionDto purchaseRequisitionDto){
        PurchaseRequisition purchaseRequisition = PurchaseRequisitionMapper.MAPPER.toEntity(purchaseRequisitionDto);
        purchaseRequisition.setRequisitionTitle(purchaseRequisitionDto.getRequisitionTitle());
        purchaseRequisition.setDescription(purchaseRequisitionDto.getDescription());
        purchaseRequisition.setDateNeeded(purchaseRequisitionDto.getDateNeeded());
        purchaseRequisition.setItems(purchaseRequisitionDto.getItems());
        purchaseRequisition.setApprovalStatus(purchaseRequisitionDto.getApprovalStatus());
        PurchaseRequisition savedpurchaseR = purchaseRequisitionRepository.save(purchaseRequisition);
        PurchaseRequisitionDto saveDto = PurchaseRequisitionMapper.MAPPER.toDto(savedpurchaseR);
        return saveDto;
    }
    public PurchaseRequisitionDto updatePurchaseRequisition(PurchaseRequisitionDto purchaseRequisitionDto,Long requisitionId){
        PurchaseRequisition existingPurchaseRequisition = purchaseRequisitionRepository.findByRequisitionId(requisitionId);
        existingPurchaseRequisition.setApprovalStatus(purchaseRequisitionDto.getApprovalStatus());
        existingPurchaseRequisition.setRequisitionTitle(purchaseRequisitionDto.getRequisitionTitle());
        existingPurchaseRequisition.setDateNeeded(purchaseRequisitionDto.getDateNeeded());
        existingPurchaseRequisition.setDescription(purchaseRequisitionDto.getDescription());
        PurchaseRequisition updatedPR = purchaseRequisitionRepository.save(existingPurchaseRequisition);
        PurchaseRequisitionDto saveUpdatedDto = PurchaseRequisitionMapper.MAPPER.toDto(updatedPR);
        return saveUpdatedDto;



    }

    public List<PurchaseRequisition> getAllPurchaseRequisitions() {
        return purchaseRequisitionRepository.findAll();
    }

    public Optional<PurchaseRequisition> getPurchaseRequisitionById(Long  requisitionId) {
        return purchaseRequisitionRepository.findById(requisitionId);
    }
}
