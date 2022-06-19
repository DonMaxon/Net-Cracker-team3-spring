package com.example.demo.basicClasses.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        System.out.println("STATUS="+status);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("errorCode", statusCode);
        }
        if(message != null){
            model.addAttribute("message", message);
        }
        return "error";
    }

}
