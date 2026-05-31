package com.hmeclazcke.simplewebservicetest.controller;

import com.hmeclazcke.simplewebservicetest.dto.AccountResponse;
import com.hmeclazcke.simplewebservicetest.dto.CreateAccountRequest;
import com.hmeclazcke.simplewebservicetest.entity.Account;
import com.hmeclazcke.simplewebservicetest.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// CONTROLLER:
// This layer receives HTTP calls.
@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // @RequestBody: Automatically converts the received JSON into an Account object.
    @PostMapping("/account")
    public AccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(
                request.holder(),
                request.balance()
        );
    }

    @GetMapping("/account")
    public List<AccountResponse> listAccounts() {
        return accountService.listAccounts();
    }

}
