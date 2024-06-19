package peakspace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import peakspace.service.impl.UserServiceImpl;


@SpringBootApplication
@EnableScheduling
public class PeakSpaceB12Application {

    public static void main(String[] args) {
        SpringApplication.run(PeakSpaceB12Application.class, args);
        System.out.println("Peakspace project is running...");
    }
}