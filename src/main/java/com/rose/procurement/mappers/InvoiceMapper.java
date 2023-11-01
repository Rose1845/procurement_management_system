package com.rose.procurement.mappers;

import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.invoice.InvoiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {
    InvoiceMapper MAPPER = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(source = "totalAmount",target = "subTotalAmount")
    InvoiceDto mapToInvoiceDTo(Invoice invoice);
    @Mapping(source = "subTotalAmount",target = "totalAmount")

    Invoice mapToInvoice(InvoiceDto invoiceDto);
}
