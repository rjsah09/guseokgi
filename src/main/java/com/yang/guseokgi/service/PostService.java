package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Img;
import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.Report;
import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import com.yang.guseokgi.dto.post.*;
import com.yang.guseokgi.repository.AccountRepository;
import com.yang.guseokgi.repository.ImgRepository;
import com.yang.guseokgi.repository.PostRepository;
import com.yang.guseokgi.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final ImgRepository imgRepository;

    public Page<PostPreview> findAll(Pageable pageable) {
        return findWithThumbnail(postRepository.findAll(pageable).map(PostPreview::new));
    }

    public Long save(PostWrite postWrite, Long id) throws IOException {
        Post post = new Post(accountRepository.findById(id).get(), postWrite.getTitle(), postWrite.getArticle(), postWrite.getPostCategory());
        Long postId;

        if(!postWrite.getFiles().isEmpty()) {
            List<Img> imgs = parseImg(postWrite.getFiles(), post);
            postId = postRepository.save(post).getId();
            for(Img img : imgs) {
                img.setTargetPost(post);
            }
            imgRepository.saveAll(imgs);
        } else {
            postId = postRepository.save(post).getId();
        }

        return postId;
    }

    public List<Img> parseImg(List<MultipartFile> files, Post post) throws IOException {
        List<Img> imgs = new ArrayList<>();

        for(MultipartFile multipartFile : files) {
            Img img = new Img();

            String sourceFileName = multipartFile.getOriginalFilename();
            String sourceFileNameExtension = "." + FilenameUtils.getExtension(sourceFileName).toLowerCase();

            File destinationFile;
            String destinationFileName = System.nanoTime() + sourceFileNameExtension;
            if(!(sourceFileNameExtension.equals(".png") || sourceFileNameExtension.equals(".jpg") || sourceFileNameExtension.equals(".gif")))
                continue;
            String fileUrl = "C:/guseokgiImages/";

            do {
                destinationFile = new File(fileUrl + destinationFileName);
            } while(destinationFile.exists());

            destinationFile.getParentFile().mkdirs();
            multipartFile.transferTo(destinationFile);

            img.setFileName(destinationFileName);
            img.setFileOriName(sourceFileName);
            img.setFileUrl(fileUrl);
            img.setTargetPost(post);

            imgs.add(img);
        }

        return imgs;
    }

    public void updateViews(Long id) {
        Post findPost = postRepository.findById(id).get();
        findPost.setViews(findPost.getViews() + 1);
        findPost.setDailyViews(findPost.getDailyViews() + 1);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public PostBasic findPostBasicById(Long id) {
        Optional<PostBasic> postBasicOpt = postRepository.findById(id).map(PostBasic::new);
        if(postBasicOpt.isEmpty()) {
            return null;
        }

        PostBasic postBasic = postBasicOpt.get();
        List<String> list = imgRepository.findListByPostId(id);
        List<String> newList = new ArrayList<>();
        for(String eachImg : list) {
            eachImg = "/image/" + eachImg;
            newList.add(eachImg);
        }
        postBasic.setImgList(newList);

        return postBasic;
    }

    public Optional<PostUpdate> findPostBasicUpdateById(Long id) {
        return postRepository.findById(id).map(PostUpdate::new);
    }

    public List<PostIdAndTitle> findNotCompleted(Long id) {
        return postRepository.findByIdNotCompleted(id);
    }

    //카테고리별 목록
    public Page<PostPreview> findByPostCategory(Pageable pageable, PostCategory postCategory) {
        return findWithThumbnail(postRepository.findByPostCategory(pageable, postCategory).map(PostPreview::new));
    }

    //카테고리 내 검색
    public Page<PostPreview> findByPostCategoryAndKeyword(Pageable pageable, PostCategory postCategory, String keyword) {
        return findWithThumbnail(postRepository.findByPostCategoryAndKeyword(pageable, postCategory, keyword).map(PostPreview::new));
    }

    //모든 카테고리 내 검색
    public Page<PostPreview> findAllByKeyword(Pageable pageable, String keyword) {
        return findWithThumbnail(postRepository.findAllByKeyword(pageable, keyword).map(PostPreview::new));
    }

    //계정 물품
    public Page<PostPreview> findAllById(Pageable pageable, long accountId) {
        return findWithThumbnail(postRepository.findAllByAccountId(pageable, accountId).map(PostPreview::new));
    }

    //계정물품 상태
    public Page<PostPreview> findAllByIdAndStatus(Pageable pageable, long accountId, PostStatus postStatus) {
        return findWithThumbnail(postRepository.findAllByAccountIdAndPostStatus(pageable, accountId, postStatus).map(PostPreview::new));
    }

    //작성자 물품 검색
    public Page<PostPreview> findWriterPost(Pageable pageable, long accountId) {
        return findWithThumbnail(postRepository.findWriterPost(pageable, accountId).map(PostPreview::new));
    }

    //오늘의 인기글
    public Page<PostPreview> findToday(Pageable pageable) {
        return findWithThumbnail(postRepository.findToday(pageable).map(PostPreview::new));
    }

    public Page<PostBasicManager> findAllManager(Pageable pageable) {
        Page<PostBasicManager> list =  postRepository.findAllManager(pageable).map(PostBasicManager::new);
        for(PostBasicManager pbm : list) {
            List<String> imgList = imgRepository.findListByPostId(pbm.getId());
            List<String> newImgList = new ArrayList<>();
            for(String eachImg : imgList) {
                eachImg = "/image/" + eachImg;
                newImgList.add(eachImg);
            }
            pbm.setImgList(newImgList);

        }

        return list;
    }

    public Page<PostBasicManager> findAllByKeywordManager(Pageable pageable, String keyword) {
        return postRepository.findAllByKeywordManager(pageable, keyword).map(PostBasicManager::new);
    }

    public Optional<PostNicknameAndTitle> findNicknameAndTitleById(Long postId) {
        return postRepository.findNicknameAndTitleById(postId);
    }

    //글 삭제
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        if(!post.isDeleted()) {
            post.setDeleted(true);
        }
    }

    //썸네일을 포함한 PostPreview 반환
    public Page<PostPreview> findWithThumbnail(Page<PostPreview> page) {
        for(PostPreview pb: page) {
            Long postId = pb.getId();
            List<String> imgList = imgRepository.findListByPostId(postId);

            if(!imgList.isEmpty()) {
                String thumbnail = "/image/" + imgList.get(0);
                pb.setThumbnail(thumbnail);
            }
        }

        return page;
    }

    public Object findTradedByAccountId(Long accountId) {
        return postRepository.findTradedByAccountId(accountId);
    }

    public Object findTradingByAccountId(long accountId) {
        return postRepository.findTradingByAccountId(accountId);
    }

    public Object findTradedByPostId(long postId) {
        Post post = postRepository.findById(postId).get();
        return postRepository.findTradedByAccountId(post.getAccount().getId());
    }

    public void postUpdate(Long postId, PostUpdate postUpdate) throws IOException {
        Post post = postRepository.findById(postId).get();
        post.setTitle(postUpdate.getTitle());
        post.setArticle(postUpdate.getArticle());
        post.setPostCategory(postUpdate.getPostCategory());

        if(postUpdate.isFileUpdate()) {
            imgRepository.deleteAllByPostId(postId);
            if(!postUpdate.getFiles().isEmpty()) {
                List<Img> imgs = parseImg(postUpdate.getFiles(), post);
                postId = postRepository.save(post).getId();
                for(Img img : imgs) {
                    img.setTargetPost(post);
                }
                imgRepository.saveAll(imgs);
            } else {
                postId = postRepository.save(post).getId();
            }
        }
    }
}
