package com.springboot.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index(Model model){
        model.addAttribute("name", "shiyaam");
        return "index";
    }

    @RequestMapping(value = "/home")
    @ResponseBody
    public String index1(){
        return "index";
    }
}
