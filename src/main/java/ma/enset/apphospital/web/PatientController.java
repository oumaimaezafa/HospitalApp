package ma.enset.apphospital.web;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.enset.apphospital.entities.Patient;
import ma.enset.apphospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    // l'ijection se fait via Autowired ou bien via constrecteur
    private PatientRepository patientRepository;

    //je creer une methode qui retourne un String permet de retourner un vue

    @GetMapping("/index")
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

    @GetMapping("/delete")
    public String delete(Long id, @RequestParam(value = "keyword" , defaultValue = "") String keyword, @RequestParam (value = "page", defaultValue ="0") int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

}
