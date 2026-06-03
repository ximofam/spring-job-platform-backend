package com.htweb.admin.services;

import com.htweb.admin.wrappers.EmployerUpdateForm;

public interface EmployerService {
    void updateEmployer(Long userId, EmployerUpdateForm form);
    void approveEmployer(Long userId);
    void rejectEmployer(Long userId);
    EmployerUpdateForm getEmployerEditForm(Long userId);
}