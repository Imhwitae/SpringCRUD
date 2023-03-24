package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor  // lombok이 제공하는 어노테이션으로 생성자 주입을 생략해준다.
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")  // 같은 URL이지만 GET방식으로 들어오면 작성폼으로 들어가게끔 구분
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")  // 위와 같은 URL이지만 POST방식으로 들어오면 저장하게끔 구분
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model)
    {
        Item item = new Item();  // 생성자에 직접 넣어도 된다.
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);  // model에 item이라는 이름으로 item객체 저장

        return "basic/item";
    }

//    @PostMapping("/add")  // 중복 매핑을 막기 위해 주석처리
    public String addItemV2(@ModelAttribute("item") Item item) {
        /* @ModelAttribute가 자동으로
            Item item = new Item();
            item.setItemName(itemName);
            item.setPrice(price);
            item.setQuantity(quantity);
            이렇게 set호출을 해서 만들어준다.
         */
        itemRepository.save(item);
//        model.addAttribute("item", item);  // 자동 추가. 생략 가능

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {  // @ModelAttribute의 속성값을 생략할 수 있다.
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {  // @ModelAttribute까지 생략할 수 있다. (객체인 경우에만 @ModelAttribute가 호출)
        itemRepository.save(item);
        return "basic/item";
    }

//    상품 새로고침 문제 해결 버전
//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

//    화면에 '저장되었습니다' 출력
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.updateItem(itemId, item);
        return "redirect:/basic/items/{itemId}";  // 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
    }


//    테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}


