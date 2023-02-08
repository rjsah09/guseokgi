package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Inform;
import com.yang.guseokgi.dto.Inform.InformBasic;
import com.yang.guseokgi.dto.Inform.InformPreview;
import com.yang.guseokgi.dto.Inform.InformWrite;
import com.yang.guseokgi.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InformService {

    private final InformRepository informRepository;

    //공지 조회
    public Page<InformBasic> findPageAll(Pageable pageable) {
        return informRepository.findPageAll(pageable).map(InformBasic::new);
    }

    //공지 작성
    public Long save(String title, String article) {
        Inform inform = new Inform(title, article);
        return informRepository.save(inform).getId();
    }

    //공지 수정
    public void update(Long informId, String title, String article) {
        Inform inform = informRepository.findById(informId).get();
        inform.setTitle(title);
        inform.setArticle(article);
    }

    public InformWrite findInformWriteById(Long informId) {
        return informRepository.findInformWriteById(informId).get();
    }

    public void deleteById(Long informId) {
        informRepository.deleteById(informId);
    }

    public Page<InformPreview> findInformPreviewAll(Pageable pageable) {
        return informRepository.findInformPreviewAll(pageable).map(InformPreview::new);
    }

    public InformBasic findInformBasicById(Long informId) {
        return informRepository.findInformBasicById(informId).get();
    }
}
