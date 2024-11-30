package com.java07.baitap.controller;


import com.java07.baitap.dto.RegistrationDTO;
import com.java07.baitap.dto.StudentDTO;
import com.java07.baitap.entity.CourseEntity;
import com.java07.baitap.entity.RegistrationEntity;
import com.java07.baitap.entity.StudentEntity;
import com.java07.baitap.repository.CourseRepository;
import com.java07.baitap.repository.RegistrationRepository;
import com.java07.baitap.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    // Lấy danh sách tất cả sinh viên
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<StudentEntity> students = studentRepository.findAll();
        List<StudentDTO> studentDTOs = students.stream().map(this::convertStudentToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    // Lấy thông tin sinh viên theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        return studentRepository.findById(id).map(student -> new ResponseEntity<>(convertStudentToDTO(student), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Thêm mới sinh viên
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentEntity student = new StudentEntity();
        student.setName(studentDTO.getStudentName());
        student.setEmail(studentDTO.getEmail());
        student.setAge(studentDTO.getAge());
        StudentEntity savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // Cập nhật thông tin sinh viên theo id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @RequestBody StudentDTO studentDTO) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(studentDTO.getStudentName());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setAge(studentDTO.getAge());
            StudentEntity updatedStudent = studentRepository.save(existingStudent);
            return new ResponseEntity<>(convertStudentToDTO(updatedStudent), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Xóa sinh viên theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        return studentRepository.findById(id).map(student -> {
            studentRepository.delete(student);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Tìm kiếm sinh viên theo tên (keyword)
    @GetMapping("/search")
    public ResponseEntity<?> findByNameContaining(@RequestParam("name") String keyword) {
        return ResponseEntity.ok(studentRepository.findByNameContaining(keyword));
    }

    // Thêm một danh sách khóa học mà sinh viên đã đăng ký
    @PostMapping("/{studentId}/courses")
    public ResponseEntity<?> registerCoursesForStudent(@PathVariable int studentId, @RequestBody List<Integer> courseId) {
        return studentRepository.findById(studentId).map(student -> {

            List<CourseEntity> courses = courseRepository.findAllById(courseId);
            List<RegistrationEntity> registrations = courses.stream().map(course -> {
                RegistrationEntity registration = new RegistrationEntity();
                registration.setStudent(student);
                registration.setCourse(course);
                registration.setRegistrationDate(new Date());
                return registration;
            }).collect(Collectors.toList());
            registrationRepository.saveAll(registrations);
            // Chuyển đổi sang DTO và trả về
            List<RegistrationDTO> registrationDTOs = registrations.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(registrationDTOs, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Lấy danh sách tất cả các khóa học mà một sinh viên đã đăng ký
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<?> getCoursesForStudent(@PathVariable int studentId) {
        List<RegistrationEntity> registrations = registrationRepository.findByStudentId(studentId);
        List<RegistrationDTO> registrationDTOs = registrations.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(registrationDTOs, HttpStatus.OK);
    }

    // Thêm một sinh viên vào một khóa học
    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<?> addStudentToCourse(@PathVariable int studentId, @PathVariable int courseId) {
        return studentRepository.findById(studentId).flatMap(student -> courseRepository.findById(courseId).map(course -> {
            RegistrationEntity registration = new RegistrationEntity();
            registration.setStudent(student);
            registration.setCourse(course);
            registration.setRegistrationDate(new Date());
            registrationRepository.save(registration);
            RegistrationDTO registrationDTO = convertToDTO(registration);
            return new ResponseEntity<>(registrationDTO, HttpStatus.CREATED);
        })).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Hủy đăng ký của sinh viên khỏi một khóa học
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<?> unregisterStudentFromCourse(@PathVariable int studentId, @PathVariable int courseId) {
        List<RegistrationEntity> registrations = registrationRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (!registrations.isEmpty()) {
            // Xoá tất cả các bản ghi trong danh sách
            registrationRepository.deleteAll(registrations);
            return new ResponseEntity<>("Unregistered successfully!", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Registration not found!", HttpStatus.NOT_FOUND);
        }
    }

    // Helper method to convert Entity to DTO
    private StudentDTO convertStudentToDTO(StudentEntity student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId(student.getId());
        studentDTO.setStudentName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setAge(student.getAge());
        return studentDTO;
    }

    // Helper method to convert Registration to RegistrationDTO
    private RegistrationDTO convertToDTO(RegistrationEntity registration) {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setRegistrationId(registration.getId());
        registrationDTO.setStudentId(registration.getStudent().getId());
        registrationDTO.setCourseId(registration.getCourse().getId());
        registrationDTO.setRegistrationDate(registration.getRegistrationDate());
        return registrationDTO;
    }

}
