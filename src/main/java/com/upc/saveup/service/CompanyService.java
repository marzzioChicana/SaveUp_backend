package com.upc.saveup.service;

import com.upc.saveup.model.Company;

import java.util.List;
import java.util.Map;

public interface CompanyService {

    public abstract Company createCompany(Company company);
    public abstract void updateCompany(Company company);
    public abstract void deleteCompany(int id);
    public abstract Company getCompany(int id);
    public abstract List<Company> getAllCompanies();
    public abstract boolean isCompanyExist(int id);
    public List<Map<String, Object>> getSaleData(int companyId);
}
