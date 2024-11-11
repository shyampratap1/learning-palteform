package com.Learning.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Learning.Entity.Exam;

public interface ExamRepo extends JpaRepository<Exam, Long>{

}
