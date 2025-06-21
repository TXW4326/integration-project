
package aiss.githubminer.model;

import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {

    @JsonProperty("endCursor")
    private String endCursor;

    @JsonProperty("hasNextPage")
    private boolean hasNextPage;

    @JsonProperty("endCursor")
    public String getEndCursor() {
        return endCursor;
    }

    @JsonProperty("endCursor")
    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    @JsonProperty("hasNextPage")
    public boolean isHasNextPage() {
        return hasNextPage;
    }

    @JsonProperty("hasNextPage")
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("endCursor", endCursor)
                .append("hasNextPage", hasNextPage)
                .toString();
    }

}
