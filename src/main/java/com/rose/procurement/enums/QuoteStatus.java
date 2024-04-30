package com.rose.procurement.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuoteStatus {
    /**
     * MPESA AND PAYPAL
     */
    Waiting_for_offer,
    SUPPLIER_HAS_OFFERED,
    BUYER_ACCEPTED,
    BUYER_HAS_CANCELED
}

