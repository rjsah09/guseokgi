package com.yang.guseokgi.dto.manager;

import com.yang.guseokgi.domain.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerSession {

    private Long id;

    public ManagerSession(Manager manager) {
        this.id = manager.getId();
    }

}
