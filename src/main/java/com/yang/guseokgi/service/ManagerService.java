package com.yang.guseokgi.service;

import com.yang.guseokgi.dto.manager.ManagerSession;
import com.yang.guseokgi.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final ManagerRepository managerRepository;

    public Optional<ManagerSession> findByIdAndPassword(String uid, String password) {
        return managerRepository.findByIdAndPassword(uid, password).map(ManagerSession::new);
    }

}
