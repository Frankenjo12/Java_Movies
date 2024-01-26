/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.dal.sql.SqlRepository;

/**
 *
 * @author hyperv
 */
public class RepositoryFactory {

    private static Repository repository;
    
    private RepositoryFactory() {
    }
    
    public static Repository getRepository() throws Exception {
        if (repository == null) {
            repository = new SqlRepository();
        }
        return repository;
    }
    
    
    
}
