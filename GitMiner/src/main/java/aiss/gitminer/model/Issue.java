package aiss.gitminer.model;

import aiss.gitminer.exception.BadRequestException;
import aiss.gitminer.utils.GeneralOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "Issue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        description = "Represents an issue in a project"
)
public class Issue {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    @Schema(hidden = true)
    private Project project;


    @Id
    @JsonProperty("id")
    @NotNull(message = "Issue ID cannot be null")
    @NotBlank(message = "Issue ID should not be empty")
    @Schema(
            description = "Unique identifier for the issue",
            example = "I_kwDOABGHUc6-VYdM",
            minLength = 1,
            required = true
    )
    private String id;

    @JsonProperty("title")
    @NotNull(message = "Issue title cannot be null")
    @NotEmpty(message = "Issue title cannot be empty")
    @Schema(
            description = "Title of the issue",
            example = "Remove obsolete `update_copyright_headers.sh`",
            minLength = 1,
            required = true
    )
    private String title;

    @JsonProperty("description")
    @Column(columnDefinition="TEXT")
    @Size(min = 1, message = "Issue description cannot be empty")
    @Schema(
            description = "Description of the issue",
            example = "This script is no longer used and should be removed.",
            required = false
    )
    private String description;

    @JsonProperty("state")
    @NotNull(message = "Issue state cannot be null")
    @NotBlank(message = "Issue state cannot be empty")
    @Schema(
            description = "State of the issue (e.g., open, closed)",
            example = "open",
            minLength = 1,
            required = true
    )
    private String state;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Issue created date cannot be null")
    @Past(message = "Issue created date must be in the past")
    @Schema(
            description = "Date and time when the issue was created",
            example = "2023-10-01T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            required = true,
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Issue updated date cannot be null")
    @Past(message = "Issue updated date must be in the past")
    @Schema(
            description = "Date and time when the issue was last updated",
            example = "2023-10-02T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            required = true,
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime updatedAt;

    @JsonProperty("closed_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Past(message = "Issue closed date must be in the past")
    @Schema(
            description = "Date and time when the issue was closed",
            example = "2023-10-03T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime closedAt;

    @JsonProperty("labels")
    @ElementCollection
    @NotNull(message = "Issue label list cannot be null")
    @UniqueElements(message = "Issue label list should not contain duplicates")
    @ArraySchema(
            schema = @Schema(minLength = 1),
            arraySchema = @Schema(
                    description = "List of labels associated with the issue",
                    example = "[\"bug\", \"enhancement\", \"documentation\"]",
                    required = true
            ),
            uniqueItems = true
    )
    private List<@NotNull(message = "Issue label cannot be null") String> labels;

    @JsonProperty("author")
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    @OneToOne(cascade=CascadeType.ALL)
    @Valid
    private User author;

    @JsonProperty("assignee")
    @JoinColumn(name = "assignee_id",referencedColumnName = "id")
    @OneToOne(cascade=CascadeType.ALL)
    @Valid
    private User assignee;

    @JsonProperty("votes")
    @Min(value = 0, message = "Votes must be a non-negative integer")
    @NotNull(message = "Issue votes cannot be null")
    @Schema(
            description = "Number of votes for the issue",
            example = "5",
            required = true
    )
    private Integer votes;

    @JsonProperty("comments")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "issueId")
    @Valid
    @NotNull(message = "Issue comment list cannot be null")
    @UniqueElements(message = "Issue comment ids should be unique")
    @ArraySchema(
            schema = @Schema(implementation = Comment.class),
            arraySchema = @Schema(
                    description = "List of comments on the issue",
                    required = true
            ),
            uniqueItems = true
    )
    private List<@NotNull(message = "Issue comments cannot be null") Comment> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public List<aiss.gitminer.model.Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Issue.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? "<null>" : this.description));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null) ? "<null>" : this.state));
        sb.append(',');
        sb.append("createdAt");
        sb.append('=');
        sb.append(((this.createdAt == null) ? "<null>" : this.createdAt));
        sb.append(',');
        sb.append("updatedAt");
        sb.append('=');
        sb.append(((this.updatedAt == null) ? "<null>" : this.updatedAt));
        sb.append(',');
        sb.append("closedAt");
        sb.append('=');
        sb.append(((this.closedAt == null) ? "<null>" : this.closedAt));
        sb.append(',');
        sb.append("labels");
        sb.append('=');
        sb.append(((this.labels == null) ? "<null>" : this.labels));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null) ? "<null>" : this.author));
        sb.append(',');
        sb.append("assignee");
        sb.append('=');
        sb.append(((this.assignee == null) ? "<null>" : this.assignee));
        sb.append(',');
        sb.append("votes");
        sb.append('=');
        sb.append(((this.votes == null) ? "<null>" : this.votes));
        sb.append(',');
        sb.append("comments");
        sb.append('=');
        sb.append(((this.comments == null) ? "<null>" : this.comments));
        sb.append(',');

        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Project _project)) return false;
        return Objects.equals(getId(), _project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


    private enum OrderBy {
        ID("id"),
        TITLE("title"),
        CREATED_AT("createdAt"),
        UPDATED_AT("updatedAt"),
        VOTES("votes");

        private final String value;
        private static final String validValues = Stream.of(OrderBy.values()).map(orderBy -> orderBy + ", -" + orderBy).collect(Collectors.joining(", ", "{ ", " }"));

        OrderBy(String value) {
            this.value = value;
        }

        public static OrderBy of(String name) {
            return Stream.of(OrderBy.values()).filter(e -> e.value.equals(name)).findFirst().orElseThrow(()->
                    new BadRequestException("Invalid issueOrderBy value: " + name + ", expected one of: " + validValues)
            );
        }


        @Override
        public String toString() {
            return value;
        }
    }

    public record Order(OrderBy orderBy, boolean ascending) implements GeneralOrder {
        public Order(String order) {
            this(
                    order.charAt(0) == '-' ? OrderBy.of(order.substring(1)) : OrderBy.of(order),
                    order.charAt(0) != '-'
            );
        }

        public String getOrderBy() {
            return orderBy.toString();
        }
    }




}
