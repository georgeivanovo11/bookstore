package com.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DeliveryController {
	
	@Autowired
	private DeliveryService service;

	@PostMapping("/deliveries")
	public @ResponseBody ResponseEntity<HttpStatus> deliver(@RequestBody DeliveryView view) throws EntityNotFoundException, InvalidInputDataException{
		service.deliver(view);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
}
