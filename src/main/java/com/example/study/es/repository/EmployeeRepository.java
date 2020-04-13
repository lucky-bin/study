package com.example.study.es.repository;

import com.example.study.es.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.math.BigDecimal;

public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

    Employee findBySalaryBetween(BigDecimal startSalary);

//    @Query("""
//        {
//            "_source": ["name", "gender", "salary", "hiredate"],
//            "stored_fields": ["describe"],
//            "query": {
//                "term": {
//                    "_id": "${0}"
//                }
//            }
//        }
//    """)
//    Employee byId(String id);
}
