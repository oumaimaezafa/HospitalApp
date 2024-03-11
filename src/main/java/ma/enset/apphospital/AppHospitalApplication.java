package ma.enset.apphospital;

import ma.enset.apphospital.entities.Patient;
import ma.enset.apphospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class AppHospitalApplication implements CommandLineRunner {
    @Autowired
    private PatientRepository patientRepository;
    public static void main(String[] args) {
        SpringApplication.run(AppHospitalApplication.class, args);
    }



    @Override
    public void run(String... args) throws Exception {
       /* patientRepository.save(new Patient(null,"Oumaima",new Date(),false,34));
        patientRepository.save(new Patient(null,"Duha",new Date(),true,34));
        patientRepository.save(new Patient(null,"Meryem",new Date(),false,74));
        patientRepository.save(new Patient(null,"lama",new Date(),true,80));*/


       /*
       Une facon de creer un patient
       Patient patient1=Patient.builder()
                .nom("Imane")
                .dateNaissance(new Date())
                .malade(false)
                .score(123).build();*/

    }





}
