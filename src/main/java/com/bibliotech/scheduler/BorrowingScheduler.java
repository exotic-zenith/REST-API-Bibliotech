package com.bibliotech.scheduler;

import com.bibliotech.model.Borrowing;
import com.bibliotech.model.BorrowingStatus;
import com.bibliotech.repository.BorrowingRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BorrowingScheduler {

    private final BorrowingRepository borrowingRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkOverdueBorrowings() {
        LocalDate overdueDate = LocalDate.now().minusDays(14);
        List<Borrowing> overdueBorrowings = borrowingRepository.findByStatusAndBorrowDateBefore(
            BorrowingStatus.ONGOING,
            overdueDate
        );
        overdueBorrowings.forEach(b -> b.setStatus(BorrowingStatus.OVERDUE));
        borrowingRepository.saveAll(overdueBorrowings);
        log.info("{} emprunt(s) marques OVERDUE.", overdueBorrowings.size());
    }
}

