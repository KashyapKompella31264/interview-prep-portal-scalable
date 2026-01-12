package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;

@Service
public class AdminService {
	@Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    public Optional<Admin> getAdminById(String id) {  // Fixed ID type
        return adminRepository.findById(id);
    }

    public Admin updateAdmin(String id, Admin updatedAdmin) {
        return adminRepository.findById(id).map(admin -> {
            admin.setName(updatedAdmin.getName());
            return adminRepository.save(admin);
        }).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public void deleteAdmin(String id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(id);
    }
}
