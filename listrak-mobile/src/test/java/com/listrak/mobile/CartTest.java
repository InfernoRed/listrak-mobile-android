package com.listrak.mobile;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.util.collections.Iterables;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

/**
 * Created by Pam on 5/1/2017.
 */
public class CartTest extends BaseUnitTest {
    @Test
    public void getItems_returnsNotNullCollection() {
        Collection<CartItem> items = Cart.getItems();
        assertNotNull(items);
    }

    @Test
    public void addItem_withOnlyValidArgs_callsService() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        verify(mockListrackService).updateCart(ArgumentMatchers.<CartItem>anyCollection());
    }

    @Test
    public void addItem_withOnlyValidArgs_incrementsCollectionSize() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        Collection<CartItem> items = Cart.getItems();
        assertEquals(1, items.size());
    }

    @Test
    public void addItem_withImageUrl_includesUrlInCartItem() throws UnsupportedEncodingException, InstantiationException {
        String imageUrl = "imageUrl";

        Cart.addItem("sku", 1, 1.0, "title", imageUrl, null);

        Collection<CartItem> items = Cart.getItems();
        CartItem item = Iterables.firstOf(items);

        assertEquals(imageUrl, item.imageUrl);
    }

    @Test
    public void addItem_withLinkUrl_includesUrlInCartItem() throws UnsupportedEncodingException, InstantiationException {
        String linkUrl = "linkUrl";

        Cart.addItem("sku", 1, 1.0, "title", null, linkUrl);

        Collection<CartItem> items = Cart.getItems();
        CartItem item = Iterables.firstOf(items);

        assertEquals(linkUrl, item.linkUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_withNullSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem(null, 1, 1.0, "title");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_withNullTitle_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_withZeroQuantity_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 0, 1.0, "title");
    }

    @Test
    public void updateItemQuantity_withValidArgs_updatesCartItemQuantity() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        Cart.updateItemQuantity("sku", 2);
        CartItem item = Cart.getItem("sku");
        assertEquals(2, item.quantity);
    }

    @Test
    public void updateItemQuantity_withValidArgs_callsService() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        Cart.updateItemQuantity("sku", 2);
        verify(mockListrackService, atLeast(2)).updateCart(ArgumentMatchers.<CartItem>anyCollection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateItemQuantity_withNullSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.updateItemQuantity(null, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateItemQuantity_withZeroQuantity_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.updateItemQuantity("sku", 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void updateItemQuantity_withNonExistentSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.updateItemQuantity("sku-doesnt-exist", 2);
    }
    
    @Test
    public void removeItem_withValidArgs_removesFromMap() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        Cart.removeItem("sku");
        assertFalse(Cart.hasItem("sku"));
    }

    @Test
    public void removeItem_withValidArgs_callsService() throws UnsupportedEncodingException, InstantiationException {
        Cart.addItem("sku", 1, 1.0, "title");
        Cart.removeItem("sku");
        verify(mockListrackService, atLeast(2)).updateCart(ArgumentMatchers.<CartItem>anyCollection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItem_withNullSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.removeItem(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeItem_withNonExistentSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Cart.removeItem("sku-doesnt-exist");
    }

    @Test
    public void clearItems_emptiesCart() throws UnsupportedEncodingException, InstantiationException {
        Cart.clearItems();
        assertEquals(0, Cart.getItems().size());
    }

    @Test
    public void clearItems_callsService() throws UnsupportedEncodingException, InstantiationException {
        Cart.clearItems();
        verify(mockListrackService).clearCart();
    }
}