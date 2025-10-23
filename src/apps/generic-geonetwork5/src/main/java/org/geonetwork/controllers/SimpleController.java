/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.controllers;

import org.geonetwork.config.DaveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** controller for `/home`. This allows for run-time variables to be injected into the view. */
@Controller
public class SimpleController {

    @Autowired
    DaveConfig daveConfig;

    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("myname", daveConfig.getFirstName());
        model.addAttribute("myname2", daveConfig.getLastName());

        return "test";
    }
}
