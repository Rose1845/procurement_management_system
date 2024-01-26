package com.rose.procurement.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractStatus {
    OPEN,
    EXPIRED,
    ISSUED,
    INDELIVERY,
    FULLYRECEIVED,
    CLOSED
}
