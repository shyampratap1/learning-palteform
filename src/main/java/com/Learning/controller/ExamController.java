package com.Learning.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Learning.Entity.Exam;
import com.Learning.Entity.Question;
import com.Learning.userRepo.ExamRepo;
import com.Learning.userRepo.QuestionRepo;

@Controller
public class ExamController {

	@Autowired
	private ExamRepo examRepository;

	@Autowired
	private QuestionRepo questionRepository;

	@GetMapping("/exams")
	public String getExams(Model model) {
		List<Exam> exams = examRepository.findAll();
		model.addAttribute("exams", exams);
		return "Exam";
	}

	@GetMapping("/questions/{examId}")
	public String getQuestionsByExam(@PathVariable Long examId, Model model) {
		Exam exam = examRepository.findById(examId).orElse(null);
		List<Question> questions = questionRepository.findByExam(exam);
		model.addAttribute("exam", exam);
		model.addAttribute("questions", questions);
		return "Question";
	}

	// Controller Method to Show Create Question Form with Exam ID
	@GetMapping("/createQuestion/{examId}")
	public String showCreateQuestionForm(@PathVariable Long examId, Model model) {
		model.addAttribute("examId", examId);
		model.addAttribute("question", new Question());
		return "createQuestion";
	}

	// Controller Method to Handle Create Question Form Submission
	@PostMapping("/createQuestion")
	public String createQuestion(@ModelAttribute Question question, @RequestParam Long examId) {
		Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Invalid exam ID"));
		question.setExam(exam);
		questionRepository.save(question);
		return "redirect:/questions/" + exam.getId(); // Corrected redirect
	}

	// Show the form to create a new exam
	@GetMapping("/createExam")
	public String showCreateExamForm(Model model) {
		model.addAttribute("exam", new Exam());
		return "createExam";
	}

	// Handle the submission of the new exam form
	@PostMapping("/createExam")
	public String createExam(@ModelAttribute Exam exam) {
		examRepository.save(exam);
		return "redirect:/exams";
	}
	// display delete functionality

	@GetMapping("/deleteQuestions/{examId}")
	public String getDeleteQuestionsPage(@PathVariable Long examId, Model model) {
		Exam exam = examRepository.findById(examId).orElse(null);
		List<Question> questions = questionRepository.findByExam(exam);
		model.addAttribute("exam", exam);
		model.addAttribute("questions", questions);
		return "deleteQuestion";
	}

	@PostMapping("/deleteQuestion")
	public String deleteQuestion(@RequestParam Long questionId, @RequestParam Long examId) {
		questionRepository.deleteById(questionId);
		return "redirect:/deleteQuestion/" + examId;
	}

	// Display the update question form
	@GetMapping("/updateQuestion/{questionId}/{examId}")
	public String getUpdateQuestionPage(@PathVariable Long questionId, @PathVariable Long examId, Model model) {
		Question question = questionRepository.findById(questionId).orElse(null);
		if (question != null) {
			model.addAttribute("question", question);
			model.addAttribute("examId", examId);
		}
		return "updateQuestion";
	}

	// Handle the update question form submission
	@PostMapping("/updateQuestion")
	public String updateQuestion(@ModelAttribute Question question, @RequestParam Long examId) {
		questionRepository.save(question);
		return "redirect:updateQuestion/" + examId;
	}
	   

	    @PostMapping("/upload")
	    public String uploadExam(@RequestParam("image") MultipartFile image,
	                             @RequestParam("title") String title) {
	        // Define the path to save the image
	        String imagePath = "src/main/resources/static/images/" + image.getOriginalFilename();

	        try {
	            // Save the image to the specified path
	            image.transferTo(new File(imagePath));

	            // Create and save the exam entity
	            Exam exam = new Exam();
	            exam.setTitle(title);
	            exam.setImage("/images/" + image.getOriginalFilename()); // Store the relative path
	            examRepository.save(exam);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return "redirect:/exams"; // Redirect to the exams page after upload
	    }
	
}
