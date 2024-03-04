package ma.enset.apphospital.repository;

import ma.enset.apphospital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository  extends JpaRepository<Patient,Long> {

 Page<Patient> findByNomContains(String keyword);//en respectnt les convention


    /*@Query("select  p from Patient p where p.nom like :x")//on utilisant les annotations
 Page<Patient>chercher (String keyword);*/

}
