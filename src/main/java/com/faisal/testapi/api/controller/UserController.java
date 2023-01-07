package com.faisal.testapi.api.controller;

import com.faisal.testapi.api.exception.ResponseMessage;
import com.faisal.testapi.api.model.Employee;
import com.faisal.testapi.api.model.TypeUser;
import com.faisal.testapi.api.repo.TypeUserRepo;
import com.faisal.testapi.api.repo.EmployeeRepo;
import com.faisal.testapi.api.utils.ExcelHelper;
import com.faisal.testapi.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private ExcelService excelService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private TypeUserRepo typeUserRepo;


    @GetMapping(value = "/allEmployee")
    public List<Employee> getUsers(){
        Date date = new Date();
        logger.info("test =======================> "+date);
        return employeeRepo.findAll();
    }

    @PostMapping("/createDataEmployee")
    Employee addUser(@RequestBody Employee employee){
        Date date = new Date();
        employee.setCreate_date(date.toString());
        return employeeRepo.save(employee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id){
        String message = "";
        try {
            employeeRepo.deleteById(id);
            message = "Delete Sukses";
        }catch (Exception e){
            message = "Delete Gagal";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

//    @PutMapping("/update/{id}")
//    public User getUserById(@PathVariable Integer id, @RequestBody User users){
//        logger.info("test =======================> ");
//        User user = new User();
//        user = userRepo.getUserById(id);
//
//
//        if(user.getType_id() == 1){
//            user.setName(users.getName());
//            user.setEmail(users.getEmail());
//            user.setPhone(users.getPhone());
//            user.setAddress(users.getAddress());
//            userRepo.save(user);
//        }else{
//            user.setAddress(users.getAddress());
//            userRepo.save(user);
//        }
//        return user;
//    }

//    public TypeUser getTypeUser()

    @PutMapping(value = "/updateEmployee/{role}/{id}")
    public ResponseEntity<Object> updateEmploye(@PathVariable String role, @PathVariable Integer id, @RequestBody Employee employees){
        String message = "TRUE";
        logger.info("ROLE =======================> "+role);
        Employee employee = new Employee();
        TypeUser typeUser = new TypeUser();

        typeUser =  typeUserRepo.getTypeUserBy(role);;
        employee = employeeRepo.getUserById(id);

        if(typeUser != null){
            if(typeUser.getId() == 1){
                employee.setName(employees.getName());
                employee.setEmail(employees.getEmail());
                employee.setPhone(employees.getPhone());
                employee.setAddress(employees.getAddress());
                employeeRepo.save(employee);
                message = "Data Berhasil di Update ADMIN";
            }else{
                employee.setAddress(employees.getAddress());
                employeeRepo.save(employee);
                message = "Data Berhasil di Update USER";
            }
        }else{
            message = "Role Tidak Ada";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }


    @PostMapping("/upload")
    public ResponseEntity<Object> uploadCommuneFile(@RequestParam("data") MultipartFile file) {
        String message = "";
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                excelService.saveUser(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
}
