package com.careerconnect.service;

import com.careerconnect.chat.ChatClient;
import com.careerconnect.chat.OllamaChatClient;
import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;
import com.careerconnect.policy.EligibilityPolicy;
import com.careerconnect.repository.DriveRepository;
import com.careerconnect.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CareerAssistantService {

    private static final Logger logger = LoggerFactory.getLogger(CareerAssistantService.class);

    private final ChatClient chatClient;
    private final StudentRepository studentRepository;
    private final DriveRepository driveRepository;
    private final EligibilityPolicy eligibilityPolicy;

    private static final String BASE_SYSTEM_PROMPT = """
            You are a placement advisor for CareerConnect.
            Your role is to help students with placement-related questions.
            Important rules:
            - You provide guidance only. You cannot create, approve, reject, or modify applications.
            - Only use the information provided in the context. Do not make up details or database entries.
            - If asked to perform actions like approving applications, explain that you can only provide guidance.
            - Keep responses concise and helpful.
            """;

    public CareerAssistantService(ChatClient chatClient,
                                   StudentRepository studentRepository,
                                   DriveRepository driveRepository,
                                   EligibilityPolicy eligibilityPolicy) {
        this.chatClient = chatClient;
        this.studentRepository = studentRepository;
        this.driveRepository = driveRepository;
        this.eligibilityPolicy = eligibilityPolicy;
    }

    public String handleChat(String studentId, String driveId, String userMessage) {
        StringBuilder contextBuilder = new StringBuilder();

        Optional<Student> studentOpt = Optional.empty();
        if (studentId != null && !studentId.isBlank()) {
            studentOpt = studentRepository.findById(studentId);
            studentOpt.ifPresent(student -> {
                contextBuilder.append("Student Profile:\n");
                contextBuilder.append("- Name: ").append(student.getName()).append("\n");
                contextBuilder.append("- Programme: ").append(student.getProgramme()).append("\n");
                contextBuilder.append("- Graduation Year: ").append(student.getGraduationYear()).append("\n");
                contextBuilder.append("- CGPA: ").append(student.getCgpa()).append("\n");
                contextBuilder.append("- Active Backlogs: ").append(student.getActiveBacklogs()).append("\n");
                contextBuilder.append("- Skills: ").append(String.join(", ", student.getSkills())).append("\n\n");
            });
        }

        Optional<PlacementDrive> driveOpt = Optional.empty();
        if (driveId != null && !driveId.isBlank()) {
            driveOpt = driveRepository.findById(driveId);
            driveOpt.ifPresent(drive -> {
                contextBuilder.append("Drive Details:\n");
                contextBuilder.append("- Drive ID: ").append(drive.getId()).append("\n");
                contextBuilder.append("- Role: ").append(drive.getRole()).append("\n");
                contextBuilder.append("- Location: ").append(drive.getLocation()).append("\n");
                contextBuilder.append("- Package: ").append(drive.getPackageLpa()).append(" LPA\n");
                contextBuilder.append("- Required Skills: ").append(String.join(", ", drive.getRequiredSkills())).append("\n");
                contextBuilder.append("- Min CGPA: ").append(drive.getMinCgpa()).append("\n");
                contextBuilder.append("- Max Backlogs Allowed: ").append(drive.getMaxBacklogs()).append("\n");
                contextBuilder.append("- Deadline: ").append(drive.getDeadline()).append("\n\n");
            });
        }

        if (studentOpt.isPresent() && driveOpt.isPresent()) {
            EligibilityResult result = eligibilityPolicy.evaluate(studentOpt.get(), driveOpt.get());
            contextBuilder.append("Eligibility Result:\n");
            contextBuilder.append("- Eligible: ").append(result.isEligible()).append("\n");
            contextBuilder.append("- Reasons:\n");
            for (String reason : result.getReasons()) {
                contextBuilder.append("  - ").append(reason).append("\n");
            }
            contextBuilder.append("\n");
        }

        String fullUserMessage;
        if (contextBuilder.length() > 0) {
            fullUserMessage = "Context:\n" + contextBuilder.toString() + "User Question: " + userMessage;
        } else {
            fullUserMessage = "User Question: " + userMessage;
        }

        logger.info("Processing chat request (studentId={}, driveId={})", studentId, driveId);
        return chatClient.chat(BASE_SYSTEM_PROMPT, fullUserMessage);
    }

    public String getModelName() {
        if (chatClient instanceof OllamaChatClient ollamaClient) {
            return ollamaClient.getModel();
        }
        return "unknown";
    }
}
