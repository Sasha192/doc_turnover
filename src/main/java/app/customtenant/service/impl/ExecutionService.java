package app.customtenant.service.impl;

import app.customtenant.service.extapis.IMailService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    private ExecutorService executor;

    @PostConstruct
    private void init() {
        executor = Executors.newFixedThreadPool(4);
    }

    public void pushTask(Runnable runnable) {
        executor.execute(runnable);
    }

    public void shutdown() {
        if ((!executor.isTerminated()) || (!executor.isShutdown())) {
            executor.shutdown();
        }
    }

    public static class MailSender implements Runnable {

        private IMailService mailService;
        private String email;
        private String html;
        private FileSystemResource[] resources;

        public MailSender(final IMailService mailService, final String email,
                          final String html, FileSystemResource... resources) {
            this.mailService = mailService;
            this.email = email;
            this.html = html;
            this.resources = resources;
        }

        @Override
        public void run() {
            mailService.sendMimeMessage(email, "Verification Code", html, resources);
        }
    }
}
