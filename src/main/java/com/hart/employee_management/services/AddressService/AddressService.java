package com.hart.employee_management.services.AddressService;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Address;
import com.hart.employee_management.repository.AddressRepository;
import com.hart.employee_management.request.AddressRequest;
import com.hart.employee_management.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final EmployeeService employeeService;
    private final AddressRepository addressRepository;

    @Override
    public Address findAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new CustomException("Address not found"));
    }

    @Override
    public Address createAddress(AddressRequest address, Long employeeId) {
        var employee = employeeService.findEmployeeById(employeeId);
        Address employeeAddress = new Address();
        employeeAddress.setCity(address.getCity());
        employeeAddress.setState(address.getState());
        employeeAddress.setStreet(address.getStreet());
        employeeAddress.setCountry(address.getCountry());
        employeeAddress.setEmployee(employee);
        return addressRepository.save(employeeAddress);
    }

    @Override
    public Address updatedAddress(AddressRequest address, Long addressId) {
        Address employeeAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException("AddressService not found"));
        employeeAddress.setCity(address.getCity());
        employeeAddress.setState(address.getState());
        employeeAddress.setStreet(address.getStreet());
        employeeAddress.setCountry(address.getCountry());
        return addressRepository.save(employeeAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.findById(addressId).orElseThrow(() -> new CustomException("Address not found"));
        addressRepository.deleteById(addressId);
    }
}
