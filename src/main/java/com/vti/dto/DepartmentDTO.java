package com.vti.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {
    private Integer id;
    private String name;
    private Integer totalMembers;
    private String type;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    private List<AccountDTO> accounts;

    @Getter
    @Setter
    public static class AccountDTO extends RepresentationModel<AccountDTO>{
        @JsonProperty("accountId")
        private Integer id;
        private String username;
    }
}
