package com.rose.procurement.invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceMapperImpl implements InvoiceMapper{


    @Override
    public Invoice toEntity(InvoiceDto invoiceDto) {
        log.info("Mapping invoice dto to its equivalent invoice entity");
         Invoice invoice = Invoice.builder().build();
         invoice.setInvoiceNumber(invoiceDto.getInvoiceNumber());
        BeanUtils.copyProperties(
                invoiceDto,invoice
        );
        return invoice;
    }

    @Override
    public InvoiceDto toDto(Invoice invoice) {
        log.info("Mapping invoice entity to its equivalent invoice dto");
        InvoiceDto invoiceDto = InvoiceDto.builder().build();
        BeanUtils.copyProperties(invoice,invoiceDto);
        invoiceDto.setInvoiceNumber(invoice.getInvoiceNumber());
        return invoiceDto;
    }

    @Override
    public Invoice partialUpdate(InvoiceDto invoiceDto, Invoice invoice) {
        return null;
    }
}
