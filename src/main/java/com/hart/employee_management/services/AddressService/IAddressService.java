package com.hart.employee_management.services.AddressService;

import com.hart.employee_management.model.Address;
import com.hart.employee_management.request.AddressRequest;

public interface IAddressService {
    Address findAddressById(Long id);
    Address createAddress(AddressRequest address, Long employeeId);
    Address updatedAddress(AddressRequest address, Long addressId);
    void deleteAddress(Long addressId);
}
