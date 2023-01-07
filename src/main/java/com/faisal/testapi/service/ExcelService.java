package com.faisal.testapi.service;

import com.faisal.testapi.api.model.Employee;
import com.faisal.testapi.api.repo.EmployeeRepo;
import com.faisal.testapi.api.utils.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExcelService {
    Logger logger = LoggerFactory.getLogger(ExcelService.class);
    @Autowired
    private EmployeeRepo employeeRepo;
    public void saveUser(MultipartFile file) {
        logger.info("masuk sini ngga");
        try {
            logger.info("sini dong ==================> ");
            List<Employee> employee = ExcelHelper.excel(file.getInputStream());


            StopWatch watch = new StopWatch();
            watch.start();

            employeeRepo.saveAll(employee);
            watch.stop();
            logger.info("Save communes {} time Elapsed: {}", employee.size(), watch.getTotalTimeSeconds());
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
