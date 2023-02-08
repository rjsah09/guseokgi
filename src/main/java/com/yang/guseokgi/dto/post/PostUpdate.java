package com.yang.guseokgi.dto.post;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.category.PostCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdate {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String article;

    @NotNull(message = "카테고리를 선택해주세요")
    private PostCategory postCategory;

    private boolean fileUpdate;

    private List<MultipartFile> files;

    private boolean deleted;

    public PostUpdate(Post post) {
        this.title = post.getTitle();
        this.article = post.getArticle();
        this.postCategory = post.getPostCategory();
        this.deleted = post.isDeleted();
    }

}
