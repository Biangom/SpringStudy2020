package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
// (1) readOnly면 saveItem이 저장이 안된다
//readOnly니까
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    // (2) 그래서 Overridding해줬다.
    // 결과적으로 saveItem은 DB에 저장할 수가 있게된다.
    // Anootation Overriding은 메서드에 우선권이 있다.
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }


}

// 실제 개발할 때는 테스트를 작성하겠지만
// 강의에선 생략하겠다.