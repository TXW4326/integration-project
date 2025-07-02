package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Comment;
import aiss.bitbucketminer.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BitbucketCommentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final BitbucketUserMapper userMapper = new BitbucketUserMapper();

    public List<Comment> fetchComments(String workspace, String repo, String issueId) {
        List<Comment> comments = new ArrayList<>();
        String url = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo + "/issues/" + issueId + "/comments";

        Map response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> values = (List<Map<String, Object>>) response.get("values");
        for (Map<String, Object> value : values) {
            Comment comment = new Comment();
            comment.setId(String.valueOf(value.get("id")));

            Map<String, Object> content = (Map<String, Object>) value.get("content");
            if (content != null) comment.setBody((String) content.get("raw"));

            comment.setCreatedAt((String) value.get("created_on"));
            comment.setUpdatedAt((String) value.get("updated_on"));

            Map<String, Object> user = (Map<String, Object>) value.get("user");
            if (user != null) {
                User author = userMapper.mapUser(user);
                comment.setAuthor(author);
            }

            comments.add(comment);
        }
        return comments;
    }
}
