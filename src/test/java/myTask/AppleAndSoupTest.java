package myTask;

import static myTask.Constants.ItemName.APPLE;
import static myTask.Constants.ItemName.BREAD;
import static myTask.Constants.ItemName.MILK;
import static myTask.Constants.ItemName.SOUP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppleAndSoupTest {
	ArrayList<StockItems> stockItemsDB;
	  RegisterCalculator registerCalculator;
	  ItemDiscount itemDiscount;
	  ArrayList<StockItems> purchasedItems;

	  @BeforeEach
	  void setUp() {
	    ItemRepository itemRepository = new ItemRepository();
	    stockItemsDB = itemRepository.findAll();
	    itemDiscount = new ItemDiscount(itemRepository);
	    registerCalculator = new RegisterCalculator(itemDiscount);
	    purchasedItems = new ArrayList<>();
	  }

	  @Test
	  void threeSoup_twoLoavesBread_withDiscount_purchasedToday() {
	    addToPurchasedItems(SOUP, 3);
	    addToPurchasedItems(BREAD, 2);
	    double baseTotalPrice = 3.55;
	    double expectdDiscountedPrice = 3.15;
	    LocalDate dateToday = LocalDate.now();

	    double discountedPrice = itemDiscount.twoSoupGetOneLoafBreadHalfOff(purchasedItems, baseTotalPrice, dateToday);

	    assertEquals(expectdDiscountedPrice, discountedPrice);
	  }
	  
	@Test
	  void sixApples_and_oneBottleOfMilk_noDiscount_purchasedToday() {
	    addToPurchasedItems(APPLE, 6);
	    addToPurchasedItems(MILK, 1);
	    double baseTotalPrice = 1.90;
	    double baseTotalPriceWithoutDiscount = 1.90;
	    LocalDate dateToday = LocalDate.now();

	    double discountedPrice = itemDiscount.applyAppleTenPercentDiscount(purchasedItems, baseTotalPriceWithoutDiscount, dateToday);

	    assertEquals(baseTotalPriceWithoutDiscount, discountedPrice);
	  }

	@Test
	  void sixApples_and_oneBottleOfMilk_withDiscount_purchasedWithinThreeDaysFromToday() {
	    addToPurchasedItems(APPLE, 6);
	    addToPurchasedItems(MILK, 1);
	    double baseTotalPrice = 1.90;
	    double expectedDiscountedPrice = 1.84;
	    LocalDate dateToday = LocalDate.now().plusDays(3);

	    double discountedPrice = itemDiscount.applyAppleTenPercentDiscount(purchasedItems, baseTotalPrice, dateToday);

	    assertEquals(expectedDiscountedPrice, discountedPrice);
	  }

	@Test
	  void threeApples_twoSoup_oneBread_purchasedFiveDaysFromToday() {
	    addToPurchasedItems(APPLE, 3);
	    addToPurchasedItems(SOUP, 2);
	    addToPurchasedItems(BREAD, 1);
	    double baseTotalPrice = 1.97;
	    LocalDate dateToday = LocalDate.now().plusDays(5);

	    double total = registerCalculator.tallyTotalForPurchasedItems(purchasedItems, dateToday);

	   assertEquals(baseTotalPrice, total);
	   
	  } 

	  private void addToPurchasedItems(Constants.ItemName itemName, Integer howManyToAdd) {
	    StockItems item = stockItemsDB.get(itemName.getValue());
	    for (int i = 0; i < howManyToAdd; i++) {
	      purchasedItems.add(item);
	    }
	  }
}
