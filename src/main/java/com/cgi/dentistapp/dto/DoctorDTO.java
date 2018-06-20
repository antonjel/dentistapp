package com.cgi.dentistapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DoctorDTO {


    String type;

    @NotNull
    @Size(min = 1, max = 50)
    String name;

    public DoctorDTO() {
    }

    public DoctorDTO(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
