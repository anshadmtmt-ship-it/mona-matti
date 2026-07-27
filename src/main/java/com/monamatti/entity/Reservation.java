package com.monamatti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation", indexes = {
    @Index(name = "idx_reservation_created_at", columnList = "created_at")
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @NotBlank(message = "Signature is required")
    @Column(name = "signature", columnDefinition = "TEXT", nullable = false)
    private String signature;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Reservation() {}

    public Reservation(String fullName, String signature) {
        this.fullName = fullName;
        this.signature = signature;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /** Formatted reservation ID: MM-000001 */
    public String getFormattedId() {
        return String.format("MM-%06d", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation r = (Reservation) o;
        return id != null && id.equals(r.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", fullName='" + fullName + "'}";
    }
}
