package models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)

public class Livro {
}
