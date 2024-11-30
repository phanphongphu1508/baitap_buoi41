package com.java07.baitap.controller;

import com.java07.baitap.dto.CourseDTO;
import com.java07.baitap.dto.RegistrationDTO;
import com.java07.baitap.entity.CourseEntity;
import com.java07.baitap.entity.RegistrationEntity;
import com.java07.baitap.repository.CourseRepository;
import com.java07.baitap.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    // Lấy danh sách các khóa học có thời gian lớn hơn một giá trị
    @GetMapping
    public ResponseEntity<?> getCoursesByDuration(@RequestParam int durationGreaterThan) {
        List<CourseEntity> courses = courseRepository.findByDurationGreaterThan(durationGreaterThan);
        List<CourseDTO> courseDTOs = courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }

    // Lấy số lượng các khóa học hiện có
    @GetMapping("/count")
    public ResponseEntity<?> getCourseCount() {
        long count = courseRepository.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Lấy danh sách sinh viên đăng ký vào một khóa học cụ thể
    @GetMapping("/{courseId}/students")
    public ResponseEntity<?> getStudentsForCourse(@PathVariable int courseId) {
        List<RegistrationEntity> registrations = registrationRepository.findByCourseId(courseId);
        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(registrationDTOs, HttpStatus.OK);
    }

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<?> getCoursesForStudent(@PathVariable int studentId) {
        List<RegistrationEntity> registrations = registrationRepository.findByStudentId(studentId);
        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(registrationDTOs, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "duration,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort[0]).descending());

        Page<CourseEntity> coursePage = courseRepository.findAll(pageable);

        // Chuyển đổi Entity sang DTO
        List<CourseDTO> courseDTOs = coursePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
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

    // Helper method to convert Entity to DTO
    private CourseDTO convertToDTO(CourseEntity course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDuration(course.getDuration());
        return courseDTO;
    }
}

