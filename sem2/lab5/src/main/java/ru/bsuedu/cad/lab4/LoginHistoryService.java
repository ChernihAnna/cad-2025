package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    public List<LoginHistory> getAllLoginHistory() {
        return loginHistoryRepository.findAll();
    }

    public LoginHistory addLoginHistory(LoginHistory loginHistory) {
        return loginHistoryRepository.save(loginHistory);
    }
}