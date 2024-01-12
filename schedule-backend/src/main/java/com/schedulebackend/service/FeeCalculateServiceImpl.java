package com.schedulebackend.service;

import com.schedulebackend.domain.FinancialTransfer;
import com.schedulebackend.repository.FinancialTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FeeCalculateServiceImpl implements FeeCalculateService {

    @Autowired
    private FinancialTransferRepository financialRepository;

    @Override
    public BigDecimal feeCalculationA(FinancialTransfer financialTransfer) {

        BigDecimal feeThree = new BigDecimal("3");

        BigDecimal originalValue = financialTransfer.getTransferAmount();

        BigDecimal threePercent = originalValue.multiply(new BigDecimal("0.03"));

        BigDecimal resultAfterPercent = originalValue.subtract(threePercent);

        BigDecimal resultAfterFee = resultAfterPercent.subtract(feeThree);

        return originalValue.subtract(resultAfterFee);

    }

    @Override
    public BigDecimal feeCalculationB(FinancialTransfer financialTransfer) {

        return new BigDecimal("12");

    }

    @Override
    public BigDecimal feeCalculationC(FinancialTransfer financialTransfer) {

        LocalDate transferDate = financialTransfer.getTransferDate().toLocalDate();
        LocalDate scheduledDate = financialTransfer.getScheduledDate().toLocalDate();

        long daysDifference = DAYS.between(scheduledDate, transferDate);

        if (daysDifference > 40) {
            return financialTransfer.getTransferAmount().multiply(new BigDecimal("0.017"));
        } else if (daysDifference > 30) {
            return financialTransfer.getTransferAmount().multiply(new BigDecimal("0.047"));
        } else if (daysDifference > 20) {
            return financialTransfer.getTransferAmount().multiply(new BigDecimal("0.069"));
        } else if (daysDifference > 10) {
            return financialTransfer.getTransferAmount().multiply(new BigDecimal("0.082"));
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal feeCalculationD(FinancialTransfer financialTransfer) {

        if (financialTransfer.getTransferAmount().compareTo(new BigDecimal("1000")) <= 0) {
            return feeCalculationA(financialTransfer);
        } else if (financialTransfer.getTransferAmount().compareTo(new BigDecimal("1001")) > 0
                && financialTransfer.getTransferAmount().compareTo(new BigDecimal("2000")) <= 0) {
            return feeCalculationB(financialTransfer);
        } else if (financialTransfer.getTransferAmount().compareTo(new BigDecimal("2000")) > 0) {
            return feeCalculationC(financialTransfer);
        } else {
            System.out.println("Erro: Valor de transferência inválido");
            return new BigDecimal("0");
        }
    }
}
