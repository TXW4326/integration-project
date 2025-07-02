package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.Objects;

@JsonPropertyOrder({"id","title","message",
        "author_name","author_email","authored_date",
        "web_url"})
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Commit {

    // 1. id
    @JsonProperty("id")
    private String id;

    @JsonProperty("id")
    public String getHash() {
        return id;
    }

    @JsonProperty("hash")
    public String setHash(String id) {
        return this.id = id;
    }

    // 2. title
    @JsonProperty("title")
    private String title;

    // 3. message
    @JsonProperty("message")
    private String message;

    @JsonProperty("message")
    public void setMessage(String message){
        String[] lines = message.split("\\n\\n",2);
        this.title = lines[0];
        if(lines.length == 2){
            if (lines[1].isEmpty()){
                this.message = "No message";
            } else {
                this.message = lines[1].replace("-","");
            }
        } else {
            this.message = "No message";
        }
    }

    // Para serializar author_name y author_email, ocultamos el objeto Raw
    @JsonIgnore
    private Raw author;

    @JsonIgnore
    public Raw getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Raw author) {
        this.author = author;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class Raw {
        @JsonProperty("author_name")
        private String author_name;

        @JsonProperty("author_email")
        private String author_email;

        @JsonProperty("raw")
        public void setRaw(String cadena){
            String[] parts = cadena.split("<", 2);
            this.author_name = parts[0].trim();
            this.author_email = parts[1].replace(">","").trim();
        }
    }

    // 4. author_name
    @JsonProperty("author_name")
    public String getAuthor_name() {
        if (Objects.isNull(this.author)) return null;
        return this.author.author_name;
    }

    // 5. author_email
    @JsonProperty("author_email")
    public String getAuthor_email() {
        if (Objects.isNull(this.author)) return null;
        return this.author.author_email;
    }

    // 6. authored_date
    @JsonAlias("date")
    @JsonProperty("authored_date")
    private String authored_date;

    // Ocultamos links para que no salga en JSON
    @JsonIgnore
    private Links links;

    @JsonIgnore
    public Links getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class Links {
        @JsonProperty("html")
        private Html html;

        @JsonProperty("html")
        public void setHtml(Html html) {
            this.html = html;
        }

        @Data
        static class Html {
            @JsonProperty("href")
            private String href;

            @JsonProperty("href")
            public void setHref(String href) {
                this.href = href;
            }
        }
    }

    // 7. web_url (derivada de links.html.href)
    @JsonProperty("web_url")
    public String getWeb_url() {
        return links != null && links.getHtml() != null ? links.getHtml().getHref() : null;
    }

    public void setWeb_url(String href) {
        if (links == null) links = new Links();
        if (links.getHtml() == null) links.setHtml(new Links.Html());
        links.getHtml().setHref(href);
    }

}
