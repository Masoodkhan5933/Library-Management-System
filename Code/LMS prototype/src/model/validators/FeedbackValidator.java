package model.validators;
import model.dto.FeedbackDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;
import model.dto.FeedbackResponse;

public class FeedbackValidator {
    public void validateFeedback(FeedbackDTO feedbackDTO, FeedbackResponse response) {
        if (feedbackDTO == null) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Invalid feedback entry.", MessageType.ERROR));
        } else {
            if (feedbackDTO.getUsername() == null || feedbackDTO.getUsername().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Username is required.", MessageType.WARNING));
            }
            if (feedbackDTO.getFeedback() == null || feedbackDTO.getFeedback().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Feedback content is required.", MessageType.WARNING));
            }
        }
    }
}




