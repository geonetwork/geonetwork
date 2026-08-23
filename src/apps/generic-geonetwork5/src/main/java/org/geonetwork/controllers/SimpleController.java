/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
