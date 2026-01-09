package org.alex.donor.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alex.donor.repository.ProgramareRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramareScheduler {

    private final ProgramareRepository programareRepo;


    @Scheduled(cron = "0 1 0 * * ?")
    public void verificareZilnica() {
        executaAnularea();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void verificareLaPornire() {
        log.info("Sistemul verifică programările expirate la pornire...");
        executaAnularea();
    }

    private void executaAnularea() {
        LocalDateTime inceputAzi = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        int modificate = programareRepo.anuleazaProgramariNeprezentate(inceputAzi);

        if (modificate > 0) {
            log.info("Succes: {} programări neprezentate au fost anulate automat.", modificate);
        } else {
            log.info("Nu au fost găsite programări expirate de anulat.");
        }
    }
}