package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Favorite;
import com.yang.guseokgi.domain.Img;
import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.dto.favorite.FavoritePreview;
import com.yang.guseokgi.dto.post.PostPreview;
import com.yang.guseokgi.repository.AccountRepository;
import com.yang.guseokgi.repository.FavoriteRepository;
import com.yang.guseokgi.repository.ImgRepository;
import com.yang.guseokgi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final FavoriteRepository favoriteRepository;
    private final ImgRepository imgRepository;

    //즐겨찾기 등록
    public void save(Long postId, Long accountId) {
        Post post = postRepository.findById(postId).get();
        Account account = accountRepository.findById(accountId).get();

        if(post.getAccount().getId() != accountId) {
            Favorite favorite = new Favorite(post, account);
            favoriteRepository.save(favorite);
        }
    }

    //즐겨찾기 개수 반환
    public Long findCountByAccountId(Long accountId) {
        return favoriteRepository.findCountByAccountId(accountId);
    }

    //즐겨찾기 해제
    public void deleteByPostIdAndAccountId(Long postId, Long accountId) {
        Post post = postRepository.findById(postId).get();

        if(post.getAccount().getId() != accountId) {
            favoriteRepository.deleteByPostIdAndAccountId(postId, accountId);
        }
    }

    //나의 즐겨찾기 리스트 반환
    public List<FavoritePreview> findPostPreviewByAccountId(Long accountId) {
        return findWithThumbnail(favoriteRepository.findPostPreviewByAccountId(accountId));
    }

    public void deleteById(Long notificationId) {
        favoriteRepository.deleteById(notificationId);
    }

    public Long findCountByPostIdAndAccountId(Long postId, Long accountId) {
        return favoriteRepository.findCountByPostIdAndAccountId(postId, accountId);
    }

    public List<FavoritePreview> findWithThumbnail(List<FavoritePreview> list) {
        for(FavoritePreview fb: list) {
            Long postId = fb.getPostId();
            List<String> imgList = imgRepository.findListByPostId(postId);

            if(!imgList.isEmpty()) {
                String thumbnail = "/image/" + imgList.get(0);
                fb.setThumbnail(thumbnail);
            }
        }

        return list;
    }
}
