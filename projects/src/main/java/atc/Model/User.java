package atc.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "administrator")
public class User {
    @Id
    @NotNull
    private int id;
    @NotBlank
    @Column
    @JsonProperty
    private String username;
    @Column
    @JsonProperty
    @NotBlank
    private String password;

}
