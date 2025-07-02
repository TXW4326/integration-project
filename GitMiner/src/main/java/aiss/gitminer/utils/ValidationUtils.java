package aiss.gitminer.utils;

import aiss.gitminer.exception.BadRequestException;

public final class ValidationUtils {

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
}
