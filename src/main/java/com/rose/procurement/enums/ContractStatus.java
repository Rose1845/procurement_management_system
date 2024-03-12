package com.rose.procurement.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractStatus {
    OPEN,
    PENDING_SUPPLIER_ACCEPTANCE,
    EXPIRED,
    ACCEPTED,
    DECLINE,
    BUYER_ACCEPTANCE,
    RENEW,
    TERMINATE,

}
