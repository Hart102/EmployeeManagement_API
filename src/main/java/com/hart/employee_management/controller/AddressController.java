package com.hart.employee_management.controller;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Address;
import com.hart.employee_management.request.AddressRequest;
import com.hart.employee_management.response.ApiResponse;
import com.hart.employee_management.services.AddressService.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse> findAddressById(@PathVariable Long addressId) {
        try {
            Address address = addressService.findAddressById(addressId);
            return ResponseEntity.ok(new ApiResponse(false, "Address retrieved", address));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAddress(
            @RequestBody AddressRequest request, @RequestParam  Long userAddress) {
        try {
            Address response = addressService.createAddress(request, userAddress);
            return ResponseEntity.ok(new ApiResponse(false, "Address created successfully", response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PutMapping("/update/address-id/{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(@RequestBody AddressRequest request, @PathVariable Long addressId) {
        try {
            Address response = addressService.updatedAddress(request, addressId);
            return ResponseEntity.ok(new ApiResponse(false, "Address updated successfully", response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(true, e.getMessage(), null));
        }
    }

//    @DeleteMapping("/delete/address-id/{addressId}")
//    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
//        try {
//            addressService.deleteAddress(addressId);
//            return ResponseEntity.ok(new ApiResponse(false, "Address deleted successfully", null));
//        } catch (CustomException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(true, e.getMessage(), null));
//        }
//    }
}
