/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.controllerexceptions;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    @RestController
    static class ThrowingController {
        @GetMapping("/denied")
        void denied() {
            throw new AccessDeniedException("Access Denied");
        }

        @GetMapping("/unauthenticated")
        void unauthenticated() {
            throw new BadCredentialsException("bad");
        }

        @GetMapping("/boom")
        void boom() {
            throw new IllegalStateException("boom");
        }
    }

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void securityExceptionsPropagateToSpringSecurity() {
        var denied = assertThrows(ServletException.class, () -> mockMvc.perform(get("/denied")));
        assertInstanceOf(AccessDeniedException.class, denied.getCause());

        var unauth = assertThrows(ServletException.class, () -> mockMvc.perform(get("/unauthenticated")));
        assertInstanceOf(BadCredentialsException.class, unauth.getCause());
    }

    @Test
    void otherExceptionsStillMapTo500() throws Exception {
        mockMvc.perform(get("/boom")).andExpect(status().isInternalServerError());
    }
}
