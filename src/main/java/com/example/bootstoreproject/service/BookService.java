package com.example.bootstoreproject.service;

import com.example.bootstoreproject.dao.AuthorDao;
import com.example.bootstoreproject.dao.BookDao;
import com.example.bootstoreproject.ds.Cart;
import com.example.bootstoreproject.ds.CartItems;
import com.example.bootstoreproject.entity.Author;
import com.example.bootstoreproject.entity.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    private final Cart cart;

    public BookService(AuthorDao authorDao, BookDao bookDao, Cart cart) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
        this.cart = cart;
    }
    public void saveAuthor(Author author){
        authorDao.save(author);
    }

    @Transactional
    public void saveBook(Book book){
        Author author = authorDao.findById(book.getAuthor().getId()).get();
        author.addBook(book);
        bookDao.save(book);
    }

    public List<Author> listAuthors(){
        return authorDao.findAll();
    }
    public List<Book> listBooks(){
        return bookDao.findAll();
    }

//    Delete Book
    @Transactional
    public void removeBook(int bookId){
//        THis is only for knowledge (bts), No database operation
        Book book = bookDao.findById(bookId).get();
        Author author = book.getAuthor();
        author.removeBook(book);
//        if (bookDao.existsById(bookId)){
//            bookDao.deleteById(bookId);
//        }else {
//            throw new EntityNotFoundException(bookId + "Not Foun!");
//        }
    }

//    Update Book
    public Book findBookById(int id){
        return bookDao.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Book book){
        Book existBook = findBookById(book.getId());
        existBook.setAuthor(book.getAuthor());
        existBook.setId(book.getId());
        existBook.setTitle(book.getTitle());
        existBook.setPrice(book.getPrice());
        existBook.setPublisher(book.getPublisher());
        existBook.setYearPublished(book.getYearPublished());
        existBook.setImgUrl(book.getImgUrl());
    }

    public void updateAgain(Book updateBook){
        bookDao.saveAndFlush(updateBook);
    }

    public void addToCart(int id){
        Book book = findBookById(id);
        cart.addToCart(new CartItems(
                book.getId(),
                book.getTitle(),
                book.getPrice(),
                1
        ));
    }

    public int cartSize(){
       return cart.cartSize();
    }

    public Set<CartItems> getCartItems(){
        return cart.getCartItems();
    }

    public Set<CartItems> removeFromCart(int id){
        Set<CartItems> CartItems = getCartItems().stream()
                .filter(i -> i.getId()!= id)
                .collect(Collectors.toSet());
        cart.setCartItems(CartItems);
        return CartItems;
    }

    public void clearCart(){
        cart.clearCart();
    }

}



















