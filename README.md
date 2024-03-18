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
