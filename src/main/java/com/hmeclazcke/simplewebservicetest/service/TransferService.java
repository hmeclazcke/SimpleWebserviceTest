package com.hmeclazcke.simplewebservicetest.service;

import com.hmeclazcke.simplewebservicetest.dto.TransferResponse;
import com.hmeclazcke.simplewebservicetest.entity.Transfer;
import com.hmeclazcke.simplewebservicetest.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*
 * COORDINATOR SERVICE
 *
 * Coordinates the complete use case:
 * 1. Create a PENDING transfer.
 * 2. Try to execute the transfer.
 * 3. If it fails, mark the transfer as FAILED.
 *
 * Important:
 * This class does NOT use @Transactional because we need
 * the PENDING transfer to remain stored even if the movement fails later.
 */
@Service
public class TransferService {

    /*
     * These dependencies are Spring beans.
     * Spring creates them when the application starts, stores them in the ApplicationContext,
     * and injects them through this constructor.
     */
    private final TransferOperationService operationService;
    private final TransferRepository transferRepository;

    public TransferService(
            TransferOperationService operationService,
            TransferRepository transferRepository ) {
        this.transferRepository = transferRepository;
        this.operationService = operationService;
    }

    public TransferResponse transfer(Long originAccountId,
                               Long destinationAccountId,
                               BigDecimal amount) {

        /*
         * TRANSACTION 1:
         * The transfer is created and committed as PENDING.
         */
        Transfer transfer =
                operationService.createPendingTransfer(originAccountId, destinationAccountId, amount);

        try {
            /*
             * TRANSACTION 2:
             * The origin account is debited, the destination account is credited,
             * and the transfer is marked as COMPLETED.
             */
            operationService.executeTransfer(transfer.getId());

        } catch (RuntimeException e) {
            /*
             * TRANSACTION 3:
             * If the movement failed, the transfer remains registered as FAILED.
             */
            operationService.markAsFailed(transfer.getId());

            throw e;
        }

        /*
         * We retrieve the transfer again to return it
         * with its updated status: COMPLETED.
         */
        Transfer updatedTransfer = operationService.searchById(transfer.getId());
        return toResponse(updatedTransfer);
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> listTransfers() {
        return transferRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getOriginAccount().getId(),
                transfer.getDestinationAccount().getId(),
                transfer.getAmount(),
                transfer.getStatus()
        );
    }


}
