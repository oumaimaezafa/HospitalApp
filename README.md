# HospitalApp

Cette activité est une application Web JEE basée sur Spring MVC, Thymeleaf et Spring Data JPA qui permet de gérer les patients. L'application doit permettre les fonctionnalités suivantes :

<ul>
  <li>Afficher les patients</li>
  <li>Faire la pagination</li>
  <li>Chercher les patients</li>
  <li>Supprimer un patient</li>
  <li>Faire des améliorations supplémentaires</li>
  <li>Créer une page template</li>
  <li>Faire la validation des formulaires</li>
  <li>Faire la sécurité avec Spring Security</li>
</ul>
Tout d'abord, nous ajoutons des dépendances pour utiliser les fonctionnalités de Spring en modifiant le fichier <code>pom.xml</code>  comme suit :

```java
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>nz.net.ultraq.thymeleaf</groupId>
            <artifactId>thymeleaf-layout-dialect</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.webjars.npm/bootstrap-icons -->
        <dependency>
            <groupId>org.webjars.npm</groupId>
            <artifactId>bootstrap-icons</artifactId>
            <version>1.11.3</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.webjars/bootstrap -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>5.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!-- https:
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity6</artifactId>
            <version>3.1.0.M1</version>
        </dependency>

    </dependencies>

```
Ensuite,on  doit configurer les propriétés suivantes dans le fichier de configuration

```java
server.port=8884
#spring.datasource.url=jdbc:h2:mem:patients-db
#spring.h2.console.enabled=true
spring.datasource.url=jdbc:mysql://localhost:3306/appHospital-db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
#on peut aussi specifier la format du dat dans properties au lieu des annotations
#spring.mvc.format.date=yyyy-MM-dd


```
Puis On  crée notre application , on cree les entities  avec des annotations JPA pour faire <code>le mapping objet relationnel</code>  : 

``` java
package ma.enset.apphospital.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity
//Builder
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Patient {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min=4,max =30)
    private String nom;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date dateNaissance;
    private boolean malade;
    @DecimalMax("100")
    private int score;
}
```
<ul>
<li><code>@Entity</code> : Indique que la classe est persistante est correspond à une table dans la base de donée</li>
<li> <code>@Data</code> : est utilisée pour générer automatiquement les méthodes toString(), equals(), hashCode(), ainsi que les méthodes getter et setter pour tous les champs de classe  </li>
<li><code>@AllArgsConstructor</code> : génère un constructeur prenant en compte tous les champs de la classe.</li>
<li><code>@NoArgsConstructor</code> : génère un constructeur sans argument pour la classe.</li>
<li><code>@Id</code> : Associer un champ de la table à la propriété en tant que clé primaire.</li>
<li><code>@GeneratedValue(strategy = GenerationType.IDENTITY) : </code> Demander la génération automatique de la clé primaire au besoin</li>
<li><code>@DateTimeFormat(pattern = "yyy-MM-dd")</code> : Pour spécifier le format de la date, il est possible de l'inclure directement dans les annotations ou de le définir dans le fichier <code>application.properties</code> avec la clé <code>spring.mvc.format.date=yyyy-MM-dd</code>.</li>
<li><code>@Temporal(TemporalType.DATE)</code> : indique que seules les informations de date (année, mois et jour) doivent être stockées dans la base de données, sans tenir compte de l'heure.</li>
</ul>

<h2>Repository</h2>
Le repository contient une interface <code>PatientRepository</code> qui étend <code>JpaRepository</code>. Cela signifie que PatientRepository hérite de toutes les méthodes définies dans JpaRepository, telles que <code>save()</code>,  <code>findById()</code>,  <code>findAll()</code>,  <code>deleteById()</code>, etc., qui fournissent des fonctionnalités de base pour interagir avec les données des patients en utilisant <code>Spring Data JPA</code>.
Cette interface peut également contenir des méthodes personnalisées qui utilisent soit <code>les conventions de nommage</code>  de Spring Data JPA, comme<code>findByNomContains()</code>, soit l'annotation <code>@Query</code>, permettant de définir des requêtes spécifiques à la base de données pour des opérations plus complexes.

```java
package ma.enset.apphospital.repository;

import ma.enset.apphospital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository  extends JpaRepository<Patient,Long> {

     Page<Patient> findByNomContains(String keyword, Pageable pageable);//en respectnt les convention

    /*@Query("select  p from Patient p where p.nom like :x")//on utilise les annotations pour definir notre query
       Page<Patient>chercher (@param("x")String keyword,Pageable pageable );*/

}
```
<h2>Security</h2>
Il  représente la configuration de la sécurité de l'application , On utilisant les annotations necessaires  du configuration comme
<ul>
<li><code>@Configuration</code> :Indique à Spring que cette classe est une configuration de l'application</li>
<li><code>@EnableWebSecurity</code> : Cette annotation active la sécurité Web dans l'application. Elle permet de configurer la sécurité des requêtes HTTP</li>
<li><code>@EnableMethodSecurity(prePostEnabled = true)</code> : Cette annotation active la sécurité basée sur les annotations pour les méthodes de l'application. Elle permet d'utiliser les annotations de sécurité telles que <code >@PreAuthorize</code> et <code>@PostAuthorize</code> pour sécuriser les méthodes.</li>
<li><code>@PreAuthorize ,@PostAuthorize </code> :  sont utilisées dans les contrôleurs pour restreindre l'accès aux méthodes en fonction des rôles des utilisateurs , On a remplacer ce code la </li>
<code>//http.authorizeRequests().requestMatchers("/user/**").hasRole("USER");
//http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN");
</code>
avec l'annotation <code>@EnableMethodSecurity(prePostEnabled = true)</code>
<li>Pour que Spring puisse comparer les mots de passe, nous utilisons <code>{noop}</code> car cela signifie "no operation", indiquant à Spring de ne pas effectuer de hachage sur les mots de passe.</li></ul>

```java
package ma.enset.apphospital.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    //Configure authentication mechanism
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return  new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").permitAll();
        http.rememberMe();
        http.authorizeRequests().requestMatchers("/webjars/**").permitAll();
        //http.authorizeRequests().requestMatchers("/user/**").hasRole("USER");
        //http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        // POUR AFFICHER UN ERREUR A UN UTILISATEUR QUE N'A PAS LE DROIT
        http.exceptionHandling().accessDeniedPage("/notAuthorized");
        http.authorizeRequests()
                .anyRequest().authenticated();
        return http.build();
    }


}

```

<h2>Controller</h2>
Le contrôleur gère les requêtes HTTP pour afficher, ajouter, éditer et supprimer des patients dans l'application. Il utilise des annotations telles que <code>@GetMapping</code> et <code>@PostMapping</code> pour mapper les URL aux méthodes, ainsi que des annotations de sécurité comme <code>@PreAuthorize</code> pour restreindre l'accès aux méthodes en fonction des rôles des utilisateurs , tous ces methodes retourne une vue qui sera affichée dans le navigateur du client.
Pour envoyer un attribut à une vue dans Spring MVC, on doit  utiliser la méthode <code> addAttribute()</code> de l'objet Model. Cette méthode prend deux arguments : le nom de l'attribut et sa valeur.

```java
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
    // l'injection se fait via Autowired ou bien via constrecteur
    private PatientRepository patientRepository;

    //la creation d'une methode qui retourne  une vue de type String 

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
```
<h2>Template </h2>