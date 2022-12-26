package com.sungam1004.register.utill;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class PasswordManager {

    private String userPassword = "1234"; // 초기화 비밀번호
    private String adminPassword = "tjddkaryghl"; // 초기화비밀번호

    public void changeUserPassword(String nPassword) throws CustomException {
        if (Pattern.matches("^[0-9]{4}$", nPassword)) {
            userPassword = nPassword;
        }
        else throw new CustomException(ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD);
    }

    public Boolean isCorrectUserPassword(String nPassword) {
        return Objects.equals(userPassword, nPassword);
    }

    public void changeAdminPassword(String nPassword) throws CustomException {
        if (nPassword.length() >= 5 && nPassword.length() <= 20) {
            adminPassword = nPassword;
        }
        else throw new CustomException(ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD);
    }

    public Boolean isCorrectAdminPassword(String nPassword) {
        return Objects.equals(adminPassword, nPassword);
    }
}
