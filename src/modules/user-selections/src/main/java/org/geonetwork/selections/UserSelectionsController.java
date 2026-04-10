/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.selections;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.geonetwork.constants.ApiParams;
import org.geonetwork.domain.Selection;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSelectionsController {

    private final UserSelectionsService userSelectionsService;

    @Autowired
    public UserSelectionsController(UserSelectionsService userSelectionsService) {
        this.userSelectionsService = userSelectionsService;
    }

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Get list of user selection sets")
    public @ResponseBody @ResponseStatus(HttpStatus.OK) List<Selection> getSelectionList() {
        return userSelectionsService.retrieveAllSelections();
    }

    @GetMapping(value = "/{selectionIdentifier}/items")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get records in a user selection set")
    @PreAuthorize("hasAuthority('Guest')")
    public @ResponseBody @ResponseStatus(HttpStatus.OK) List<String> getUserSelectionRecords(
            @Parameter(description = "Selection identifier", required = true) @PathVariable Integer selectionIdentifier,
            @Parameter(description = "User identifier") @RequestParam(required = false) Integer userIdentifier,
            @AuthenticationPrincipal UserDetails userDetails)
            throws Exception {

        return userSelectionsService.retrieveUserSelectionItems(selectionIdentifier, userDetails, userIdentifier);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Add items to a user selection set")
    @PutMapping(value = "/{selectionIdentifier}/items")
    @PreAuthorize("hasAuthority('Guest')")
    public @ResponseBody @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Records added to selection set."),
                @ApiResponse(responseCode = "404", description = "Selection or user or at least one UUID not found."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN)
            }) ResponseEntity<String> addUserSelectionRecords(
            @Parameter(description = "Selection identifier", required = true) @PathVariable Integer selectionIdentifier,
            @Parameter(description = "User identifier") @RequestParam(required = false) Integer userIdentifier,
            @Parameter(description = "One or more record UUIDs.") @RequestParam(required = false) String[] uuid,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            userSelectionsService.addItemsToUserSelection(selectionIdentifier, userIdentifier, uuid, userDetails);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{selectionIdentifier}/items")
    @io.swagger.v3.oas.annotations.Operation(summary = "Remove items to a user selection set")
    @PreAuthorize("hasAuthority('Guest')")
    public @ResponseBody @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Items removed from a set.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "404", description = "Selection or user not found."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN)
            }) ResponseEntity<Void> deleteUserSelectionRecords(
            @Parameter(description = "Selection identifier", required = true) @PathVariable Integer selectionIdentifier,
            @Parameter(description = "User identifier") @RequestParam(required = false) Integer userIdentifier,
            @Parameter(description = "One or more record UUIDs. If null, remove all.") @RequestParam(required = false)
                    String[] uuid,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails)
            throws Exception {

        userSelectionsService.deleteUserSelectionRecords(selectionIdentifier, userIdentifier, uuid, userDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{selectionIdentifier}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update a user selection set")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Selection updated.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "404", description = "Selection not found."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN)
            })
    @PreAuthorize("hasAuthority('UserAdmin')")
    public ResponseEntity<Void> updateUserSelection(
            @Parameter(description = "Selection identifier", required = true) @PathVariable Integer selectionIdentifier,
            @Parameter(name = "selection") @RequestBody Selection selection)
            throws Exception {

        userSelectionsService.updateUserSelection(selectionIdentifier, selection);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{selectionIdentifier}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Remove a user selection set")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Selection removed.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "404", description = "Selection not found."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN)
            })
    @PreAuthorize("hasAuthority('UserAdmin')")
    public ResponseEntity<Void> deleteUserSelection(
            @Parameter(description = "Selection identifier", required = true) @PathVariable Integer selectionIdentifier)
            throws Exception {

        userSelectionsService.deleteUserSelection(selectionIdentifier);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Add a user selection set")
    @PutMapping
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Selection created."),
                @ApiResponse(responseCode = "400", description = "A selection with that id or name already exist."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN)
            })
    @PreAuthorize("hasAuthority('UserAdmin')")
    @ResponseBody
    public ResponseEntity<Integer> createPersistentSelectionType(
            @Parameter(name = "selection") @RequestBody Selection selection) {

        Selection createdSelection = userSelectionsService.addUserSelection(selection);
        return new ResponseEntity<>(createdSelection.getId(), HttpStatus.CREATED);
    }
}
