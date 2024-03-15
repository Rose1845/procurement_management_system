package com.rose.procurement.enums;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApprovalStatus {
    PENDING,
    APPROVED,
    REJECT,
    ISSUED,
    IN_DELIVERY,
    FULLY_RECEIVED,
    COMPLETED,
    CLOSED;
}
