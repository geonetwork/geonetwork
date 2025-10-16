package org.geonetwork.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * controller for `/home`.  This allows for run-time variables to be injected into the view.
 */
@Controller
public class HomeController {

  @Value("${geonetwork.4.url:'/geonetwork'}")
  public String gn4URL;

  @GetMapping("/home")
  public String home(Model model) {
    model.addAttribute("gn4_url",gn4URL);
    return "home";
  }

}
