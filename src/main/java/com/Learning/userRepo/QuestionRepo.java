package com.Learning.userRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Learning.Entity.Exam;
import com.Learning.Entity.Question;


public interface QuestionRepo extends JpaRepository<Question, Long>{

	List<Question> findByExam(Exam exam);
}
