package eu.arrowhead.application.skeleton.consumer.dto;

import java.io.Serializable;

public class PersonResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int fingerprint_hash;
    private int id_card_hash;
    private int facial_recog_hash;

    public PersonResponseDTO(String name, int fingerprint_hash, int id_card_hash, int facial_recog_hash) {
        this.name = name;
        this.fingerprint_hash = fingerprint_hash;
        this.id_card_hash = id_card_hash;
        this.facial_recog_hash = facial_recog_hash;
    }

    // Get and set for all members
    // Get:
    public String getName() {
        return name;
    }

    public int getFingerprintHash() {
        return fingerprint_hash;
    }

    public int getIdCardHash() {
        return id_card_hash;
    }

    public int getFacialRecogHash() {
        return facial_recog_hash;
    }

    // Set:
    public void setName(String name) {
        this.name = name;
    }

    public void setFingerprintHash(int fingerprint_hash) {
        this.fingerprint_hash = fingerprint_hash;
    }

    public void setIdCardHash(int id_card_hash) {
        this.id_card_hash = id_card_hash;
    }

    public void setFacialRecogHash(int facial_recog_hash) {
        this.facial_recog_hash = facial_recog_hash;
    }
}
