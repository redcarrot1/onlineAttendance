package com.sungam1004.register.service;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.config.PasswordManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final PasswordManager passwordManager;

    public void loginAdmin(String password) {
        if (!passwordManager.isCorrectAdminPassword(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

    public void changeUserPassword(String password) throws CustomException {
        passwordManager.changeUserPassword(password);
    }

    public void changeAdminPassword(String password) throws CustomException {
        passwordManager.changeAdminPassword(password);
    }
}
