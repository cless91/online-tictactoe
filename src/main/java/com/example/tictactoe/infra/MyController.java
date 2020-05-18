package com.example.tictactoe.infra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MyController {
    @RequestMapping("/")
    public ModelAndView homePage(HttpServletResponse response) throws IOException {
        response.addCookie(new Cookie("playerId", UUID.randomUUID().toString()));

//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
        return new ModelAndView("index");
    }
}
