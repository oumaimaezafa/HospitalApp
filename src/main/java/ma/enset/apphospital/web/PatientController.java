package ma.enset.apphospital.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.enset.apphospital.entities.Patient;
import ma.enset.apphospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    // l'ijection se fait via Autowired ou bien via constrecteur
    private PatientRepository patientRepository;

    //je creer une methode qui retourne un String permet de retourner un vue

    @GetMapping("/user/index")
    public String index(Model model, @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "7") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String kw){


        Page<Patient> patients=patientRepository.findByNomContains(kw,PageRequest.of(page,size));//pour afficher une liste de patients
         model.addAttribute("patients",patients.getContent());
         model.addAttribute("pages",new int[patients.getTotalPages()]);
         model.addAttribute("currentPage",page);
         model.addAttribute("keyword",kw);

         return "patients";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/delete")
    public String delete(Long id, @RequestParam(value = "keyword" , defaultValue = "") String keyword, @RequestParam (value = "page", defaultValue ="0") int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/formPatients")
    public String formPatient(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/admin/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors())return  "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/editPatient")
    public String editPatient(Model model,Long id,String keyword,int page ){
        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient ==null) throw  new RuntimeException("Patient introuvable !!");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }
}
