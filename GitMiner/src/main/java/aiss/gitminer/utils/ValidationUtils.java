package aiss.gitminer.utils;

import aiss.gitminer.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ValidationUtils {

    private static final List<String> errors  = new ArrayList<>();

    private ValidationUtils() {}

    public static void validateProjectId(String id) {
        if (id == null) throw new BadRequestException("Project ID cannot be null");
        if (id.isBlank()) throw new BadRequestException("Project ID cannot be empty");
    }

    public static void validateUserId(String id) {
        if (id == null) throw new BadRequestException("User ID cannot be null");
        if (id.isBlank()) throw new BadRequestException("User ID cannot be empty");
    }

    public static void validateIssueId(String id) {
        if (id == null) throw new BadRequestException("Issue ID cannot be null");
        if (id.isBlank()) throw new BadRequestException("Issue ID cannot be empty");
    }

    public static void validateCommentId(String id) {
        if (id == null) throw new BadRequestException("Comment ID cannot be null");
        if (id.isBlank()) throw new BadRequestException("Comment ID cannot be empty");
    }

    public static void validateCommitId(String id) {
        if (id == null) throw new BadRequestException("Commit ID cannot be null");
        if (id.isBlank()) throw new BadRequestException("Commit ID cannot be empty");
        if (id.length() != 40) throw new BadRequestException("Commit ID must be a 40-character SHA-1 hash");
    }

    public static void validateIssueState(String state) {
        if (state == null) throw new BadRequestException("Issue state cannot be null");
        if (state.isBlank()) throw new BadRequestException("Issue state cannot be empty");
    }

    public static void validateIssuePage(int page) {
        if (page < 0) throw new BadRequestException("Issue page number cannot be negative");
    }

    public static void validateIssuePageSize(int size) {
        if (size <= 0) throw new BadRequestException("Issue page size must be a positive integer");
    }

    public static void validateCommitPage(int page) {
        if (page < 0) throw new BadRequestException("Commit page number cannot be negative");
    }
    public static void validateCommitPageSize(int size) {
        if (size <= 0) throw new BadRequestException("Commit page size must be a positive integer");
    }

    public static void validateCommentPage(int page) {
        if (page < 0) throw new BadRequestException("Comment page number cannot be negative");
    }
    public static void validateCommentPageSize(int size) {
        if (size <= 0) throw new BadRequestException("Comment page size must be a positive integer");
    }

    public static void validateProjectPage(int page) {
        if (page < 0) throw new BadRequestException("Project page number cannot be negative");
    }
    public static void validateProjectPageSize(int size) {
        if (size <= 0) throw new BadRequestException("Project page size must be a positive integer");
    }



    public static <T> void accumulateValidation(Consumer<T> validationFunction, T input) {
        if (input == null) return;
        try {
            validationFunction.accept(input);
        } catch (BadRequestException e) {
           errors.add(e.getMessage());
        }
    }

    public static <T, R> R accumulateValidationReturn(Function<T,R> validationFunction, T input) {
        try {
            return validationFunction.apply(input);
        } catch (BadRequestException e) {
            errors.add(e.getMessage());
        }
        return null;
    }


    public static void throwIfErrors() {
        if (errors.isEmpty()) return;
        List<String> errorMessages = new ArrayList<>(errors);
        clearErrors(); // Clear errors after collecting them
        throw new BadRequestException("User input errors were found", errorMessages);
    }

    public static void clearErrors() {
        errors.clear();
    }
}
