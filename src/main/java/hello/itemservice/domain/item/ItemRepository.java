package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // 동시에 여러 쓰레드가 접근할 때에는 일반 HashMap말고 ConcurrentHashMap<>();을 사용해야 한다.
    private static final Map<Long, Item> store = new HashMap<>();  // static

    private static long sequence = 0L;  // static

    public Item save(Item item) {

        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());  // 실제 store에 영향이 가지 않게 ArrayList로 한 번 감싼다.
    }

    // 실제로는 아래 메서드에서 get, setId를 사용하지 않기때문에 updateParam의 클래스를 하나 만들어서 ItemName, Price, Quantity를 따로 보관한다
    public void updateItem(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
