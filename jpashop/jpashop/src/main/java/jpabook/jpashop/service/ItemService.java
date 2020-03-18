package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    // (2) 방법이 더 났다.
    public Item updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        return findItem;
    }
//    (1)
//    public void updateItem(Long itemId, Book bookParam) {
//        Item findItem = itemRepository.findOne(itemId);
//        findItem.setPrice(bookParam.getPrice());
//        findItem.setName(bookParam.getName());
//        findItem.setStockQuantity(bookParam.getStockQuantity());
//        // 나머지 필드를 채웠다 치고.
//        // 아래는 아무것도 할 필요 없음.
//        // 이 라인이 끝나면 커밋이됨.
//        // @Transactional에 의해 커밋이됨.
//        // 커밋이 되면 JPA는 flush를 날림.
//        // 영속성 컨텍스트 중에 변경된애를 찾아서.
//        // 바뀐값을 업데이트 해버림.
//
//        //itemRepository.save(findItem)
//
//        // (1) merge 방법
//        // 아래의 코드와 완전히 같음.
//        // return findItem;
//
//
//    }

}

// 실제 개발할 때는 테스트를 작성하겠지만
// 강의에선 생략하겠다.