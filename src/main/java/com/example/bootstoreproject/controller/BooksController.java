package com.example.bootstoreproject.controller;

import com.example.bootstoreproject.ds.Cart;
import com.example.bootstoreproject.ds.CartItems;
import com.example.bootstoreproject.entity.Author;
import com.example.bootstoreproject.entity.Book;
import com.example.bootstoreproject.entity.User;
import com.example.bootstoreproject.service.BookService;

import com.example.bootstoreproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BooksController {
    @Autowired
    private final BookService bookService;
    @Autowired
    private final UserService userService;
    private final Cart cart;

    public BooksController(BookService bookService, UserService userService, Cart cart) {
        this.bookService = bookService;
        this.userService = userService;
        this.cart = cart;
    }


    @GetMapping({"/","/home"})
    public ModelAndView index(Model model){
       // model.addAttribute("books",bookService.listBooks());
        return new ModelAndView("home","books",bookService.listBooks());
    }
    @GetMapping("/author-form")
    public String authorForm(Model model){
        model.addAttribute("author",new Author());
        return "author-form";
    }
    @PostMapping("/author-form")
    public String saveAuthor(@Validated Author author, BindingResult result){
        if (result.hasErrors()){
            return "author-form";
        }
        bookService.saveAuthor(author);
        return "redirect:/authors";
    }


    @GetMapping("/authors")
    public String listAuthor(Model model){
        model.addAttribute("authors",bookService.listAuthors());
        return "authors";
    }

    @GetMapping("/book-form")
    public String bookForm(Model model){
        model.addAttribute("authors",bookService.listAuthors());
        model.addAttribute("book",new Book());
        return "book-form";
    }

    @PostMapping("/book-form")
    public String saveBook(@Valid Book book, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        if (result.hasErrors()){
            model.addAttribute("authors",bookService.listAuthors());
            return "book-form";
        }
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/list-books";
    }

    @RequestMapping("/list-books")
    public String listAllBook(Model model){
        model.addAttribute("delete",model.containsAttribute("delete"));
        model.addAttribute("success",model.containsAttribute("success"));
        model.addAttribute("books",bookService.listBooks());
        return "books";
    }

//    Delete Book
    @GetMapping("/book/remove")
    public String removeBook(@RequestParam("id")int id,RedirectAttributes redirectAttributes){
        bookService.removeBook(id);
        redirectAttributes.addFlashAttribute("delete",true);
        return "redirect:/list-books";
    }
//      Update Book
    @GetMapping("/book/update")
    public String updateForm(@RequestParam("id")int id,Model model){
        model.addAttribute("book",bookService.findBookById(id));
        this.bookId=id;
        model.addAttribute("authors",bookService.listAuthors());
        return "book-update";
    }
    int bookId;


    @PostMapping("/book/update")
    public String saveUpdateBook(@Valid Book book,RedirectAttributes attributes,BindingResult result,Model model){
        if (result.hasErrors()){
            model.addAttribute("book",bookService.findBookById(book.getId()));
            model.addAttribute("authors",bookService.listAuthors());
            return "book-update";
        }
        book.setId(bookId);
        bookService.update(book);
        attributes.addFlashAttribute("update",true);
        return "redirect:/list-books";
    }
    String imgUrl;
    Author author;
    int uiUpdateId;
    @GetMapping("/book/ui-update")
    public String uiUpdate(@RequestParam("id")int id,Model model){
        Book updateBook = bookService.findBookById(id);
        imgUrl=updateBook.getImgUrl();
        this.author=updateBook.getAuthor();
        this.uiUpdateId= updateBook.getId();
        List<Book> bookList = bookService.listBooks().stream().map(b -> {
                if (b.equals(updateBook)){
                    b.setRender(true);
                }
                return b;
            }).collect(Collectors.toList());
        model.addAttribute("updateBook",updateBook);
        model.addAttribute("books",bookList);
        return "books";
    }
    @PostMapping("/book/ui/update")
    public String updateBookAgain(Book updateBook,BindingResult result){
        if (result.hasErrors()){
            return "redirect:/book/ui-update";
        }
        updateBook.setImgUrl(imgUrl);
        updateBook.setId(uiUpdateId);
        updateBook.setAuthor(author);
        updateBook.setRender(false);
        bookService.updateAgain(updateBook);
        return "redirect:/list-books";
    }

//    Detail Books
    @GetMapping("/book/details")
    public String detailsBooks(@RequestParam("id")int id,Model model){
        model.addAttribute("book",bookService.findBookById(id));
        return "details-book";
    }

//    Cart controller
    @GetMapping("/cart/add-cart")
    public String addToCart(@RequestParam("id")int id){
        bookService.addToCart(id);
        return "redirect:/book/details?id="+id;
    }

    @ModelAttribute("cartSize")
    public int cartSize(){
        return bookService.cartSize();
    }

    @GetMapping("/view-cart")
    public String viewCart(Model model){
        model.addAttribute("CartItems",new CartItems());
        model.addAttribute("changeButton",false);
        model.addAttribute("cartItems",bookService.getCartItems());
        return "cart-view";
    }

    @GetMapping("/view-cart/remove")
    public String removeCartItem(@RequestParam("id")int id){
        bookService.removeFromCart(id);
        return "redirect:/view-cart";
    }

    @ModelAttribute("totalPrice")
    public double totalPrice(){
        return bookService.getCartItems().stream()
                .map(i -> i.getPrice() * i.getQuantity())
                .mapToDouble(i -> i)
                .sum();
    }

    @GetMapping("/view-cart/clear")
    public String clearCart(){
        bookService.clearCart();
        return "redirect:/view-cart";
    }
//    ____________________________________________________________________________________
//    Check out

    private boolean changeButton;
    @ModelAttribute("changeButton")
    public boolean changeButton(){
        return changeButton;
    };
    @GetMapping("/check-out-v1")
    public String checkOutV1(Model model){
        Set<CartItems> cartItems = bookService.getCartItems()
                .stream().map(
                        i -> {
                            i.setRender(true);
                            return i;
                        }
                )
                .collect(Collectors.toSet());
        model.addAttribute("CartItems",new CartItems());
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("changeButton",true);
        return "cart-view";
    }

    @PostMapping("/check-out-v2")
    public String checkoutV2(CartItems cartItems,Model model){
        model.addAttribute("cartItems",bookService.getCartItems());
        int i=0;
        for (CartItems cartItems1:bookService.getCartItems()){
            cartItems1.setQuantity(cartItems.getQuanLinkedList().get(i));
            cartItems1.setRender(false);
            i++;
        }
        return "redirect:/view-cart";
    }
//    ------------------------------------------------------------------------------------------
//    Login in / Logout /Security

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        return "login";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model){
        model.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/register")
    public String register(User user,BindingResult result){
        if (result.hasErrors()){
            return "signup";
        }
        userService.signUp(user);
        return "redirect:/login";
    }
}






















